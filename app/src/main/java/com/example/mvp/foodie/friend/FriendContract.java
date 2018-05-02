package com.example.mvp.foodie.friend;

import com.example.mvp.foodie.models.FriendRequest;
import com.example.mvp.foodie.models.User;


public class FriendContract {

    public interface View {
        void onCheckUserFriendshipSuccess(boolean friend);
        void onCheckUserFriendshipFailure(String error);
        void onSendRequestSuccess(User fromUser, User toUser);
        void onSendRequestFailure(String error);
        void onCancelRequestSuccess(User fromUser, User toUser);
        void onCancelRequestFailure(String error);
        void onAcceptRequestSuccess(User fromUser, User toUser);
        void onAcceptRequestFailure(String error);
        void onDeclineRequestSuccess(User fromUser, User toUser);
        void onDeclineRequestFailure(String error);
        void onLoadRequestsSuccess(FriendRequest friendRequest);
        void onLoadRequestsFailure(String error);
    }

    public interface Adapter {}

    public interface Presenter {
        void sendFriendRequest(String fromUserID, String toUserID);
        void cancelFriendRequest(String fromUserID, String toUserID);
        void acceptFriendRequest(String fromUserID, String toUserID);
        void declineFriendRequest(String fromUserID, String toUserID);
        void loadFriendRequests(String userID);
        void checkUserFriendship(String fromUserID, String toUserID);
    }
}
