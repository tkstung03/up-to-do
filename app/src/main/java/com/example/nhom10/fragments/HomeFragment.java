package com.example.nhom10.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nhom10.R;
import com.example.nhom10.dao.TasksDAO;
import com.example.nhom10.model.Tasks;

import java.util.List;


public class HomeFragment extends Fragment {

    private TasksDAO tasksDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tasksDAO = new TasksDAO(getContext());

        List<Tasks> tasks = tasksDAO.getTasks();
        System.out.println(tasks.isEmpty());

        return view;
    }
}