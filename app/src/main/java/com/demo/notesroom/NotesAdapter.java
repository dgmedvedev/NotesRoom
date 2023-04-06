package com.demo.notesroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    List<Note> notes;

    private OnNoteClickListener onNoteClickListener;

    public NotesAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    // В RecyclerView нет встроенного setOnClickListener(), поэтому нужно создавать свой Listener
    interface OnNoteClickListener {
        void onNoteClick(int position);

        void onLongClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // передаем макет заметки в конструктор NotesViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    // принимает объект NotesViewHolder и порядковый номер элемента массива
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int position) {
        Note note = notes.get(position);
        notesViewHolder.textViewTitle.setText(note.getTitle());
        notesViewHolder.textViewDescription.setText(note.getDescription());
        notesViewHolder.textViewDayOfWeek.setText(getDayAsString(note.getDayOfWeek()));
        int colorId;
        int priority = note.getPriority();
        switch (priority) {
            case 1:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_red_light);
                break;
            case 2:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_orange_light);
                break;
            default:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_green_light);
                break;
        }
        notesViewHolder.textViewTitle.setBackgroundColor(colorId);
    }

    @Override
    // возвращает кол-во элементов в массиве
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDayOfWeek;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            itemView.setOnClickListener(view -> {
                // onClick(View view)
                if (onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(view -> {
                // onLongClick(View view)
                if (onNoteClickListener != null) {
                    onNoteClickListener.onLongClick(getAdapterPosition());
                }
                return true; // если указать false, то сработает и метод onClick(View view)
            });
        }
    }

    public static String getDayAsString(int position) {
        switch (position) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            default:
                return "Воскресенье";
        }
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public List<Note> getNotes() {
        return notes;
    }
}