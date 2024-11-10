package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private TaskDatabaseHelper dbHelper; // For database operations
    private Button notesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this matches your XML layout

        // Retrieve username from SharedPreferences or Intent extras
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");

        // Find the TextView and set the username
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText("Welcome, " + username); // Set the username

        dbHelper = new TaskDatabaseHelper(this); // Initialize the database helper

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        loadTasksFromDatabase(); // Load tasks from database

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddTaskDialog());

        // Find the back button (previously logout button)
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomepageActivity
                Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish(); // Close MainActivity
            }
        });


    }

    // Method to show Add Task dialog
    private void showAddTaskDialog() {
        // Inflate the dialog_add_task layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        EditText editTextTask = dialogView.findViewById(R.id.editTextTask);

        // Create and show the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.confirmButton).setOnClickListener(v -> {
            String taskName = editTextTask.getText().toString().trim();
            if (!taskName.isEmpty()) {
                addTask(taskName); // Add the task to the database
                dialog.dismiss();
            } else {
                editTextTask.setError("Task name cannot be empty");
            }
        });
        dialog.show();
    }

    // Method to add task to the database and reload tasks
    private void addTask(String taskName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO tasks (taskName, completed) VALUES (?, 0)", new Object[]{taskName});

        // Reload tasks from the database after adding a new one
        taskList.clear();
        loadTasksFromDatabase();
    }

    // Method to load tasks from the SQLite database
    @SuppressLint("NotifyDataSetChanged")
    private void loadTasksFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tasks", null, null, null, null, null, null);

        taskList.clear(); // Clear list before adding new data
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex("taskName"));
            @SuppressLint("Range") boolean completed = cursor.getInt(cursor.getColumnIndex("completed")) == 1;

            Task task = new Task(id, taskName, completed);
            taskList.add(task);
        }

        cursor.close();
        db.close();
        taskAdapter.notifyDataSetChanged(); // Notify the adapter
    }

    // Method to show Edit/Delete Task dialog
    public void showEditDeleteDialog(Task task, int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_delete, null);
        EditText editTextTask = dialogView.findViewById(R.id.editTextTask);
        editTextTask.setText(task.getTaskName());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Update Task
        dialogView.findViewById(R.id.editButton).setOnClickListener(v -> {
            String updatedTaskName = editTextTask.getText().toString().trim();
            if (!updatedTaskName.isEmpty()) {
                updateTaskInDatabase(task.getId(), updatedTaskName);
                taskList.get(position).setTaskName(updatedTaskName);
                taskAdapter.notifyItemChanged(position);
                dialog.dismiss();
            } else {
                editTextTask.setError("Task name cannot be empty");
            }
        });

        // Delete Task
        dialogView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            deleteTaskFromDatabase(task.getId());
            taskList.remove(position);
            taskAdapter.notifyItemRemoved(position);
            dialog.dismiss();
        });

        dialog.show();
    }

    // Method to update a task in the database
    private void updateTaskInDatabase(int taskId, String updatedTaskName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE tasks SET taskName = ? WHERE id = ?", new Object[]{updatedTaskName, taskId});
        db.close();
    }

    // Method to delete a task from the database
    private void deleteTaskFromDatabase(int taskId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM tasks WHERE id = ?", new Object[]{taskId});
        db.close();
    }
}