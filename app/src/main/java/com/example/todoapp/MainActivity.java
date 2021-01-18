package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton addNewNote;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewNote = findViewById(R.id.image_button);
        //Implement from here - click listener
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use note_input.xml layout
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.note_input, null, false);

                //Get id of the field
                EditText editTitle = viewInput.findViewById(R.id.edit_title);
                EditText editDescription = viewInput.findViewById(R.id.edit_description);

                // Alert dialog. Set from here button and title alert name
                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Get info from input fields
                                String title = editTitle.getText().toString();
                                String description = editDescription.getText().toString();

                                //Create new note object with info from input fields
                                Note note = new Note(title, description);

                                // Add note obj to db and check insertion is ok
                                boolean isInserted = new NoteHandler(MainActivity.this).create(note);

                                if (isInserted) {
                                    Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                } else {
                                    Toast.makeText(MainActivity.this, "Unable to save the note", Toast.LENGTH_SHORT).show();
                                }
                                //Close dialog after adding
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // Callback fo swiping item from recycler view and after that delete it
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadNotes();
    }

    //MainActivity => readNotes()
    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new NoteHandler(this).readNotes();
        return notes;
    }

    //Get ArrayList with objects from MainActivity => readNotes() which refer to NoteHandler => readNotes()
    //Create new recyclerView element and NoteAdapter
    public void loadNotes() {
        notes = readNotes();
        noteAdapter = new NoteAdapter(notes,this);
        recyclerView.setAdapter(noteAdapter);
    }
}
