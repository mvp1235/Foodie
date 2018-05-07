package com.example.mvp.foodie.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.R;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messageList;
    private FirebaseAuth mAuth;

    class LeftMessageViewHolder extends RecyclerView.ViewHolder  {
        public CircleImageView userPhoto;
        public AppCompatTextView messageContent, messageTime;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.messageUserPhoto_id);
            messageContent = itemView.findViewById(R.id.messageContent_id);
            messageTime = itemView.findViewById(R.id.messageTime_id);
        }

    }

    class RightMessageViewHolder extends RecyclerView.ViewHolder  {
        public AppCompatTextView messageContent, messageTime;

        public RightMessageViewHolder(View itemView) {
            super(itemView);
            messageContent = itemView.findViewById(R.id.messageContent_id);
            messageTime = itemView.findViewById(R.id.messageTime_id);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        String fromID = message.getFromUserID();

        if (mAuth.getCurrentUser().getUid().equals(fromID))
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View rightView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item_layout, parent, false);
                return new RightMessageViewHolder(rightView);
            case 1:
                View leftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item_layout, parent, false);
                return new LeftMessageViewHolder(leftView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                RightMessageViewHolder rightHolder = (RightMessageViewHolder) holder;
                loadRightMessageDetail(message, rightHolder);
                break;
            case 1:
                LeftMessageViewHolder leftHolder = (LeftMessageViewHolder) holder;
                loadLeftMessageDetail(message, leftHolder);
                break;
        }
    }

    private void loadRightMessageDetail(Message message, RightMessageViewHolder holder) {
        holder.messageContent.setText(message.getContent());
        holder.messageTime.setText(message.getMessageDuration());
    }

    private void loadLeftMessageDetail(final Message message, final LeftMessageViewHolder holder) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(message.getFromUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User fromUser = dataSnapshot.getValue(User.class);
                holder.messageContent.setText(message.getContent());
                holder.messageTime.setText(message.getMessageDuration());

                Picasso.get().load(fromUser.getProfileURL())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.userPhoto, new Callback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(fromUser.getProfileURL()).into(holder.userPhoto);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public MessageRecyclerAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        mAuth = FirebaseAuth.getInstance();
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void removeMessage(Message message) {
        if (messageList.contains(message)) {
            messageList.remove(message);
            notifyDataSetChanged();
        }
    }
}
