package com.demo.notesroom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    private static NotesDatabase database;
    private LiveData<List<Note>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = NotesDatabase.getInstance(getApplication());
        notes = database.notesDao().getAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void insertNote(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (note != null) {
                database.notesDao().insertNote(note);
            }
        });
    }

    public void deleteNote(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (note != null) {
                database.notesDao().deleteNote(note);
            }
        });
    }

    public void deleteAllNotes() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            database.notesDao().deleteAllNotes();
        });
    }
}