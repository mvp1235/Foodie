package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class CommentInteractor implements CommentContract.Interactor {
    private CommentContract.onPostListener postListener;
    private CommentContract.onLoadListener loadListener;


    public CommentInteractor(CommentContract.onPostListener postListener, CommentContract.onLoadListener loadListener) {
        this.postListener = postListener;
        this.loadListener = loadListener;
    }

    @Override
    public void loadCommentsFromFirebase(BaseActivity activity, String postID) {
        final DatabaseReference databaseReference = activity.getmDatabase();

        databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                loadListener.onLoadSuccess(post.getComments());
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
            final DatabaseReference databaseReference = activity.getmDatabase();

            databaseReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String newCommentID = databaseReference.push().getKey();
                    final User currentUser = dataSnapshot.getValue(User.class);
                    databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Comment comment = new Comment();
                            comment.setcID(newCommentID);
                            comment.setContent(commentText);
                            comment.setUser(currentUser);

                            Post post = dataSnapshot.getValue(Post.class);
                            post.addComment(comment);
                            databaseReference.child("Posts").child(postID).setValue(post);
                            postListener.onPostSuccess(comment);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            postListener.onPostFailure(databaseError.getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    postListener.onPostFailure(databaseError.getMessage());
                }
            });

        }

    }
}
