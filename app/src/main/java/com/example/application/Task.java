package com.example.application;

public class Task {
    private String taskName;
    private boolean isCompleted;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isCompleted = false; // By default, a new task is not completed
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
