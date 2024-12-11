package com.example.nhom10.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.fragments.ChooseCategoryFragment;
import com.example.nhom10.fragments.DeleteTaskFragment;
import com.example.nhom10.fragments.EditTaskDialogFragment;
import com.example.nhom10.model.Task;

public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageViewEditTitleTask;
    private LinearLayout layoutCategoryButton;
    private ImageButton buttonClose;
    private TextView textViewDelete, textViewTitle, textViewNote;

    private TaskDAO taskDAO;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskDAO = new TaskDAO(this);
        int taskId = getIntent().getIntExtra("task_id", -1);

        imageViewEditTitleTask = findViewById(R.id.edit_icon);
        layoutCategoryButton = findViewById(R.id.layoutCategoryButton);
        buttonClose = findViewById(R.id.buttonClose);
        textViewDelete = findViewById(R.id.text_delete);
        textViewTitle = findViewById(R.id.task_title);
        textViewNote = findViewById(R.id.task_subtitle);

        loadTaskDetails(taskId);

        textViewTitle.setText(currentTask.getTitle());
        textViewNote.setText(currentTask.getNote());

        imageViewEditTitleTask.setOnClickListener(view -> {
            EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance(currentTask.getTitle(), currentTask.getNote());
            dialogFragment.show(getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        layoutCategoryButton.setOnClickListener(view -> {
            ChooseCategoryFragment dialogFragment = ChooseCategoryFragment.newInstance(currentTask.getTaskId());
            dialogFragment.show(getSupportFragmentManager(), "ChooseCategoryFragment");
        });

        textViewDelete.setOnClickListener(view -> {
            DeleteTaskFragment dialogFragment = DeleteTaskFragment.newInstance(currentTask.getTaskId(), currentTask.getTitle());
            dialogFragment.show(getSupportFragmentManager(), "DeleteTaskFragment");
        });


        buttonClose.setOnClickListener(view -> finish());
    }

    private void loadTaskDetails(int taskId) {
        if (taskId != -1) {
            currentTask = taskDAO.getTaskById(taskId);

            if (currentTask != null) {

            } else {
                Toast.makeText(this, "Task not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}