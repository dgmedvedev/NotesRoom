package com.demo.notesroom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
    // 1. Метод получает все данные из БД, будет выполняться при запросе к БД, поэтому @Query
    // Дополнительно (не обязательно) отсортировали данные "ORDER BY dayOfWeek" ASC/DESC по-порядку
    // При вызове метода, будет сформирован запрос и вернется ArrayList
    @Query("SELECT * FROM notes ORDER BY dayOfWeek ASC")
    LiveData<List<Note>> getAllNotes();

    // 2. Метод добавления данных в БД. Будет вставлять данные поэтому @Insert
    @Insert
    void insertNote(Note note);

    // 3. Метод удаления данных из БД.
    @Delete
    void deleteNote(Note note);

    // 4. Удаляет все данные из БД. Обращается к БД с запросом, поэтому @Query
    @Query("DELETE FROM notes")
    void deleteAllNotes();
}