package com.example.mvp.foodie.message;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.Message;
import com.example.mvp.foodie.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.TO_USER_ID;
import static com.example.mvp.foodie.UtilHelper.USER_NAME;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    private Context context;
    private List<Conversation> conversations;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference conversationRef;

    public ConversationRecyclerAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        conversationRef = FirebaseDatabase.getInstance().getReference().child("Conversations");
    }

    public void sortConversations() {
        Collections.sort(conversations, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation c1, Conversation c2) {
                long time1 = c1.getLastMessageTime();
                long time2 = c2.getLastMessageTime();

                if (time1 > time2)
                    return -1;
                else if (time1 < time2)
                    return 1;
                else
                    return 0;
            }
        });
        notifyDataSetChanged();
    }

    public void addConversation(Conversation conversation) {
        if (!conversations.contains(conversation)) {
            conversations.add(conversation);
            notifyDataSetChanged();
        } else {
            updateExistingConversation(conversation);
        }
    }

    private void updateExistingConversation(Conversation conversation) {
        for (int i=0; i<conversations.size(); i++) {
            Conversation c = conversations.get(i);
            if (c.getcID().equals(conversation.getcID())) {
                conversations.set(i, conversation);
                break;
            }
        }
    }

    public void removeConversation(Conversation conversation) {
        if (conversations.contains(conversation)) {
            conversations.remove(conversation);
            notifyDataSetChanged();
        }
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item_layout, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);

        conversationRef.child(conversation.getcID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Conversation conversation = dataSnapshot.getValue(Conversation.class);
                String currentUserID = mAuth.getCurrentUser().getUid();
                String returnUserID = null;
                if (currentUserID.equals(conversation.getFirstUserID()))
                    returnUserID = conversation.getSecondUserID();
                else if (currentUserID.equals(conversation.getSecondUserID()))
                    returnUserID = conversation.getFirstUserID();

                if (returnUserID != null) {
                    userRef.child(returnUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final User returnUser = dataSnapshot.getValue(User.class);

                            setBlockClickListener(holder, returnUser.getFullName(), returnUser.getuID());

                            holder.userName.setText(returnUser.getFullName());

                            Picasso.get().load(returnUser.getProfileURL())
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .into(holder.userPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {}

                                        @Override
                                        public void onError(Exception e) {
                                            Picasso.get().load(returnUser.getProfileURL()).into(holder.userPhoto);
                                        }
                                    });

                            List<Message> messages = conversation.getMessages();

                            if (messages.size() > 0) {
                                Message lastMessage = messages.get(messages.size()-1);
                                holder.lastMessageTime.setText(lastMessage.getMessageDuration());

                                if (lastMessage.getContent().length() > 100)
                                    holder.lastMessageContent.setText(lastMessage.getContent().substring(0, 97) + "...");
                                else
                                    holder.lastMessageContent.setText(lastMessage.getContent());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setBlockClickListener(ConversationViewHolder holder, final String userName, final String toUserID) {
        holder.conversationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(USER_NAME, userName);
            intent.putExtra(TO_USER_ID, toUserID);
            context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

}
