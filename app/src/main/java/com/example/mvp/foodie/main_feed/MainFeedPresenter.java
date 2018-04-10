package com.example.mvp.foodie.main_feed;

import com.example.mvp.foodie.models.Post;

import java.util.List;

public class MainFeedPresenter implements MainFeedContract.Presenter, MainFeedContract.onLoadListener {
    MainFeedContract.View view;
    MainFeedContract.Interactor interactor;

    public MainFeedPresenter(MainFeedContract.View view) {
        this.view = view;
        interactor = new MainFeedInteractor(this);
    }

    @Override
    public void loadPosts() {
        interactor.loadPostsFromFirebase();
    }

    @Override
    public void onLoadSuccess(List<Post> posts) {
        view.onPostsLoadedSuccess(posts);
    }

    @Override
    public void onLoadFailure(String error) {
        view.onPostsLoadedFailure(error);
    }
}
