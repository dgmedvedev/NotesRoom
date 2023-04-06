package com.demo.notesroom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerDaysOfWeek;
    private RadioGroup radioGroupPriority;
    private Button buttonSaveNote;

    // private NotesDatabase database; // 1 DB
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        //database = NotesDatabase.getInstance(this); // 2 DB
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerDaysOfWeek = findViewById(R.id.spinnerDaysOfWeek);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);

        buttonSaveNote.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            int dayOfWeek = spinnerDaysOfWeek.getSelectedItemPosition() + 1;
            int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioButtonId);
            int priority = Integer.parseInt(radioButton.getText().toString());

            // 3  DB. Добавляем данные в БД
            if (isFilled(title, description)) {
                Note note = new Note(title, description, dayOfWeek, priority);
                // database.notesDao().insertNote(note);
                viewModel.insertNote(note);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.warning_fill_fields, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Проверяем, заполнены ли значения
    private boolean isFilled(String title, String description) {
        return !title.isEmpty() && !description.isEmpty();
    }
}