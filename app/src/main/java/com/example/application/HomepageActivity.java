package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button taskManagementButton;
    private Button notesButton;
    private Button profileButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize buttons and TextView
        logoutButton = findViewById(R.id.logoutButton);
        taskManagementButton = findViewById(R.id.taskManagementButton);
        notesButton = findViewById(R.id.notesButton);
        profileButton = findViewById(R.id.profileButton);
        welcomeText = findViewById(R.id.welcomeText);

        // Get the username from SharedPreferences (or Database)
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User"); // default to "User" if not found

        // Set the welcome message with the username
        welcomeText.setText("Welcome, " + username + "!");

        // Logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear session data if necessary
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all stored data
                editor.apply();

                // Redirect to LoginActivity
                Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Task Management button click listener
        taskManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Notes button click listener
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        // Profile button click listener
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to ProfileActivity
                Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
