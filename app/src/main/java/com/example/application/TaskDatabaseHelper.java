package com.example.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";  // Database name
    private static final int DATABASE_VERSION = 1;           // Database version

    // Constructor
    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // Create tasks table
            String CREATE_TASKS_TABLE = "CREATE TABLE tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "taskName TEXT NOT NULL," +
                    "completed INTEGER DEFAULT 0)";
            db.execSQL(CREATE_TASKS_TABLE);

            // Create users table
            String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +  // Ensure email is unique
                    "password TEXT NOT NULL)";
            db.execSQL(CREATE_USERS_TABLE);

            // Create an index for the email column in the users table
            String CREATE_EMAIL_INDEX = "CREATE INDEX idx_users_email ON users(email)";
            db.execSQL(CREATE_EMAIL_INDEX);

            db.setTransactionSuccessful();  // Mark transaction as successful
        } finally {
            db.endTransaction();  // End the transaction (rolls back if not successful)
        }
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop both tasks and users tables if they exist
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");

        // Recreate the tables
        onCreate(db);
    }
}
