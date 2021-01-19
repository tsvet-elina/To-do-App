package com.example.todoapp;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    ArrayList<Note> notes;
    Context context;
    NoteClickListener noteClickListener;
    ViewGroup parent;

    public NoteAdapter(ArrayList<Note> notes, Context context, NoteClickListener noteClickListener) {
        this.notes = notes;
        this.context = context;
        this.noteClickListener = noteClickListener;
    }

    public NoteAdapter(ArrayList<Note> notes, MainActivity mainActivity) {
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_holder,parent,false);
        this.parent = parent;
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.desc.setText(notes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView editImg;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_note_name);
            desc = itemView.findViewById(R.id.txt_note_info);
            editImg = itemView.findViewById(R.id.edit_button);

            //After click on view show multiline description.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (desc.getMaxLines() == 1) {
                        desc.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        desc.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteClickListener.onItemClick(getAdapterPosition(),itemView);
                }
            });

        }
    }

    interface NoteClickListener {
        void onItemClick(int positionOfTheNote, View view);
    }
}
