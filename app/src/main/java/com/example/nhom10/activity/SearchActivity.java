package com.example.nhom10.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.adapter.TasksAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView recyclerView;
    private List<Task> filteredTasks = new ArrayList<>();
    private TasksAdapter tasksAdapter;
    private TaskDAO taskDAO;

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView backButton = findViewById(R.id.imageViewBack);
        searchBar = findViewById(R.id.editTextSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskDAO = new TaskDAO(this);
        CategoryDAO categoryDAO = new CategoryDAO(this);
        TaskTagsDAO taskTagsDAO = new TaskTagsDAO(this);

        tasksAdapter = new TasksAdapter(filteredTasks, taskDAO, categoryDAO, taskTagsDAO, null);
        recyclerView.setAdapter(tasksAdapter);

        backButton.setOnClickListener(v -> finish());
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hủy tìm kiếm trước đó
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Lập lịch tìm kiếm sau 300ms
                searchRunnable = () -> filterTasks();
                searchHandler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateTaskList(List<Task> tasks) {
        filteredTasks.clear();
        filteredTasks.addAll(tasks);
        tasksAdapter.notifyDataSetChanged();
    }

    private void filterTasks() {
        String keyword = searchBar.getText().toString().toLowerCase();
        List<Task> allTasks = taskDAO.findAll();

        updateTaskList(allTasks);
    }

}