package com.example.application;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private EditText titleInput, contentInput;
    private NotesAdapter notesAdapter;
    private NotesDatabaseHelper notesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Initialize the views
        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        Button saveNoteButton = findViewById(R.id.saveNoteButton);
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        ImageButton backButton = findViewById(R.id.backButton);

        // Initialize the database helper
        notesDatabase = new NotesDatabaseHelper(this);

        // Set up RecyclerView with an empty adapter initially
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, fetchNotesFromDatabase());
        notesRecyclerView.setAdapter(notesAdapter);

        // Back Button functionality
        backButton.setOnClickListener(v -> finish());

        // Save Note Button
        saveNoteButton.setOnClickListener(v -> saveNote());

        // Handle item clicks in the RecyclerView
        notesAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onDeleteClick(Note note) {
                confirmDeleteNote(note);
            }
        });
    }

    // Fetch notes from the database
    private List<Note> fetchNotesFromDatabase() {
        List<Note> notes = notesDatabase.getAllNotes();
        return notes != null ? notes : List.of();  // Ensure it returns a non-null list
    }

    // Save a new note to the database
    private void saveNote() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in both title and content", Toast.LENGTH_SHORT).show();
        } else {
            Note note = new Note(title, content);
            long result = notesDatabase.addNote(note);
            if (result > 0) {
                Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
                clearInputs();
                refreshNotesList();
            } else {
                Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Clear the input fields
    private void clearInputs() {
        titleInput.setText("");
        contentInput.setText("");
    }

    // Refresh the notes list after adding or deleting a note
    private void refreshNotesList() {
        notesAdapter.setNotes(fetchNotesFromDatabase());
        notesAdapter.notifyDataSetChanged();
    }

    // Confirm deletion of a note
    private void confirmDeleteNote(final Note note) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> deleteNote(note))
                .setNegativeButton("No", null)
                .show();
    }

    // Delete a note from the database
    private void deleteNote(Note note) {
        boolean deleted = notesDatabase.deleteNote(note.getId());
        if (deleted) {
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            refreshNotesList();
        } else {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
        }
    }
}
