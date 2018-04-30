package com.example.mvp.foodie.friend;

public class FriendContract {

    public interface View {
        void onSendRequestSuccess();
        void onSendRequestFailure(String error);
        void onCancelRequestSuccess();
        void onCancelRequestFailure(String error);
        void onAcceptRequestSuccess();
        void onAcceptRequestFailure(String error);
    }

    public interface Presenter {
        void sendFriendRequest(String fromUserID, String toUserID);
        void cancelFriendRequest(String fromUserID, String toUserID);
        void acceptFriendRequest(String fromUserID, String toUserID);
        void declineFriendRequest(String fromUserID, String toUserID);
    }
}
