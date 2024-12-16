package com.example.nhom10.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.TaskGroup;

import java.util.List;

public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.ParentViewHolder> {

    private final List<TaskGroup> taskGroupList;
    private final TaskDAO taskDAO;
    private final CategoryDAO categoryDAO;
    private final TaskTagsDAO taskTagsDAO;

    public TaskGroupAdapter(List<TaskGroup> taskGroupList, TaskDAO taskDAO, CategoryDAO categoryDAO, TaskTagsDAO taskTagsDAO) {
        this.taskGroupList = taskGroupList;
        this.taskDAO = taskDAO;
        this.categoryDAO = categoryDAO;
        this.taskTagsDAO = taskTagsDAO;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_group_item, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentViewHolder holder, int position) {
        TaskGroup taskGroup = taskGroupList.get(position);
        holder.bind(taskGroup, taskDAO, categoryDAO, taskTagsDAO);
    }

    @Override
    public int getItemCount() {
        return taskGroupList.size();
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout header;
        private final TextView parentTitle;
        private final TextView itemCount;
        private final ImageView arrowIcon;
        private final RecyclerView childRecyclerView;
        private boolean isExpanded = false;

        public ParentViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            parentTitle = itemView.findViewById(R.id.parentTitle);
            itemCount = itemView.findViewById(R.id.itemCount);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);

            itemView.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                childRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                arrowIcon.setImageResource(isExpanded ? R.drawable.baseline_keyboard_arrow_right_24 : R.drawable.baseline_keyboard_arrow_down_24);
            });
        }

        public void bind(TaskGroup taskGroup, TaskDAO taskDAO, CategoryDAO categoryDAO, TaskTagsDAO taskTagsDAO) {
            String colorString = taskGroup.getColor();
            int color = Color.parseColor(colorString);
            header.setBackgroundColor(color);

            parentTitle.setText(taskGroup.getTitle());
            int childCount = taskGroup.getTaskList().size();
            itemCount.setText(String.valueOf(childCount));

            arrowIcon.setImageResource(isExpanded ? R.drawable.baseline_keyboard_arrow_right_24 : R.drawable.baseline_keyboard_arrow_down_24);
            childRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            childRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            TasksAdapter childAdapter = new TasksAdapter(taskGroup.getTaskList(), taskDAO, categoryDAO, taskTagsDAO);
            childRecyclerView.setAdapter(childAdapter);
        }
    }
}
