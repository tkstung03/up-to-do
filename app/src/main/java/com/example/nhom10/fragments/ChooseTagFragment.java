package com.example.nhom10.fragments;

import android.content.Context;
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
import com.example.nhom10.adapter.ChooseTagAdapter;
import com.example.nhom10.dao.TagDAO;
import com.example.nhom10.dao.TaskTagsDAO;
import com.example.nhom10.model.Tag;

import java.util.List;

public class ChooseTagFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "task_id";
    private int taskId;
    private TagDAO tagDAO;
    private TaskTagsDAO taskTagsDAO;
    private List<Tag> selectedTags;
    private ChooseTagAdapter adapter;

    public ChooseTagFragment() {
    }

    public static ChooseTagFragment newInstance(int taskId) {
        ChooseTagFragment fragment = new ChooseTagFragment();
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

        Context context = requireContext();
        tagDAO = new TagDAO(context);
        taskTagsDAO = new TaskTagsDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_tag, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerTagList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        List<Tag> tags = tagDAO.getAllTags();
        adapter = new ChooseTagAdapter(tags, this::onTagSelected);
        recyclerView.setAdapter(adapter);

        selectedTags = taskTagsDAO.getAllByTaskId(taskId);
        adapter.setSelectedTags(selectedTags);

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v -> dismiss());

        Button saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> onSaveClicked());

        return view;
    }

    private void onSaveClicked() {
        Bundle result = new Bundle();
        result.putIntArray("updatedTagIds", selectedTags.stream().mapToInt(Tag::getTagId).toArray());
        getParentFragmentManager().setFragmentResult("UPDATED_TAG", result);

        dismiss();
    }

    private void onTagSelected(Tag tag) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag);
        } else {
            selectedTags.add(tag);
        }
        adapter.setSelectedTags(selectedTags);
    }
}