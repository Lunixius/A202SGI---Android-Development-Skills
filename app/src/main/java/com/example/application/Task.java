package com.example.application;

public class Task {

    private int id; // ID field
    private String taskName;
    private boolean completed = false; // Initialize to false by default

    // Constructor for new tasks
    public Task(String taskName) {
        this.taskName = taskName;
    }

    // Constructor for tasks retrieved from the database
    public Task(int id, String taskName, boolean completed) {
        this.id = id;
        this.taskName = taskName;
        this.completed = completed;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Optional: Convenience method to mark a task as completed
    public void markCompleted() {
        this.completed = true;
    }

    // Optional: Toggle the completion status
    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    // Override toString() for easy debugging or logging
    @Override
    public String toString() {
        return "Task{id=" + id + ", taskName='" + taskName + "', completed=" + completed + '}';
    }
}
