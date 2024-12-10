package com.example.nhom10.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.fragments.EditTaskDialogFragment;

public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageViewEditTitleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        imageViewEditTitleTask = findViewById(R.id.edit_icon);

        imageViewEditTitleTask.setOnClickListener(view -> {
            EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance("ccsasdaskdkasjdaskjdajs  fffff");
            dialogFragment.show(getSupportFragmentManager(), "EditTaskDialogFragment");
        });
    }
}