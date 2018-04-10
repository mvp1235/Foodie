package com.example.mvp.foodie.main_feed;

import com.example.mvp.foodie.models.Post;

import java.util.List;

public interface MainFeedContract {
    interface View {
        void onPostsLoadedSuccess(List<Post> posts);
        void onPostsLoadedFailure(String error);

    }

    interface Presenter {
        void loadPosts();
    }

    interface Interactor {
        void loadPostsFromFirebase();
    }

    interface onLoadListener {
        void onLoadSuccess(List<Post> posts);
        void onLoadFailure(String error);
    }
}
