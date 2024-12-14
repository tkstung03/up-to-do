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

import java.util.Calendar;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageViewEditTitleTask, buttonClose, iconCate;
    private LinearLayout layoutCategoryButton, layoutTagButton;
    private TextView textViewDelete, textViewTitle, textViewNote, textViewTagCount, textViewNameCate, textViewTime;

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
        textViewTime = findViewById(R.id.text_task_time_value);

        loadTaskDetails(taskId);

        getSupportFragmentManager().setFragmentResultListener("UPDATED_TITLE", this, this::handleUpdatedTitle);
        getSupportFragmentManager().setFragmentResultListener("UPDATED_CATEGORY", this, this::handleUpdatedCategory);
        getSupportFragmentManager().setFragmentResultListener("UPDATED_TAG", this, this::handleUpdatedTag);
        getSupportFragmentManager().setFragmentResultListener("DELETED_TASK", this, this::handleDeletedTask);

        imageViewEditTitleTask.setOnClickListener(view -> {
            EditTaskTitleFragment dialogFragment = EditTaskTitleFragment.newInstance(currentTask.getTitle(), currentTask.getNote());
            dialogFragment.show(getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        layoutCategoryButton.setOnClickListener(view -> {
            ChooseCategoryFragment dialogFragment = ChooseCategoryFragment.newInstance(currentTask.getCategoryId());
            dialogFragment.show(getSupportFragmentManager(), "ChooseCategoryFragment");
        });

        layoutTagButton.setOnClickListener(view -> {
            ChooseTagFragment dialogFragment = ChooseTagFragment.newInstance(currentTask.getTaskId());
            dialogFragment.show(getSupportFragmentManager(), "ChooseTagFragment");
        });

        textViewDelete.setOnClickListener(view -> {
            DeleteTaskFragment dialogFragment = DeleteTaskFragment.newInstance(currentTask.getTitle());
            dialogFragment.show(getSupportFragmentManager(), "DeleteTaskFragment");
        });

        buttonClose.setOnClickListener(view -> finish());
    }

    private void handleUpdatedTitle(String s, Bundle bundle) {
        String newTitle = bundle.getString("updatedTitle");
        String newNote = bundle.getString("updatedNote");

        boolean isUpdate = taskDAO.updateTaskTitleAndNote(currentTask.getTaskId(), newTitle, newNote);
        if (isUpdate) {
            currentTask.setTitle(newTitle);
            currentTask.setNote(newNote);

            updateViewTitle();
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdatedCategory(String s, Bundle bundle) {
        int newCategoryId = bundle.getInt("updatedCategoryId");

        boolean isUpdate = taskDAO.updateTaskCategory(currentTask.getTaskId(), newCategoryId);
        if (isUpdate) {
            currentTask.setCategoryId(newCategoryId);

            updateViewCategory();

            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdatedTag(String s, Bundle bundle) {
        int[] newTagIds = bundle.getIntArray("updatedTagIds");

        taskTagsDAO.deleteByTaskId(currentTask.getTaskId());
        if (newTagIds != null && newTagIds.length > 0) {
            taskTagsDAO.create(currentTask.getTaskId(), newTagIds);
        }

        updateViewTags();
    }

    private void handleDeletedTask(String s, Bundle bundle) {
        boolean isDeleted = taskDAO.deleteTask(currentTask.getTaskId());

        if (isDeleted) {
            Toast.makeText(this, "Đã xóa nhiệm vụ", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi xóa nhiệm vụ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTaskDetails(int taskId) {
        if (taskId != -1) {
            currentTask = taskDAO.getTaskById(taskId);
            if (currentTask != null) {
                updateViewTitle();
                updateViewTime();
                updateViewCategory();
                updateViewTags();
            } else {
                Toast.makeText(this, "Task not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void updateViewTitle() {
        textViewTitle.setText(currentTask.getTitle());
        textViewNote.setText(currentTask.getNote());
    }

    private void updateViewTime() {
        Date date = currentTask.getDueDate();
        String formattedTime;
        if (date == null) {
            formattedTime = "Chưa có";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            formattedTime = String.format("%02d:%02d, %02d/%02d/%d", hourOfDay, minute, dayOfMonth, month + 1, year);
        }
        textViewTime.setText(formattedTime);
    }

    private void updateViewTags() {
        int tagCount = taskTagsDAO.getTagCountByTaskId(currentTask.getTaskId());
        textViewTagCount.setText(String.valueOf(tagCount));
    }

    private void updateViewCategory() {
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
    }
}