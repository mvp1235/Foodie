package com.example.mvp.foodie.friend;

import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View view;
    private DatabaseReference notificationRef;
    private DatabaseReference userRef;

    public FriendPresenter(FriendContract.View view) {
        this.view = view;
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void sendFriendRequest(final String fromUserID, final String toUserID) {
        final String newNotificationID = notificationRef.push().getKey();

        final Notification notification = new Notification();
        notification.setContent("sent you a friend request.");
        notification.setnID(newNotificationID);
        notification.setType("friend request");
        notification.setToUserID(toUserID);
        notification.setFromUserID(fromUserID);

        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                fromUser.addSentFriendRequestID(toUserID);

                notification.setUserName(fromUser.getFullName());
                notification.setPhotoURL(fromUser.getProfileURL());

                userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User toUser = dataSnapshot.getValue(User.class);
                        toUser.addPendingFriendRequestID(fromUserID);

                        userRef.child(fromUserID).setValue(fromUser);
                        userRef.child(toUserID).setValue(toUser);
                        notificationRef.child(newNotificationID).setValue(notification);
                        view.onSendRequestSuccess();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.onSendRequestFailure(databaseError.getMessage());
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onSendRequestFailure(databaseError.getMessage());
            }
        });

    }

    @Override
    public void cancelFriendRequest(final String fromUserID, final String toUserID) {
        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                fromUser.removeSentFriendRequestID(toUserID);

                userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User toUser = dataSnapshot.getValue(User.class);
                        toUser.removePendingFriendRequestID(fromUserID);

                        notificationRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);
                                    if (n != null && n.getType().equals("friend request")
                                            && n.getFromUserID().equals(fromUserID) && n.getToUserID().equals(toUserID)) {
                                        notificationRef.child(toUserID).child(ds.getKey()).removeValue();
                                        break;
                                    }
                                }

                                userRef.child(fromUserID).setValue(fromUser);
                                userRef.child(toUserID).setValue(toUser);
                                view.onCancelRequestSuccess();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                view.onCancelRequestFailure(databaseError.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.onCancelRequestFailure(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onCancelRequestFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void acceptFriendRequest(String fromUserID, String toUserID) {
        final String newNotificationID = notificationRef.push().getKey();
        final Notification notification = new Notification();
        notification.setnID(newNotificationID);
        notification.setFromUserID(fromUserID);
        notification.setToUserID(toUserID);
        notification.setType("friend request");

        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User fromUser = dataSnapshot.getValue(User.class);
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void declineFriendRequest(String fromUserID, String toUserID) {

    }
}
