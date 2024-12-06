package com.example.nhom10.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tasksDAO = new TasksDAO(getContext());

        List<Tasks> tasks = tasksDAO.getTasks();
        tasksAdapter = new TasksAdapter(tasks);
        recyclerView.setAdapter(tasksAdapter);

        return view;
    }
}