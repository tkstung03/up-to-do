package com.example.nhom10.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.adapter.TasksAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Task;

import java.util.List;

public class HomeFragment extends Fragment {

    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private TasksAdapter tasksAdapter1;
    private TasksAdapter tasksAdapter2;
    private TaskTagsDAO taskTagsDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(getContext());
        categoryDAO = new CategoryDAO(getContext());
        taskTagsDAO = new TaskTagsDAO(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Task> tasks = taskDAO.getTasks();
        tasksAdapter1.updateTasks(tasks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar = view.findViewById(R.id.searchBar);

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView1);
        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        Spinner spnTimeFilter = view.findViewById(R.id.spnTimeFilter);
        Spinner spnStatusFilter = view.findViewById(R.id.spnStatusFilter);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Task> tasks1 = taskDAO.getTasks();
        List<Task> tasks2 = taskDAO.getTasks();

        tasksAdapter1 = new TasksAdapter(tasks1, taskDAO, categoryDAO, taskTagsDAO);
        recyclerView1.setAdapter(tasksAdapter1);

        tasksAdapter2 = new TasksAdapter(tasks2, taskDAO, categoryDAO, taskTagsDAO);
        recyclerView1.setAdapter(tasksAdapter2);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterTasksBySearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        spnTimeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                applyFilter(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnStatusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                applyFilter(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void filterTasksBySearch(String keyword) {
        //  List<Task> filteredTasks = taskDAO.getTasksBySearch(keyword);
        //  tasksAdapter.updateTasks(filteredTasks);
    }

    private void applyFilter(String filter) {
        List<Task> filteredTasks;

        switch (filter) {
            case "Hôm nay":
                filteredTasks = taskDAO.getTasksForToday();
                break;
            case "Tuần này":
                filteredTasks = taskDAO.getTasksForThisWeek();
                break;
            case "Tháng này":
                filteredTasks = taskDAO.getTasksForThisMonth();
                break;
            default:
                filteredTasks = taskDAO.getTasks();
                break;
        }

        tasksAdapter1.updateTasks(filteredTasks);
    }
}