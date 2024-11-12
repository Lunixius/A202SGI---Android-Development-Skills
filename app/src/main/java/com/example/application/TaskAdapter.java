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
        Task task = taskList.get(position);

        holder.taskName.setText(task.getTaskName());

        // Set CheckBox based on the completed status from the database
        holder.checkBox.setOnCheckedChangeListener(null); // Temporarily remove listener
        holder.checkBox.setChecked(task.isCompleted());

        // Add listener to update the completed status in the database
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);

            // Update the completed status in the database
            TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(context);
            dbHelper.updateTaskCompletionStatus(task.getId(), isChecked ? 1 : 0);

            // Optionally, refresh the item to ensure UI consistency
            notifyItemChanged(position);
        });

        // Handle click on task item to show Edit/Delete dialog
        holder.itemView.setOnClickListener(v -> handleTaskItemClick(task, position));
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
