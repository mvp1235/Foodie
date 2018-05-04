package com.example.mvp.foodie.friend;


import android.util.Log;

import com.example.mvp.foodie.models.FriendRequest;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View view;
    private FriendContract.Adapter adapter;
    private DatabaseReference notificationRef;
    private DatabaseReference userRef;

    public FriendPresenter(FriendContract.View view) {
        this.view = view;
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public FriendPresenter(FriendContract.View view, FriendContract.Adapter adapter) {
        this.view = view;
        this.adapter = adapter;
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    /**
     *
     * @param sentUserID user who is sending the request
     * @param receivedUserID user who is receiving the request
     */
    @Override
    public void sendFriendRequest(final String sentUserID, final String receivedUserID) {
        final String newNotificationID = notificationRef.push().getKey();

        final Notification notification = new Notification();
        notification.setContent("sent you a friend request.");
        notification.setnID(newNotificationID);
        notification.setType("friend request");
        notification.setToUserID(receivedUserID);
        notification.setFromUserID(sentUserID);

        userRef.child(sentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User sentUser = dataSnapshot.getValue(User.class);
                sentUser.addSentFriendRequestID(receivedUserID);

                notification.setUserName(sentUser.getFullName());
                notification.setPhotoURL(sentUser.getProfileURL());

                userRef.child(receivedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User receivedUser = dataSnapshot.getValue(User.class);
                        receivedUser.addPendingFriendRequestID(sentUserID);

                        userRef.child(sentUserID).setValue(sentUser);
                        userRef.child(receivedUserID).setValue(receivedUser);
                        notificationRef.child(receivedUserID).child(newNotificationID).setValue(notification);
                        view.onSendRequestSuccess(sentUser, receivedUser);
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

    /**
     *
     * @param sentUserID user who sent the request, now cancelling
     * @param receivedUserID user who received the request
     */
    @Override
    public void cancelFriendRequest(final String sentUserID, final String receivedUserID) {
        userRef.child(sentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User sentUser = dataSnapshot.getValue(User.class);
                sentUser.removeSentFriendRequestID(receivedUserID);

                userRef.child(receivedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User receivedUser = dataSnapshot.getValue(User.class);
                        receivedUser.removePendingFriendRequestID(sentUserID);

                        notificationRef.child(receivedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);
                                    if (n != null && n.getType().equals("friend request")
                                            && n.getFromUserID().equals(sentUserID) && n.getToUserID().equals(receivedUserID)) {
                                        notificationRef.child(receivedUserID).child(ds.getKey()).removeValue();
                                        break;
                                    }
                                }

                                userRef.child(sentUserID).setValue(sentUser);
                                userRef.child(receivedUserID).setValue(receivedUser);
                                view.onCancelRequestSuccess(sentUser, receivedUser);
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

    /**
     *
     * @param fromUserID user who received the request and is now accepting the request
     * @param toUserID user who sent the friend request
     */
    @Override
    public void acceptFriendRequest(final String fromUserID, final String toUserID) {
        final String newNotificationID = notificationRef.push().getKey();
        final Notification notification = new Notification();
        notification.setnID(newNotificationID);
        notification.setFromUserID(fromUserID);
        notification.setToUserID(toUserID);
        notification.setType("friend confirmation");

        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                fromUser.addFriendID(toUserID);
                fromUser.removePendingFriendRequestID(toUserID);

                userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User toUser = dataSnapshot.getValue(User.class);
                        toUser.addFriendID(fromUserID);
                        toUser.removeSentFriendRequestID(fromUserID);

                        notificationRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);

                                    if (n != null && n.getType().equals("friend request")) {
                                        String sentID = n.getFromUserID();
                                        String receivedID = n.getToUserID();

                                        if (sentID.equals(toUserID) && receivedID.equals(fromUserID)) {
                                            notificationRef.child(fromUserID).child(ds.getKey()).removeValue();
                                            break;
                                        }

                                    }

                                }

                                notification.setUserName(fromUser.getFullName());
                                notification.setPhotoURL(fromUser.getProfileURL());
                                notification.setContent("has accepted your friend request");
                                notificationRef.child(toUserID).child(newNotificationID).setValue(notification);
                                userRef.child(fromUserID).setValue(fromUser);
                                userRef.child(toUserID).setValue(toUser);

                                if (view != null)
                                    view.onAcceptRequestSuccess(fromUser, toUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                if (view != null)
                                    view.onAcceptRequestFailure(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (view != null)
                            view.onAcceptRequestFailure(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (view != null)
                    view.onAcceptRequestFailure(databaseError.getMessage());
            }
        });

    }

    /**
     *
     * @param declinedUserID user who received the request and is now declining the request
     * @param sentUserID user who sent the request
     */
    @Override
    public void declineFriendRequest(final String declinedUserID, final String sentUserID) {
        userRef.child(declinedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User declinedUser = dataSnapshot.getValue(User.class);
                declinedUser.removePendingFriendRequestID(sentUserID);

                userRef.child(sentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User sentUser = dataSnapshot.getValue(User.class);
                        sentUser.removeSentFriendRequestID(declinedUserID);

                        notificationRef.child(declinedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);
                                    if (n != null && n.getType().equals("friend request")) {
                                        String declinedID = n.getToUserID();
                                        String sentID = n.getFromUserID();

                                        if (declinedID.equals(declinedUserID) && sentID.equals(sentUserID)) {
                                            notificationRef.child(declinedUserID).child(ds.getKey()).removeValue();
                                            break;
                                        }
                                    }

                                }

                                userRef.child(declinedUserID).setValue(declinedUser);
                                userRef.child(sentUserID).setValue(sentUser);
                                view.onDeclineRequestSuccess(declinedUser, sentUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                view.onDeclineRequestFailure(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.onDeclineRequestFailure(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onDeclineRequestFailure(databaseError.getMessage());
            }
        });
    }

    /**
     *
     * @param unfriendingUserID user who is unfriending
     * @param unfriendedUserID user who is being unfriended
     */
    @Override
    public void removeFriendship(final String unfriendingUserID, final String unfriendedUserID) {
        userRef.child(unfriendingUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User unfriendingUser = dataSnapshot.getValue(User.class);
                unfriendingUser.removeFriendID(unfriendedUserID);

                userRef.child(unfriendedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User unfriendedUser = dataSnapshot.getValue(User.class);
                        unfriendedUser.removeFriendID(unfriendingUserID);

                        userRef.child(unfriendingUserID).setValue(unfriendingUser);
                        userRef.child(unfriendedUserID).setValue(unfriendedUser);

                        //remove previous friend confirmation since they are not longer friends
                        notificationRef.child(unfriendingUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);

                                    if (n != null && n.getType().equals("friend confirmation")) {
                                        String acceptedID = n.getFromUserID();
                                        String sentID = n.getToUserID();

                                        if ((acceptedID.equals(unfriendedUserID) && sentID.equals(unfriendingUserID))
                                                || (acceptedID.equals(unfriendingUserID) && sentID.equals(unfriendedUserID))) {
                                            notificationRef.child(unfriendingUserID).child(n.getnID()).removeValue();
                                            break;
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        notificationRef.child(unfriendedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Notification n = ds.getValue(Notification.class);

                                    if (n != null && n.getType().equals("friend confirmation")) {
                                        String acceptedID = n.getFromUserID();
                                        String sentID = n.getToUserID();

                                        if ((acceptedID.equals(unfriendedUserID) && sentID.equals(unfriendingUserID))
                                                || (acceptedID.equals(unfriendingUserID) && sentID.equals(unfriendedUserID))) {
                                            notificationRef.child(unfriendedUserID).child(n.getnID()).removeValue();
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        view.onRemoveFriendshipSuccess(unfriendingUser, unfriendedUser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.onRemoveFriendshipFailure(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onRemoveFriendshipFailure(databaseError.getMessage());
            }
        });
    }

    /**
     *
     * @param userID the user to be loading all friend requests for
     */
    @Override
    public void loadFriendRequests(final String userID) {
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                List<String> sentRequests = user.getSentFriendRequestIDs();
                List<String> pendingRequests = user.getPendingFriendRequestIDs();

                for (String id : sentRequests) {
                    final FriendRequest f = new FriendRequest();
                    f.setType("sent");
                    f.setSentUserID(user.getuID());
                    f.setSentUserName(user.getFullName());

                    userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User toUser = dataSnapshot.getValue(User.class);
                            f.setReceivedUserID(toUser.getuID());
                            f.setReceivedUserName(toUser.getFullName());
                            f.setTargetPhotoURL(user.getProfileURL());

                            view.onLoadRequestsSuccess(f);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }

                for (String id : pendingRequests) {
                    final FriendRequest f = new FriendRequest();
                    f.setType("pending");
                    f.setReceivedUserID(user.getuID());
                    f.setReceivedUserName(user.getFullName());

                    userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User fromUser = dataSnapshot.getValue(User.class);
                            f.setSentUserID(fromUser.getuID());
                            f.setSentUserName(fromUser.getFullName());
                            f.setTargetPhotoURL(fromUser.getProfileURL());

                            view.onLoadRequestsSuccess(f);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadRequestsFailure(databaseError.getMessage());
            }
        });
    }

    /**
     *
     * @param fromUserID user doing the friendship check
     * @param toUserID user to be checked
     */
    @Override
    public void checkUserFriendship(String fromUserID, final String toUserID) {
        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User fromUser = dataSnapshot.getValue(User.class);
                List<String> friendIDs = fromUser.getFriendIDs();

                if (friendIDs.contains(toUserID)) {
                    view.onCheckUserFriendshipSuccess(true);
                } else {
                    view.onCheckUserFriendshipSuccess(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onCheckUserFriendshipFailure(databaseError.getMessage());
            }
        });
    }

    /**
     *
     * @param fromUserID user doing the request check
     * @param toUserID user to be checked
     */
    @Override
    public void checkSentFriendRequest(String fromUserID, final String toUserID) {
        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User fromUser = dataSnapshot.getValue(User.class);
                List<String> sentRequests = fromUser.getSentFriendRequestIDs();

                if (sentRequests.contains(toUserID)) {
                    view.onCheckSentRequest(true);
                } else {
                    view.onCheckSentRequest(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onCheckSentFailure(databaseError.getMessage());
            }
        });
    }
}
