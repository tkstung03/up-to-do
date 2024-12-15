package com.example.nhom10.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.activity.TaskDetailActivity;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Category;
import com.example.nhom10.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final TaskDAO taskDAO;
    private final CategoryDAO categoryDAO;
    private final TaskTagsDAO taskTagsDAO;

    public TasksAdapter(List<Task> taskList, TaskDAO taskDAO, CategoryDAO categoryDAO, TaskTagsDAO taskTagsDAO) {
        this.taskList = taskList;
        this.taskDAO = taskDAO;
        this.categoryDAO = categoryDAO;
        this.taskTagsDAO = taskTagsDAO;
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
            holder.taskTime.setText("Vô thời hạn");
        }

        Category category = categoryDAO.getCategoryById(task.getCategoryId());
        if (category != null) {

            //Xử lý icon
            holder.textViewCate.setText(category.getName());
            if (category.getIcon() != null && !category.getIcon().isEmpty()) {
                int iconResId = holder.itemView.getContext().getResources().getIdentifier(
                        category.getIcon(), "drawable", holder.itemView.getContext().getPackageName()
                );

                if (iconResId != 0) {
                    holder.imageViewCate.setImageResource(iconResId);
                } else {
                    holder.imageViewCate.setImageResource(R.drawable.ic_block);
                }
            } else {
                holder.imageViewCate.setImageResource(R.drawable.ic_block);
            }
        } else {
            holder.textViewCate.setText("Chưa có");
            holder.imageViewCate.setImageResource(R.drawable.ic_block);
        }

        // Xử lý màu
        Drawable originalDrawable = holder.layoutCategoryButton.getBackground();
        GradientDrawable backgroundDrawable;
        if (originalDrawable instanceof GradientDrawable) {
            backgroundDrawable = (GradientDrawable) originalDrawable.mutate();
        } else {
            backgroundDrawable = new GradientDrawable();
        }
        if (category != null && category.getColor() != null && !category.getColor().isEmpty()) {
            int parsedColor = Color.parseColor(category.getColor());
            backgroundDrawable.setColor(parsedColor);
        } else {
            backgroundDrawable.setColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lavender));
        }

        int tagCount = taskTagsDAO.getTagCountByTaskId(task.getTaskId());
        holder.textViewTags.setText(String.valueOf(tagCount));

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

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskTime;
        CheckBox taskCheckBox;
        ImageView imageViewCate;
        TextView textViewCate;
        TextView textViewTags;
        LinearLayout layoutCategoryButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
            taskCheckBox = itemView.findViewById(R.id.checkbox);
            imageViewCate = itemView.findViewById(R.id.iconCate);
            textViewCate = itemView.findViewById(R.id.nameCate);
            textViewTags = itemView.findViewById(R.id.textTags);
            layoutCategoryButton = itemView.findViewById(R.id.layoutCategoryButton);
        }
    }
}