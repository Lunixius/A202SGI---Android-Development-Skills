package com.example.application;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.scheduling.Task;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private String username = "User"; // Replace with actual logged-in username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        EditText editTextTask = dialogView.findViewById(R.id.editTextTask);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        confirmButton.setOnClickListener(v -> {
            String taskName = editTextTask.getText().toString().trim();
            if (!taskName.isEmpty()) {
                addTask(taskName); // Add your task to the list
                dialog.dismiss(); // Dismiss the dialog
            } else {
                editTextTask.setError("Task name cannot be empty");
            }
        });

        dialog.show();
    }

    private void addTask(String taskName) {
        Task newTask = new Task(taskName);
        taskList.add(newTask);
        taskAdapter.notifyItemInserted(taskList.size() - 1); // Notify adapter of new item
    }

    public void showEditDeleteDialog(Task task, int position) {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_delete, null);
        EditText editTextTask = dialogView.findViewById(R.id.editTextTask);
        Button editButton = dialogView.findViewById(R.id.editButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);

        editTextTask.setText(task.getTaskName()); // Pre-fill with current task name

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Handle the Delete button click
        deleteButton.setOnClickListener(v -> {
            taskList.remove(position);
            taskAdapter.notifyItemRemoved(position); // Notify adapter of removal
            dialog.dismiss();
        });

        dialog.show();
    }
}
