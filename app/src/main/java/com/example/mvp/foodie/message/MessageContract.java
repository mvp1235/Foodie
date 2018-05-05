package com.example.mvp.foodie.message;


import com.example.mvp.foodie.models.Conversation;

public interface MessageContract {
    interface View {
        void onLoadConversationsSuccess(Conversation c);
        void onLoadConversationsFailure(String error);
    }

    interface Presenter {
        void loadConversations(String userID);
    }
}
