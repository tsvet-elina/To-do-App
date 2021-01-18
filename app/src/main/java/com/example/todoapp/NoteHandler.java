package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteHandler extends DatabaseHelper{
    public NoteHandler(Context context) {
        super(context);
    }

    //CRUD C create, R read, U update, D delete

    public boolean create(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessful = db.insert("Note",null, values) > 0;
        db.close();

        return isSuccessful;
    }

    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Note ORDER BY id ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                Note note = new Note(title, description);
                note.setId(id);
                notes.add(note);
            } while(cursor.moveToNext());
            cursor.close();
            db.close();
        }

        return notes;
    }

    public Note readSingleNote(int id) {
        Note note = null;
        String sqlQuery = "SELECT * FROM Note where id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor.moveToFirst()) {
            int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            note = new Note(title, description);
            note.setId(noteId);
        }
        cursor.close();
        db.close();

        return note;
    }

    public boolean update(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessful = db.update("Note",values,"id='" + note.getId() + "'", null) > 0;
        db.close();

        return isSuccessful;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = db.delete("Note", "id='" + id + "'", null) > 0;
        db.close();

        return isDeleted;
    }
}
