package com.example.mvp.foodie.comment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
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

    public void removeComment(String commentID) {
        for (int i=0; i<comments.size(); i++) {
            String currentCommentID = comments.get(i).getcID();
            if (currentCommentID.equals(commentID)) {
                comments.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }

    public void replaceComment(Comment c) {
        for (int i=0; i<comments.size(); i++) {
            String currentCommentID = comments.get(i).getcID();
            if (currentCommentID.equals(c.getcID())) {
                comments.set(i, c);
                this.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getContent());
        holder.commentTime.setText(comment.getCommentDuration());
        setUserInfo(holder, comment.getUserID());
        addMenuButton(comment, holder);
    }

    private void addMenuButton(final Comment comment, final CommentViewHolder holder) {
        String currentUserID = ((BaseActivity)context).getmAuth().getCurrentUser().getUid();
        String commentUserID = comment.getUserID();

        if (currentUserID.equals(commentUserID)) {
            holder.menuBtn.setVisibility(View.VISIBLE);

            holder.menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostPopupMenu(comment, holder);
                }
            });

        } else {
            holder.menuBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void showPostPopupMenu(final Comment comment, CommentViewHolder holder) {
        PopupMenu popupMenu = new PopupMenu(context, holder.menuBtn);
        popupMenu.getMenuInflater().inflate(R.menu.comment_option_menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Delete")) {
                    showDeleteConfirmationDialog(comment);
                } else if (item.getTitle().toString().equals("Edit")){
                    showEditCommentDialog(comment.getContent(), comment.getcID());
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showEditCommentDialog(String commentText, String commentID) {
        DialogFragment dialog = EditCommentDialogFragment.newInstance(commentText, commentID);
        dialog.show(((PostCommentsActivity)context).getSupportFragmentManager(), "EditCommentDialogFragment");
    }

    private void showDeleteConfirmationDialog(final Comment comment) {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this comment?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteComment(comment.getPostID(), comment.getcID());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteComment(final String postID, final String commentID) {
        final DatabaseReference postRef = ((BaseActivity)context).getmDatabase().child("Posts");
        final DatabaseReference commentRef = ((BaseActivity)context).getmDatabase().child("Comments");
        final DatabaseReference notificationRef = ((BaseActivity)context).getmDatabase().child("Notifications");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Delete comment ref ID from post
                Post post = dataSnapshot.getValue(Post.class);
                post.removeCommentID(commentID);
                postRef.child(postID).setValue(post);

                //Delete the comment
                commentRef.child(commentID).removeValue();

                //Update adapter
                removeComment(commentID);

                //Delete all notifications associated with the comment (for post owner and all users who subscribed to the post)
                List<String> subscribedUserIDs = post.getSubscribedUserIDs();



                for (final String userID : subscribedUserIDs) {
                    notificationRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String notificationID = snapshot.getKey();

                                notificationRef.child(userID).child(notificationID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Notification n = dataSnapshot.getValue(Notification.class);
                                        if (n != null && n.getCommentID().equals(commentID)) {
                                            notificationRef.child(userID).child(notificationID).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
