package com.example.mvp.foodie.message;


import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.Message;

import java.util.List;

public interface MessageContract {
    interface View {
        void onLoadConversationsSuccess(Conversation c);
        void onLoadConversationsFailure(String error);
    }

    interface DetailView {
        void onLoadMessagesSuccess(List<Message> messageList);
        void onLoadMessagesFailure(String error);
        void onSendMessageSuccess(Message message);
        void onSendMessageFailure(String error);
    }

    interface Presenter {
        void loadConversations(String userID);
        void loadMessagesBetweenTheUsers(String firstUserID, String secondUserID);
        void removeMessageEventListener();
        void removeConversationEventListener();
        void sendMessage(String conversationID, String fromUserID, String toUserID, String messageContent);
    }
}
