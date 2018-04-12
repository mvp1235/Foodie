package com.example.mvp.foodie.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentRecyclerAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.userName.setText(comment.getUser().getFullName());
        Picasso.get().load(comment.getUser().getProfileURL()).into(holder.profilePhoto);
        holder.commentText.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
