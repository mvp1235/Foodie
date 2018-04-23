package com.example.mvp.foodie.post;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;

import de.hdodenhof.circleimageview.CircleImageView;

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
    }

    interface Presenter {
        void getLocation(BaseActivity activity, String providedLocation);
        void uploadPost(BaseActivity activity, ImageView imageView, String description, String location, String userID);
        void editPost(BaseActivity activity, ImageView imageView, String description, String location, String userID, String postID);
        void deletePost(BaseActivity activity, String postID, String ownerID);
        void loadUserInfo(BaseActivity activity, String userID, CircleImageView imageView, AppCompatTextView nameTV);
        void checkIfUserLikedPost(BaseActivity activity, Post post, String userID, AppCompatImageView postHeart);
        void handleUserInterestClick(BaseActivity activity, Post post, String userID, AppCompatImageView postHeart);
    }

}
