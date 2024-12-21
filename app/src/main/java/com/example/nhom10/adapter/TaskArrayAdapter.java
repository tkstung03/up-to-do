package com.example.nhom10.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.nhom10.R;
import com.example.nhom10.activity.TaskDetailActivity;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.model.Category;
import com.example.nhom10.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskArrayAdapter extends ArrayAdapter {
    Activity context;
    int layoutID;
    ArrayList<Task> list = null;
    TaskDAO taskDAO ;
    CategoryDAO categoryDAO;
    public TaskArrayAdapter(@NonNull Activity context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.list = (ArrayList<Task>) objects;
    }

    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutID, null);
        taskDAO=new TaskDAO(convertView.getContext());
        categoryDAO=new CategoryDAO(convertView.getContext());
        TextView taskTitle = convertView.findViewById(R.id.taskTitle);
        TextView taskTime = convertView.findViewById(R.id.taskTime);
        CheckBox taskCheckBox = convertView.findViewById(R.id.checkbox);
        ImageView imageViewCate = convertView.findViewById(R.id.iconCate);
        TextView textViewCate = convertView.findViewById(R.id.nameCate);
        TextView textViewTags = convertView.findViewById(R.id.textTags);
        LinearLayout layoutCategoryButton = convertView.findViewById(R.id.layoutCategoryButton);

        Task task = list.get(position);

        Date dueDate = task.getDueDate();
        if (dueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(dueDate);
            taskTime.setText(formattedDate);
        } else {
            taskTime.setText("Vô thời hạn");
        }

        Category category = categoryDAO.getCategoryById(task.getCategoryId());
        if (category != null) {

            //Xử lý icon
            textViewCate.setText(category.getName());
            if (category.getIcon() != null && !category.getIcon().isEmpty()) {
                int iconResId = convertView.getContext().getResources().getIdentifier(
                        category.getIcon(), "drawable", convertView.getContext().getPackageName()
                );

                if (iconResId != 0) {
                    imageViewCate.setImageResource(iconResId);
                } else {
                    imageViewCate.setImageResource(R.drawable.ic_block);
                }
            } else {
                imageViewCate.setImageResource(R.drawable.ic_block);
            }
        } else {
            textViewCate.setText("Chưa có");
            imageViewCate.setImageResource(R.drawable.ic_block);
        }

        // Xử lý màu
        Drawable originalDrawable = layoutCategoryButton.getBackground();
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
            backgroundDrawable.setColor(ContextCompat.getColor(convertView.getContext(), R.color.lavender));
        }



        taskCheckBox.setChecked(task.isCompleted());
        taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            long isUpdate = taskDAO.updateTaskStatus(task.getTaskId(), isChecked);
            if (isUpdate > 0) {
                task.setCompleted(isChecked);
            }
        });

        convertView.setOnClickListener(view -> {
            int taskId = task.getTaskId();
            Intent intent = new Intent(view.getContext(), TaskDetailActivity.class);
            intent.putExtra("task_id", taskId);
            view.getContext().startActivity(intent);
        });




        return convertView;
    }


}

