package com.example.sharedfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// מחלקת האדפטר להצגת רשימת הפוסטים ומתן אפשרות למחוק פוסט בודד
public class AdminPostsAdapter extends RecyclerView.Adapter<AdminPostsAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final OnPostDeleteListener onPostDeleteListener;

    // ממשק למחיקת פוסט (קריאה חיצונית לפונקציה מהאקטיביטי)
    public interface OnPostDeleteListener {
        void onDeletePost(String postId);
    }

    // בנאי לקבלת רשימת פוסטים ומאזין למחיקה
    public AdminPostsAdapter(List<Post> postList, OnPostDeleteListener listener) {
        this.postList = postList;
        this.onPostDeleteListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postDescription.setText(post.getDescription()); // תוקן לשדה description
        holder.deletePostButton.setOnClickListener(v -> {
            if (onPostDeleteListener != null) {
                onPostDeleteListener.onDeletePost(post.getId()); // תוקן לשדה id של הפוסט
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // קלאס פנימי לניהול התצוגה של כל פריט ברשימה
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postDescription;

        Button deletePostButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postDescription = itemView.findViewById(R.id.postDescription);

            ImageButton deletePostButton = itemView.findViewById(R.id.deletePostButton);
        }
    }
}
