package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText userInput, password;
    Button loginButton;
    TextView registerLink, changePasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInput = findViewById(R.id.userInput);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        changePasswordLink = findViewById(R.id.changePasswordLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInputValue = userInput.getText().toString();
                String passwordInput = password.getText().toString();

                // Check if userInput is an email or username
                if (Patterns.EMAIL_ADDRESS.matcher(userInputValue).matches()) {
                    // It's an email
                    loginUserWithEmail(userInputValue, passwordInput);
                } else {
                    // It's a username
                    loginUserWithUsername(userInputValue, passwordInput);
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        changePasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to ChangePasswordActivity
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUserWithEmail(String email, String password) {
        // Logic to authenticate with email
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUserWithUsername(String username, String password) {
        // Logic to authenticate with username
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
