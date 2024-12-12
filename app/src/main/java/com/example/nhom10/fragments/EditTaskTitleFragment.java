package com.example.nhom10.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nhom10.R;
import com.example.nhom10.dao.TaskDAO;

public class EditTaskTitleFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_TASK_TITLE = "task_title";
    private static final String ARG_TASK_NOTE = "task_note";

    private int taskId;
    private String taskTitle;
    private String taskNote;

    private TextView textViewTitle;
    private EditText editTaskTitle;
    private TextView textViewNote;
    private EditText editTaskNote;
    private Button saveButton, cancelButton;

    private TaskDAO taskDAO;

    public static EditTaskTitleFragment newInstance(int currentTaskId, String currentTitle, String currentNote) {
        EditTaskTitleFragment fragment = new EditTaskTitleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASK_ID, currentTaskId);
        args.putString(ARG_TASK_TITLE, currentTitle);
        args.putString(ARG_TASK_NOTE, currentNote);
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
            taskNote = getArguments().getString(ARG_TASK_NOTE);
        }

        taskDAO = new TaskDAO(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task_dialog, container, false);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        editTaskTitle = view.findViewById(R.id.editTaskTitle);
        textViewNote = view.findViewById(R.id.textViewNote);
        editTaskNote = view.findViewById(R.id.editTaskNote);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        editTaskTitle.setText(taskTitle);
        editTaskNote.setText(taskNote);

        textViewTitle.setOnClickListener(v -> {
            textViewTitle.setVisibility(View.GONE);
            editTaskTitle.setVisibility(View.VISIBLE);
            editTaskTitle.setText(textViewTitle.getText());

            textViewNote.setVisibility(View.VISIBLE);
            editTaskNote.setVisibility(View.GONE);
            textViewNote.setText(editTaskNote.getText());
        });

        textViewNote.setOnClickListener(v -> {
            textViewNote.setVisibility(View.GONE);
            editTaskNote.setVisibility(View.VISIBLE);
            editTaskNote.setText(textViewNote.getText());

            textViewTitle.setVisibility(View.VISIBLE);
            editTaskTitle.setVisibility(View.GONE);
            textViewTitle.setText(editTaskTitle.getText());
        });

        cancelButton.setOnClickListener(v -> dismiss());

        saveButton.setOnClickListener(v -> {
            String newTitle = editTaskTitle.getText().toString();
            String newNote = editTaskNote.getText().toString();

            taskDAO.updateTaskTitleAndNote(taskId, newTitle, newNote);

            Bundle result = new Bundle();
            result.putBoolean("UPDATED_TITLE", true);
            getParentFragmentManager().setFragmentResult("UPDATED_TITLE", result);

            dismiss();
        });

        return view;
    }

}
