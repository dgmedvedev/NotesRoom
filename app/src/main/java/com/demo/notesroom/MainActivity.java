package com.demo.notesroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton buttonAddNote;
    private RecyclerView recyclerViewNotes;
    private final ArrayList<Note> notes = new ArrayList<>();
    NotesAdapter adapter;

    //private NotesDatabase database; // 1 DB
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database = NotesDatabase.getInstance(this); // 2 DB
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        getData(); // 4 DB
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        buttonAddNote = findViewById(R.id.buttonAddNote);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);

        // добавляем созданный адаптер
        adapter = new NotesAdapter(notes);
        // указываем расположение элементов, в данном случае (вертикальное последовательное)
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        // горизонтальное последовательное расположение
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // расположение сеткой
        //recyclerViewNotes.setLayoutManager(new GridLayoutManager(this, 3));

        // устанавливаем у RecyclerView созданный адаптер
        recyclerViewNotes.setAdapter(adapter);

        // устанавливаем слушателя событий, которого создали. в RecyclerView нет встроенного метода
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this, "Номер позиции: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
            }
        });

        buttonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        });

        // создаем объект itemTouchHelper для управления RecyclerView с помощью свайпа - сдвига элемента
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // direction отвечает за действие, в зависимости от сдвига влево или вправо (swipeDirs)
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        remove(viewHolder.getAdapterPosition());
                    }
                });
        // теперь необходимо объект itemTouchHelper применить к RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private void remove(int position) { // 5 DB
        // Note note = notes.get(position);
        Note note = adapter.getNotes().get(position);
        // database.notesDao().deleteNote(note);
        viewModel.deleteNote(note);
    }

    private void getData() { // 3 DB
        //LiveData<List<Note>> notesFromDB = database.notesDao().getAllNotes();
        LiveData<List<Note>> notesFromDB = viewModel.getNotes();
        notesFromDB.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesFromLiveData) {
//                notes.clear();
//                notes.addAll(notesFromLiveData);
//                // мгновенно обновляет RecyclerView, при добавлении или удалении элемента
//                // без метода notifyDataSetChanged() приложение ломается
//                adapter.notifyDataSetChanged();
                adapter.setNotes(notesFromLiveData);
            }
        });
    }
}