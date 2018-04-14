package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CommentInteractor implements CommentContract.Interactor {
    private CommentContract.onPostListener postListener;
    private CommentContract.onLoadListener loadListener;
    private CommentContract.onEditListener editListener;


    public CommentInteractor(CommentContract.onPostListener postListener, CommentContract.onLoadListener loadListener, CommentContract.onEditListener editListener) {
        this.postListener = postListener;
        this.loadListener = loadListener;
        this.editListener = editListener;
    }

    @Override
    public void editCommentOnFirebase(BaseActivity activity, final String commentID, final String newContent) {
        final DatabaseReference commentRef = activity.getmDatabase().child("Comments");

        commentRef.child(commentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comment.setContent(newContent + " \n(edited)");
                commentRef.child(commentID).setValue(comment);
                editListener.onEditSuccess(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                editListener.onEditFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void loadCommentsFromFirebase(BaseActivity activity, String postID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference commentRef = activity.getmDatabase().child("Comments");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                List<String> commentIDs = post.getCommentIDs();

                for (String cID: commentIDs) {
                    commentRef.child(cID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Comment c = dataSnapshot.getValue(Comment.class);
                            loadListener.onLoadSuccess(c);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            loadListener.onLoadFailure(databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadListener.onLoadFailure(databaseError.getMessage());
            }
        });

    }


    @Override
    public void postCommentToFirebase(BaseActivity activity, final String postID, final String commentText, final String userID) {
        if (activity == null || commentText == null || userID == null) {
            postListener.onPostFailure("Failed to post the comment. Please try again later.");
        } else {
            final DatabaseReference userRef = activity.getmDatabase().child("Users");
            final DatabaseReference commentRef = activity.getmDatabase().child("Comments");
            final DatabaseReference postRef = activity.getmDatabase().child("Posts");

            postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String newCommentID = commentRef.push().getKey();

                    //Construct the new comment object
                    Comment comment = new Comment();
                    comment.setcID(newCommentID);
                    comment.setContent(commentText);
                    comment.setUserID(userID);
                    comment.setPostID(postID);

                    //Update post database
                    Post post = dataSnapshot.getValue(Post.class);
                    post.addCommentID(comment.getcID());
                    postRef.child(postID).setValue(post);

                    //add comment to Comments database
                    commentRef.child(comment.getcID()).setValue(comment);

                    //notify presenter
                    postListener.onPostSuccess(comment);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    postListener.onPostFailure(databaseError.getMessage());
                }
            });

        }

    }
}
