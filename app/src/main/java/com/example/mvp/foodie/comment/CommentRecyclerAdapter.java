package com.example.mvp.foodie.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentRecyclerAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void addComment(Comment c) {
        comments.add(c);
        this.notifyDataSetChanged();
    }

    public void removeComment(Comment c) {
        if (comments.contains(c)) {
            comments.add(c);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    void setComments(List<Comment> comments) {
        this.comments = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getContent());
        setUserInfo(holder, comment.getUserID());
    }

    private void setUserInfo(final CommentViewHolder holder, final String userID) {
        DatabaseReference userRef = ((BaseActivity)context).getmDatabase().child("Users");
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                holder.userName.setText(u.getFullName());
                Picasso.get().load(u.getProfileURL()).into(holder.profilePhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
