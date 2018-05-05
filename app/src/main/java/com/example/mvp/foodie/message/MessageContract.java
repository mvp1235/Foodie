package com.example.mvp.foodie.message;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.User;

public interface MessageContract {
    interface View {

    }

    interface Adapter {
        void onLoadConversationSuccess(Conversation conversation, User returnUser, ConversationViewHolder holder);
        void onLoadConversationFailure(String error);
    }

    interface Presenter {
        void loadConversationById(String conversationID, ConversationViewHolder holder);
    }
}
