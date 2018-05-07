package com.example.mvp.foodie.message;

import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.Message;
import com.example.mvp.foodie.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagePresenter implements MessageContract.Presenter {
    private MessageContract.View view;
    private MessageContract.DetailView detailView;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference conversationRef = FirebaseDatabase.getInstance().getReference().child("Conversations");


    private ValueEventListener messageEventListener;
    private ValueEventListener conversationEventListener;

    public MessagePresenter(MessageContract.View view) {
        this.view = view;
    }

    public MessagePresenter(MessageContract.DetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public void loadConversations(String userID) {
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                List<String> conversationIDs = currentUser.getConversationIDs();

                if(conversationIDs.size() > 0) {
                    for (String cID : conversationIDs) {
                        conversationRef.child(cID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                                view.onLoadConversationsSuccess(conversation);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                view.onLoadConversationsFailure(databaseError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadConversationsFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void loadMessagesBetweenTheUsers(final String firstUserID, final String secondUserID) {
        messageEventListener = conversationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Conversation conversation = ds.getValue(Conversation.class);

                    if (conversationExist(conversation, firstUserID, secondUserID)) {
                        detailView.onLoadMessagesSuccess(conversation.getMessages());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                detailView.onLoadMessagesFailure(databaseError.getMessage());
            }
        });
    }

    private boolean conversationExist(Conversation conversation, String userID1, String userID2) {
        String firstID = conversation.getFirstUserID();
        String secondID = conversation.getSecondUserID();

        return (firstID.equals(userID1) && secondID.equals(userID2)) || (firstID.equals(userID2) && secondID.equals(userID1));
    }

    @Override
    public void removeMessageEventListener() {
        if (messageEventListener != null)
            conversationRef.removeEventListener(messageEventListener);
    }

    @Override
    public void removeConversationEventListener() {
        if (conversationEventListener != null)
            conversationRef.removeEventListener(conversationEventListener);
    }

    private void addMessageToExistingConversation(final String conversationID, final Message message) {
        conversationRef.child(conversationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                conversation.addMessage(message);
                conversation.setLastMessageTime(message.getCreatedTime());
                conversationRef.child(conversationID).setValue(conversation);

                detailView.onSendMessageSuccess(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                detailView.onSendMessageFailure(databaseError.getMessage());
            }
        });
    }

    private String getConversationIDBetweenTwoUsers(List<String> firstUserConversations, List<String> secondUserConversations) {
        for (String firstID : firstUserConversations)
            for (String secondID : secondUserConversations)
                if (firstID.equals(secondID))
                    return firstID;

        return null;
    }

    @Override
    public void sendMessage(final String conversationID, final String fromUserID, final String toUserID, final String messageContent) {
        final Message message = new Message();
        String newMessageID = conversationRef.push().getKey();
        message.setmID(newMessageID);
        message.setToUserID(toUserID);
        message.setFromUserID(fromUserID);
        message.setContent(messageContent);
        message.setCreatedTime(System.currentTimeMillis());

        //User clicked on chat from message fragment, which provides an existing conversation ID
        //Add message to existing conversation
        if (conversationID != null) {
            addMessageToExistingConversation(conversationID, message);
        } else {
            //User clicked on new message option
            //Need to check if there already exists a conversation between the two users or not
            userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User fromUser = dataSnapshot.getValue(User.class);

                    userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final User toUser = dataSnapshot.getValue(User.class);

                            List<String> fromUserConversations = fromUser.getConversationIDs();
                            List<String> toUserConversations = toUser.getConversationIDs();

                            String existingConversationID = getConversationIDBetweenTwoUsers(fromUserConversations, toUserConversations);

                            if (existingConversationID != null) {
                                addMessageToExistingConversation(existingConversationID, message);
                            } else {
                                //Create new conversation and add message
                                final Conversation conversation = new Conversation();
                                final String newConversationID = conversationRef.push().getKey();
                                conversation.setcID(newConversationID);
                                conversation.setFirstUserID(fromUserID);
                                conversation.setSecondUserID(toUserID);
                                conversation.addMessage(message);
                                conversation.setLastMessageTime(message.getCreatedTime());

                                //Add conversation id to from user
                                userRef.child(fromUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final User fromUser = dataSnapshot.getValue(User.class);
                                        fromUser.addConversationID(conversation.getcID());

                                        //add conversation id to to user
                                        userRef.child(toUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User toUser = dataSnapshot.getValue(User.class);
                                                toUser.addConversationID(conversation.getcID());

                                                userRef.child(toUserID).setValue(toUser);
                                                userRef.child(fromUserID).setValue(fromUser);
                                                conversationRef.child(newConversationID).setValue(conversation);
                                                detailView.onSendMessageSuccess(message);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                detailView.onSendMessageFailure(databaseError.getMessage());
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        detailView.onSendMessageFailure(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            detailView.onSendMessageFailure(databaseError.getMessage());
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    detailView.onSendMessageFailure(databaseError.getMessage());
                }
            });

        }
    }
}
