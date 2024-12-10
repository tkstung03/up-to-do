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

public class EditTaskDialogFragment extends DialogFragment {

    private TextView textViewTitle;
    private EditText editTaskTitle;
    private TextView textViewNote;
    private EditText editTextNote;
    private Button saveButton, cancelButton;

    private static final String ARG_TASK_TITLE = "task_title";

    public static EditTaskDialogFragment newInstance(String currentTitle) {
        EditTaskDialogFragment fragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_TITLE, currentTitle);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task_dialog, container, false);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        editTaskTitle = view.findViewById(R.id.editTaskTitle);
        textViewNote = view.findViewById(R.id.textViewNote);
        editTextNote = view.findViewById(R.id.editTextNote);
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);

        String currentTitle = getArguments() != null ? getArguments().getString(ARG_TASK_TITLE) : "";
        editTaskTitle.setText(currentTitle);


        textViewTitle.setOnClickListener(v -> {
            textViewTitle.setVisibility(View.GONE);
            editTaskTitle.setVisibility(View.VISIBLE);
            editTaskTitle.setText(textViewTitle.getText());

            textViewNote.setVisibility(View.VISIBLE);
            editTextNote.setVisibility(View.GONE);
            textViewNote.setText(editTextNote.getText());
        });

        textViewNote.setOnClickListener(v -> {
            textViewNote.setVisibility(View.GONE);
            editTextNote.setVisibility(View.VISIBLE);
            editTextNote.setText(textViewNote.getText());

            textViewTitle.setVisibility(View.VISIBLE);
            editTaskTitle.setVisibility(View.GONE);
            textViewTitle.setText(editTaskTitle.getText());
        });

        saveButton.setOnClickListener(v -> {
            String newTitle = editTaskTitle.getText().toString();
            if (getActivity() instanceof DialogListener) {
                ((DialogListener) getActivity()).onTaskTitleUpdated(newTitle);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    public interface DialogListener {
        void onTaskTitleUpdated(String newTitle);
    }
}
