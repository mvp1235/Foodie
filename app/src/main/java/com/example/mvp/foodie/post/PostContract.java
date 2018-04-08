package com.example.mvp.foodie.post;

import android.widget.ImageView;
import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;

public interface PostContract {
    interface View {
        void onPostCreated(Post post);
        void onPostFailed(String error);
    }

    interface Presenter {
        void uploadPost(BaseActivity activity, ImageView imageView, String description, String location, String userID);
    }

    interface Interactor {
        void uploadPostToFirebaseStorage(BaseActivity activity, ImageView imageView, String description, String location, String userID);
    }

    interface onPostCreateListener {
        void onPostSuccess(Post post);
        void onPostFailure(String error);
    }
}
