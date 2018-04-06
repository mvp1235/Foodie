package com.example.mvp.foodie.navigation_drawer;

import com.example.mvp.foodie.models.User;

public class DrawerPresenter implements DrawerContract.Presenter, DrawerContract.onLoadListener {
    private DrawerContract.View view;
    private DrawerContract.Interactor interactor;

    public DrawerPresenter(DrawerContract.View view) {
        this.view = view;
        interactor = new DrawerInteractor(this);
    }

    @Override
    public void loadData(String userID) {
        interactor.performLoadData(userID);
    }

    @Override
    public void onLoadSuccess(User user) {
        view.onLoadDataSuccess(user);
    }

    @Override
    public void onLoadFailure(String error) {
        view.onLoadDataFailure(error);
    }
}
