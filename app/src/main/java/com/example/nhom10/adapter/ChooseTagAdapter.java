package com.example.nhom10.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10.R;
import com.example.nhom10.model.Tag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseTagAdapter extends RecyclerView.Adapter<ChooseTagAdapter.TagViewHolder> {

    private List<Tag> tagList;
    private OnTagClickListener onTagClickListener;
    private Set<Tag> selectedTags = new HashSet<>();

    public ChooseTagAdapter(List<Tag> tagList, OnTagClickListener listener) {
        this.tagList = tagList;
        this.onTagClickListener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);

        //Làm nổi nhãn được chọn
        if (selectedTags.contains(tag)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        if (tag.getColor() != null && !tag.getColor().isEmpty()) {
            try {
                int color = Color.parseColor(tag.getColor());
                holder.imageViewIcon.getBackground().mutate().setTint(color);
            } catch (Exception e) {
            }
        }
        holder.textViewName.setText(tag.getName());

        holder.itemView.setOnClickListener(v -> onTagClickListener.onTagClick(tag));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void setSelectedTags(Set<Tag> tag) {
        this.selectedTags.clear();
        this.selectedTags.addAll(tag);
        notifyDataSetChanged();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewName;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.iconTag);
            textViewName = itemView.findViewById(R.id.textTag);
        }
    }

    public interface OnTagClickListener {
        void onTagClick(Tag tag);
    }

}