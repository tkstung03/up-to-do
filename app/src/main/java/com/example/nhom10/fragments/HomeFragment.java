package com.example.nhom10.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.adapter.TasksAdapter;
import com.example.nhom10.dao.TaskDAO;
import com.example.nhom10.model.Task;

import java.util.List;

public class HomeFragment extends Fragment {

    private TaskDAO taskDAO;
    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private Spinner filterSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskDAO = new TaskDAO(getContext());
        List<Task> tasks = taskDAO.getTasks();

        tasksAdapter = new TasksAdapter(tasks, taskDAO);
        recyclerView.setAdapter(tasksAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        tasksAdapter.updateTasks(filteredTasks);
    }
}