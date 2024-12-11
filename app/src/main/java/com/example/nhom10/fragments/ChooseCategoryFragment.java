package com.example.nhom10.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.adapter.ChooseCategoryAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.model.Category;
import com.example.nhom10.model.Task;

import java.util.List;

public class ChooseCategoryFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "taskId";
    private int taskId;
    private CategoryDAO categoryDAO;
    private TaskDAO taskDAO;
    private Category selectedCategory;
    private ChooseCategoryAdapter adapter;

    public ChooseCategoryFragment() {
    }

    public static ChooseCategoryFragment newInstance(int taskId) {
        ChooseCategoryFragment fragment = new ChooseCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = (int) (displayMetrics.widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt(ARG_TASK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_category, container, false);

        categoryDAO = new CategoryDAO(getContext());
        taskDAO = new TaskDAO(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        List<Category> categories = categoryDAO.getAllCategories();
        adapter = new ChooseCategoryAdapter(categories, this::onCategorySelected);
        recyclerView.setAdapter(adapter);

        Task currentTask = taskDAO.getTaskById(taskId);
        if (currentTask != null) {
            int currentCategoryId = currentTask.getCategoryId();
            selectedCategory = categoryDAO.getCategoryById(currentCategoryId);
            adapter.setSelectedCategory(selectedCategory);
        }

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v -> dismiss());

        Button saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> onSaveClicked());

        return view;
    }

    private void onSaveClicked() {
        if (selectedCategory != null) {
            taskDAO.updateTaskCategory(taskId, selectedCategory.getCategoryId());
            dismiss();
        }
    }

    private void onCategorySelected(Category category) {
        selectedCategory = category;
        adapter.setSelectedCategory(category);
    }

}