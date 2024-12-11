package com.example.nhom10.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nhom10.R;
import com.example.nhom10.activity.MainActivity;
import com.example.nhom10.dao.TaskDAO;

public class DeleteTaskFragment extends DialogFragment {

    private static final String ARG_TASK_TITLE = "task_title";
    private static final String ARG_TASK_ID = "task_id";
    private String taskTitle;
    private int taskId;

    public DeleteTaskFragment() {
    }

    public static DeleteTaskFragment newInstance(int taskId, String title) {
        DeleteTaskFragment fragment = new DeleteTaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASK_ID, taskId);
        args.putString(ARG_TASK_TITLE, title);
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
            taskTitle = getArguments().getString(ARG_TASK_TITLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_task, container, false);

        TextView textViewTitle = view.findViewById(R.id.task_title);
        textViewTitle.setText(String.format("Tiêu đề: %s", taskTitle));

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> dismiss());

        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> deleteTask());

        return view;
    }

    private void deleteTask() {
        TaskDAO taskDAO = new TaskDAO(getContext());
        boolean isDeleted = taskDAO.deleteTask(taskId);
        if (isDeleted) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dismiss();
        } else {
            Toast.makeText(getContext(), "Xóa task không thành công", Toast.LENGTH_LONG).show();
        }
    }
}