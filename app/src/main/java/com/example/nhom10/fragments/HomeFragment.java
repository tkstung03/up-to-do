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
import com.example.nhom10.dao.TasksDAO;
import com.example.nhom10.model.Tasks;

import java.util.List;


public class HomeFragment extends Fragment {

    private TasksDAO tasksDAO;
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

        tasksDAO = new TasksDAO(getContext());
        List<Tasks> tasks = tasksDAO.getTasks();

        tasksAdapter = new TasksAdapter(tasks, tasksDAO);
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
        List<Tasks> filteredTasks;

        switch (filter) {
            case "Hôm nay":
                filteredTasks = tasksDAO.getTasksForToday();
                break;
            case "Tuần này":
                filteredTasks = tasksDAO.getTasksForThisWeek();
                break;
            case "Tháng này":
                filteredTasks = tasksDAO.getTasksForThisMonth();
                break;
            default:
                filteredTasks = tasksDAO.getTasks();
                break;
        }

        tasksAdapter.updateTasks(filteredTasks);
    }
}