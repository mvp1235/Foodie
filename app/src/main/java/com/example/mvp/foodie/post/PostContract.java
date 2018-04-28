package com.example.mvp.foodie.post;

import android.content.Intent;
import android.widget.ImageView;
import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;

public interface PostContract {
    interface View {
        void onLocationPickedSuccess(Intent intent);
        void onLocationPickedFailure(String error);
        void onPostCreated(Post post);
        void onPostFailed(String error);
    }

    interface EditView {
        void onLocationPickedSuccess(Intent intent);
        void onLocationPickedFailure(String error);
        void onPostEditSuccess(Post post);
        void onPostEditFailure(String error);
    }

    interface DetailView {
        void onPostDeleteSuccess(String successMessage);
        void onPostDeleteFailure(String error);
        void incrementInterestCount(String incrementedCount);
        void decrementInterestCount(String decrementedCount);
        void displayInterestCountChangeError(String error);
        void onLoadPostSuccess(Post post, User postOwner);
        void onLoadPostFailure(String error);
        void onUserLikedPost();
        void onUserNotLikedPost();
    }

    interface ListView {
        void onLoadPostSuccess(Post post);
        void onLoadPostFailure(String error);
    }

    interface Presenter {
        void getLocation(BaseActivity activity, String providedLocation);
        void uploadPost(BaseActivity activity, ImageView imageView, String description, String location, String userID);
        void editPost(BaseActivity activity, ImageView imageView, String description, String location, String userID, String postID);
        void deletePost(BaseActivity activity, String postID, String ownerID);
        void loadDetailPost(BaseActivity activity, String postID, String postOwnerID, String currentUserID);
        void checkIfUserLikedPost(BaseActivity activity, String postID, String userID);
        void handleUserInterestClick(BaseActivity activity, String postID, String userID);
        void loadAllUserPosts(BaseActivity activity, String userID);
    }

}
