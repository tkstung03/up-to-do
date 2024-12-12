package com.example.nhom10.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.fragments.ChooseCategoryFragment;
import com.example.nhom10.fragments.ChooseTagFragment;
import com.example.nhom10.fragments.DeleteTaskFragment;
import com.example.nhom10.fragments.EditTaskTitleFragment;
import com.example.nhom10.model.Category;
import com.example.nhom10.model.Task;

public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageViewEditTitleTask, buttonClose, iconCate;
    private LinearLayout layoutCategoryButton, layoutTagButton;
    private TextView textViewDelete, textViewTitle, textViewNote, textViewTagCount, textViewNameCate;

    private TaskTagsDAO taskTagsDAO;
    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskTagsDAO = new TaskTagsDAO(this);
        taskDAO = new TaskDAO(this);
        categoryDAO = new CategoryDAO(this);
        int taskId = getIntent().getIntExtra("task_id", -1);

        imageViewEditTitleTask = findViewById(R.id.edit_icon);
        layoutCategoryButton = findViewById(R.id.layoutCategoryButton);
        layoutTagButton = findViewById(R.id.layoutTagButton);
        buttonClose = findViewById(R.id.buttonClose);
        textViewDelete = findViewById(R.id.text_delete);
        textViewTitle = findViewById(R.id.task_title);
        textViewNote = findViewById(R.id.task_subtitle);
        textViewTagCount = findViewById(R.id.textTags);
        iconCate = findViewById(R.id.iconCate);
        textViewNameCate = findViewById(R.id.nameCate);

        loadTaskDetails(taskId);

        getSupportFragmentManager().setFragmentResultListener("UPDATED_TITLE", this, this::handleUpdatedTitle);
        getSupportFragmentManager().setFragmentResultListener("UPDATED_CATEGORY", this, this::handleUpdatedCategory);
        getSupportFragmentManager().setFragmentResultListener("UPDATED_TAG", this, this::handleUpdatedTag);
        getSupportFragmentManager().setFragmentResultListener("DELETED_TASK", this, this::handleDeletedTask);

        imageViewEditTitleTask.setOnClickListener(view -> {
            EditTaskTitleFragment dialogFragment = EditTaskTitleFragment.newInstance(currentTask.getTaskId(), currentTask.getTitle(), currentTask.getNote());
            dialogFragment.show(getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        layoutCategoryButton.setOnClickListener(view -> {
            ChooseCategoryFragment dialogFragment = ChooseCategoryFragment.newInstance(currentTask.getTaskId(), currentTask.getCategoryId());
            dialogFragment.show(getSupportFragmentManager(), "ChooseCategoryFragment");
        });

        layoutTagButton.setOnClickListener(view -> {
            ChooseTagFragment dialogFragment = ChooseTagFragment.newInstance(currentTask.getTaskId());
            dialogFragment.show(getSupportFragmentManager(), "ChooseTagFragment");
        });

        textViewDelete.setOnClickListener(view -> {
            DeleteTaskFragment dialogFragment = DeleteTaskFragment.newInstance(currentTask.getTaskId(), currentTask.getTitle());
            dialogFragment.show(getSupportFragmentManager(), "DeleteTaskFragment");
        });


        buttonClose.setOnClickListener(view -> finish());
    }

    private void handleUpdatedTitle(String s, Bundle bundle) {
    }

    private void handleUpdatedCategory(String s, Bundle bundle) {
    }

    private void handleUpdatedTag(String s, Bundle bundle) {
    }

    private void handleDeletedTask(String s, Bundle bundle) {
    }

    private void loadTaskDetails(int taskId) {
        if (taskId != -1) {
            currentTask = taskDAO.getTaskById(taskId);

            if (currentTask != null) {
                textViewTitle.setText(currentTask.getTitle());
                textViewNote.setText(currentTask.getNote());

                Category category = categoryDAO.getCategoryById(currentTask.getCategoryId());
                if (category != null) {
                    textViewNameCate.setText(category.getName());
                    if (category.getIcon() != null && !category.getIcon().isEmpty()) {
                        int iconResId = this.getResources().getIdentifier(
                                category.getIcon(), "drawable", this.getPackageName()
                        );

                        if (iconResId != 0) {
                            iconCate.setImageResource(iconResId);
                        } else {
                            iconCate.setImageResource(R.drawable.ic_block);
                        }
                    } else {
                        iconCate.setImageResource(R.drawable.ic_block);
                    }
                } else {
                    textViewNameCate.setText("Chưa có");
                    iconCate.setImageResource(R.drawable.ic_block);
                }

                int tagCount = taskTagsDAO.getTagCountByTaskId(currentTask.getTaskId());
                textViewTagCount.setText(String.valueOf(tagCount));
            } else {
                Toast.makeText(this, "Task not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}