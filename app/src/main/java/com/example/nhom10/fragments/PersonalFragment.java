package com.example.nhom10.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nhom10.R;
import com.example.nhom10.activity.MainActivity;

public class PersonalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().hide();
        }
        return view;
    }
}