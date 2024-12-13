package com.example.nhom10.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nhom10.model.Tag;

import java.util.List;

public class TagAdapter extends BaseAdapter {
    private final Context context;
    private final List<Tag> tags;
    public TagAdapter(Context context, List<Tag> tags) {
        this.context = context;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tags.get(position).getTagId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        Tag tag = tags.get(position);

        textView.setText(tag.getName());
        textView.setBackgroundColor(Color.parseColor(tag.getColor())); // Set màu nền
        textView.setTextColor(Color.WHITE); // Màu chữ trắng

        return view;
    }
}
