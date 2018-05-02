package com.example.mvp.foodie.friend;


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
                        view.onSendRequestSuccess(fromUser, toUser);
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
                                view.onCancelRequestSuccess(fromUser, toUser);
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
     * @param fromUserID user accepting the request
     * @param toUserID user who sent the friend request
     */
    @Override
    public void acceptFriendRequest(final String fromUserID, final String toUserID) {
        final String newNotificationID = notificationRef.push().getKey();
        final Notification notification = new Notification();
        notification.setnID(newNotificationID);
        notification.setFromUserID(fromUserID);
        notification.setToUserID(toUserID);
        notification.setType("friend request");

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
                                    if (n != null && n.getType().equals("friend request")
                                            && n.getFromUserID().equals(fromUserID) && n.getToUserID().equals(toUserID)) {
                                        notificationRef.child(toUserID).child(ds.getKey()).removeValue();
                                        break;
                                    }
                                }

                                notification.setContent(fromUser.getFullName() + " has accepted your friend request");
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

    @Override
    public void declineFriendRequest(final String fromUserID, final String toUserID) {

        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                fromUser.removePendingFriendRequestID(toUserID);

                userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User toUser = dataSnapshot.getValue(User.class);
                        toUser.removeSentFriendRequestID(fromUserID);

                        notificationRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                view.onDeclineRequestSuccess(fromUser, toUser);
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

    @Override
    public void removeFriendship(final String fromUserID, final String toUserID) {
        userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                fromUser.removeFriendID(toUserID);

                userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User toUser = dataSnapshot.getValue(User.class);
                        toUser.removeFriendID(fromUserID);


                        userRef.child(fromUserID).setValue(fromUser);
                        userRef.child(toUserID).setValue(toUser);
                        view.onRemoveFriendshipSuccess(fromUser, toUser);
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

    @Override
    public void loadFriendRequests(final String userID) {
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                List<String> sentRequests = user.getSentFriendRequestIDs();
                List<String> pendingRequests = user.getPendingFriendRequestIDs();

                for (String id : sentRequests) {
                    final FriendRequest f = new FriendRequest();
                    f.setType("sent");
                    f.setFromUserID(userID);
                    f.setFromUserName(user.getFullName());


                    userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User toUser = dataSnapshot.getValue(User.class);
                            f.setToUserID(toUser.getuID());
                            f.setToUserName(toUser.getFullName());
                            f.setTargetPhotoURL(toUser.getProfileURL());

                            view.onLoadRequestsSuccess(f);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }

                for (String id : pendingRequests) {
                    final FriendRequest f = new FriendRequest();
                    f.setType("pending");
                    f.setFromUserID(userID);
                    f.setFromUserName(user.getFullName());

                    userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User toUser = dataSnapshot.getValue(User.class);
                            f.setToUserID(toUser.getuID());
                            f.setToUserName(toUser.getFullName());
                            f.setTargetPhotoURL(toUser.getProfileURL());

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

}
