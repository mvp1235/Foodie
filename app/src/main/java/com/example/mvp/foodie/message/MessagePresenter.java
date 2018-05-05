package com.example.mvp.foodie.message;

import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MessagePresenter implements MessageContract.Presenter {
    private MessageContract.Adapter adapter;
    private MessageContract.View view;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference conversationRef;

    public MessagePresenter(MessageContract.Adapter adapter) {
        this.adapter = adapter;
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        conversationRef = FirebaseDatabase.getInstance().getReference().child("Conversations");
    }

    public MessagePresenter(MessageContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        conversationRef = FirebaseDatabase.getInstance().getReference().child("Conversations");
    }


    @Override
    public void loadConversationById(String conversationID, final ConversationViewHolder holder) {
        conversationRef.child(conversationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Conversation conversation = dataSnapshot.getValue(Conversation.class);
                String currentUserID = mAuth.getCurrentUser().getUid();
                String returnUserID = null;
                if (currentUserID.equals(conversation.getFirstUserID()))
                    returnUserID = conversation.getFirstUserID();
                else if (currentUserID.equals(conversation.getSecondUserID()))
                    returnUserID = conversation.getSecondUserID();

                if (returnUserID != null) {
                    userRef.child(returnUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User returnUser = dataSnapshot.getValue(User.class);
                            adapter.onLoadConversationSuccess(conversation, returnUser, holder);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            adapter.onLoadConversationFailure(databaseError.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adapter.onLoadConversationFailure(databaseError.getMessage());
            }
        });
    }
}
