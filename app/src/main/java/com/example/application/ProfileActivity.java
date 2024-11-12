package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views for profile
        Button backButton = findViewById(R.id.backButton);
        TextView profileTitle = findViewById(R.id.profileTitle);
        TextView usernameText = findViewById(R.id.usernameText);
        TextView emailText = findViewById(R.id.emailText);

        // Get user info from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        String email = sharedPreferences.getString("email", "user@example.com");

        // Set profile title and user information
        profileTitle.setText("Profile Information");
        usernameText.setText("Username: " + username);
        emailText.setText("Email: " + email);

        // Back button click listener to go back to HomepageActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
