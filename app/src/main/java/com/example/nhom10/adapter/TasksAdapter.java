package com.example.nhom10.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.activity.TaskDetailActivity;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final TaskDAO taskDAO;

    public TasksAdapter(List<Task> taskList, TaskDAO taskDAO) {
        this.taskList = taskList;
        this.taskDAO = taskDAO;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());

        Date dueDate = task.getDueDate();
        if (dueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(dueDate);
            holder.taskTime.setText(formattedDate);
        } else {
            holder.taskTime.setText("No due date");
        }

        holder.taskCheckBox.setChecked(task.isCompleted());
        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            taskDAO.updateTaskStatus(task.getTaskId(), isChecked);

            task.setCompleted(isChecked);
        });

        holder.itemView.setOnClickListener(view -> {
            int taskId = task.getTaskId();
            Intent intent = new Intent(view.getContext(), TaskDetailActivity.class);
            intent.putExtra("task_id", taskId);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateTasks(List<Task> newTaskList) {
        this.taskList = newTaskList;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskTime;
        CheckBox taskCheckBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
            taskCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }
}