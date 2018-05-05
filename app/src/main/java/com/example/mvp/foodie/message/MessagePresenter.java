package com.example.mvp.foodie.message;

import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessagePresenter implements MessageContract.Presenter {
    private MessageContract.View view;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference conversationRef;

    public MessagePresenter(MessageContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        conversationRef = FirebaseDatabase.getInstance().getReference().child("Conversations");
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
}
