package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText userInput, password;
    Button loginButton;
    TextView registerLink, changePasswordLink;

    TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new TaskDatabaseHelper(this);

        userInput = findViewById(R.id.userInput);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        changePasswordLink = findViewById(R.id.changePasswordLink);

        loginButton.setOnClickListener(view -> {
            String userInputValue = userInput.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (userInputValue.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the input is an email or username
            if (Patterns.EMAIL_ADDRESS.matcher(userInputValue).matches()) {
                // It's an email
                loginUserWithEmail(userInputValue, passwordInput);
            } else {
                // It's a username
                loginUserWithUsername(userInputValue, passwordInput);
            }
        });

        registerLink.setOnClickListener(view -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        changePasswordLink.setOnClickListener(view -> {
            // Navigate to ChangePasswordActivity
            Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    // Method to login with email
    private void loginUserWithEmail(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            // Login success
            saveUsername(email); // Save the email in SharedPreferences
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the login activity so the user can't go back to it
        } else {
            // Login failure
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    // Method to login with username
    private void loginUserWithUsername(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.moveToFirst()) {
            // Login success
            saveUsername(username); // Save the username in SharedPreferences
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the login activity so the user can't go back to it
        } else {
            // Login failure
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    // Method to save username in SharedPreferences
    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply(); // or editor.commit();
    }
}
