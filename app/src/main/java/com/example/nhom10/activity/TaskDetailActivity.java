package com.example.nhom10.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom10.R;
import com.example.nhom10.fragments.ChooseCategoryFragment;
import com.example.nhom10.fragments.DeleteTaskFragment;
import com.example.nhom10.fragments.EditTaskDialogFragment;

public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageViewEditTitleTask;

    private LinearLayout layoutCategoryButton;

    private ImageButton buttonClose;

    private TextView textViewDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        imageViewEditTitleTask = findViewById(R.id.edit_icon);
        layoutCategoryButton = findViewById(R.id.layoutCategoryButton);
        buttonClose = findViewById(R.id.buttonClose);
        textViewDelete = findViewById(R.id.text_delete);

        imageViewEditTitleTask.setOnClickListener(view -> {
            EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance("ccsasdaskdkasjdaskjdajs  fffff");
            dialogFragment.show(getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        layoutCategoryButton.setOnClickListener(view -> {
            ChooseCategoryFragment dialogFragment = ChooseCategoryFragment.newInstance("ccsasdaskdkasjd", "");
            dialogFragment.show(getSupportFragmentManager(), "ChooseCategoryFragment");
        });

        textViewDelete.setOnClickListener(view -> {
            DeleteTaskFragment dialogFragment = DeleteTaskFragment.newInstance("", "");
            dialogFragment.show(getSupportFragmentManager(), "DeleteTaskFragment");
        });


        buttonClose.setOnClickListener(view -> finish());
    }
}