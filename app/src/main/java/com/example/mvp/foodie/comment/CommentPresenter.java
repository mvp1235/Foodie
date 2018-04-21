package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentPresenter implements CommentContract.Presenter {
    private CommentContract.View view;


    public CommentPresenter(CommentContract.View view) {
        this.view = view;
    }

    @Override
    public void editComment(BaseActivity activity, final String commentID, final String newContent) {
        final DatabaseReference commentRef = activity.getmDatabase().child("Comments");

        commentRef.child(commentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comment.setContent(newContent + " \n(edited)");
                commentRef.child(commentID).setValue(comment);
                view.onCommentEditSuccess(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onCommentEditFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void loadComments(BaseActivity activity, String postID) {
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

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            view.onCommentsLoadFailure(databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onCommentsLoadFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void postComment(final BaseActivity activity, final String postID, final String commentText, final String commenterID) {

        if (activity == null || commentText == null || commenterID == null) {
            view.onCommentFailure("Failed to post the comment. Please try again later.");
        } else {
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
                    comment.setUserID(commenterID);
                    comment.setPostID(postID);

                    //Update post database
                    Post post = dataSnapshot.getValue(Post.class);
                    post.addCommentID(comment.getcID());
                    post.addSubscriberID(commenterID);
                    postRef.child(postID).setValue(post);

                    //add comment to Comments database
                    commentRef.child(comment.getcID()).setValue(comment);

                    //Add comment notification
                    addCommentNotification(activity, postID, post.getUserID(), commenterID, post.getSubscribedUserIDs());

                    view.onCommentSuccess(comment);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.onCommentFailure(databaseError.getMessage());
                }
            });

        }
    }

    private void addCommentNotification(BaseActivity activity, final String postID, final String postOwnerID, final String commenterID, final ArrayList<String> subscribedIDs) {
        final DatabaseReference notificationRef = activity.getmDatabase().child("Notifications");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");


        userRef.child(commenterID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User commentUser = dataSnapshot.getValue(User.class);

                for (final String subID : subscribedIDs) {
                    //Only post notification for subscribers who is not the actual commenter
                    if (!subID.equals(commenterID)) {

                        final String newNotificationID = notificationRef.push().getKey();
                        final Notification notification = new Notification();
                        notification.setnID(newNotificationID);
                        notification.setFromUserID(commenterID);
                        notification.setPostID(postID);
                        notification.setType("comment");
                        notification.setUserName(commentUser.getFullName());
                        notification.setPhotoURL(commentUser.getProfileURL());

                        userRef.child(subID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final User subscribedUser = dataSnapshot.getValue(User.class);
                                notification.setToUserID(subscribedUser.getuID());

                                if (subscribedUser.getuID().equals(postOwnerID))
                                    notification.setContent("commented on your post.");
                                else
                                    notification.setContent("commented on a post you subscribed to.");

                                notificationRef.child(subscribedUser.getuID()).child(newNotificationID).setValue(notification);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
