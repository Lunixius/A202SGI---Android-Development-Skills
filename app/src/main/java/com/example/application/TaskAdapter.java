package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(holder.getAdapterPosition());  // Use getAdapterPosition()

        holder.taskName.setText(task.getTaskName());

        // Temporarily remove listener to avoid triggering on re-binding
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());

        // Set CheckBox state based on task completion
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            // Optionally, save the task's completed status in the database here
            // Example: ((MainActivity) context).updateTask(task);
        });

        // Handle click on task item to show Edit/Delete dialog
        holder.itemView.setOnClickListener(v -> handleTaskItemClick(task, holder.getAdapterPosition()));
    }

    private void handleTaskItemClick(Task task, int position) {
        ((MainActivity) context).showEditDeleteDialog(task, position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
