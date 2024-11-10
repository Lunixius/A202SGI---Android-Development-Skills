package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final Context context;
    private List<Note> notes;
    private OnNoteClickListener onNoteClickListener;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes != null ? notes : new ArrayList<>();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes != null ? notes : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.onNoteClickListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = notes.get(position);

        Log.d("NotesAdapter", "Binding view for position: " + position + ", Note Title: " + note.getTitle());

        // Set note details
        holder.titleTextView.setText(note.getTitle());
        holder.contentEditText.setText(note.getContent());

        // Temporarily remove listener to avoid triggering on re-binding
        holder.deleteButton.setOnClickListener(null);

        // Set the Delete button behavior
        holder.deleteButton.setOnClickListener(v -> {
            try {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onDeleteClick(note);
                }
            } catch (Exception e) {
                Log.e("NotesAdapter", "Error in Delete button onClick: " + e.getMessage());
                Toast.makeText(context, "An error occurred while deleting the note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    public interface OnNoteClickListener {
        void onDeleteClick(Note note);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        EditText contentEditText;
        Button deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            contentEditText = itemView.findViewById(R.id.noteContent);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
