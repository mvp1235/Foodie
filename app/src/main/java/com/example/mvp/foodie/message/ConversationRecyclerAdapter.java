package com.example.mvp.foodie.message;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationViewHolder> implements MessageContract.Adapter{

    private Context context;
    private List<Conversation> conversations;
    private MessageContract.Presenter presenter;

    public ConversationRecyclerAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
        presenter = new MessagePresenter(this);
    }

    public void addConversation(Conversation conversation) {
        conversations.add(conversation);
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        presenter.loadConversationById(conversation.getcID(), holder);

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    @Override
    public void onLoadConversationSuccess(Conversation conversation, User returnUser, ConversationViewHolder holder) {
        holder.userName.setText(returnUser.getFullName());
        Picasso.get().load(returnUser.getProfileURL()).into(holder.userPhoto);

        List<Message> messages = conversation.getMessages();

        if (messages.size() > 0) {
            Message lastMessage = messages.get(messages.size()-1);
            holder.lastMessageContent.setText(lastMessage.getContent());
            holder.lastMessageTime.setText(lastMessage.getMessageDuration());
        }
    }

    @Override
    public void onLoadConversationFailure(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
