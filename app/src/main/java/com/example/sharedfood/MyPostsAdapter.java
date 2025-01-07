package com.example.sharedfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.PostViewHolder> {
    private List<Post> posts;
    private PostClickListener listener;

    public interface PostClickListener {
        void onEditClick(Post post);
        void onDeleteClick(Post post);
    }

    public MyPostsAdapter(List<Post> posts, PostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView descriptionText;
        TextView locationText;
        ChipGroup filtersChipGroup;
        ImageButton editButton;
        ImageButton deletePostButton;

        public PostViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.postImage);
            descriptionText = view.findViewById(R.id.postDescription);
            locationText = view.findViewById(R.id.postLocation);
            filtersChipGroup = view.findViewById(R.id.filtersChipGroup);
            editButton = view.findViewById(R.id.editButton);
            deletePostButton = view.findViewById(R.id.deletePostButton);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // תיאור
        holder.descriptionText.setText(post.getDescription() != null ? post.getDescription() : "תיאור לא זמין");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // טיפול בתמונה
        if (post.getImageBitmap() != null) {
            holder.imageView.setImageBitmap(post.getImageBitmap());
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // מיקום
        holder.locationText.setText(post.getCity() != null ? post.getCity() : "מיקום לא זמין");

        // סינונים
        holder.filtersChipGroup.removeAllViews();
        if (post.getFilters() != null && !post.getFilters().isEmpty()) {
            for (String filter : post.getFilters()) {
                Chip chip = new Chip(holder.filtersChipGroup.getContext());
                chip.setText(filter);
                chip.setCheckable(false);
                holder.filtersChipGroup.addView(chip);
            }
        } else {
            // אם אין סינונים, הצגת הודעה או פשוט הימנעו מהוספת ChipGroup ריק
            Chip chip = new Chip(holder.filtersChipGroup.getContext());
            chip.setText("אין סינונים");
            chip.setCheckable(false);
            holder.filtersChipGroup.addView(chip);
        }

        // כפתורי עריכה ומחיקה
        holder.editButton.setOnClickListener(v -> listener.onEditClick(post));
        holder.deletePostButton.setOnClickListener(v -> listener.onDeleteClick(post));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
