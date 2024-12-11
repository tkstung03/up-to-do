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
import com.example.nhom10.adapter.CategoryAdapter;
import com.example.nhom10.dao.CategoryDAO;
import com.example.nhom10.model.Category;

import java.util.List;

public class ChooseCategoryFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CategoryDAO categoryDAO;

    public ChooseCategoryFragment() {
    }

    public static ChooseCategoryFragment newInstance(String param1, String param2) {
        ChooseCategoryFragment fragment = new ChooseCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_category, container, false);

        categoryDAO = new CategoryDAO(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCategoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        List<Category> categories = categoryDAO.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(categories, this::onCategorySelected);
        recyclerView.setAdapter(adapter);

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v -> dismiss());

        Button editButton = view.findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(v -> onEditClicked());

        return view;
    }

    private void onEditClicked() {
    }

    private void onCategorySelected(Category category) {
    }

}