package com.example.application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password, confirmPassword;
    Button registerButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Find views by ID
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Handle Register Button click
        registerButton.setOnClickListener(view -> {
            // Get text from EditTexts
            String usernameInput = username.getText().toString().trim();
            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString();
            String confirmPasswordInput = confirmPassword.getText().toString();

            // Validate input and register the user
            if (validateInput(username, email, password, confirmPassword)) {
                registerUser(usernameInput, emailInput, passwordInput);
            }
        });

        // Handle Login Link click
        loginLink.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Validate input fields
    private boolean validateInput(EditText username, EditText email, EditText password, EditText confirmPassword) {
        if (username.getText().toString().trim().isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return false;
        }

        if (email.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.setError("Valid email is required");
            email.requestFocus();
            return false;
        }

        if (password.getText().toString().isEmpty() || password.getText().toString().length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return false;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    // Registration logic
    private void registerUser(String username, String email, String password) {
        // Hash the password for security (placeholder for actual hashing logic)
        String hashedPassword = hashPassword(password);

        // Insert new user into the SQLite database
        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", hashedPassword);  // Store hashed password

        long newRowId = db.insert("users", null, values);

        if (newRowId != -1) {
            // User registration successful
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            // Delay a bit before navigating to LoginActivity
            new android.os.Handler().postDelayed(() -> {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 1000); // 1 second delay
        } else {
            // Handle registration failure (e.g., show a Toast)
            Toast.makeText(this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    // Placeholder function to hash passwords (replace with a real hashing function like SHA-256 or bcrypt)
    private String hashPassword(String password) {
        // Implement proper hashing here
        return password;  // For now, returning plain text (NOT SECURE)
    }
}
