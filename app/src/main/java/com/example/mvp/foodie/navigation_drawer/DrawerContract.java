package com.example.mvp.foodie.navigation_drawer;

import com.example.mvp.foodie.models.User;

public interface DrawerContract {
    interface View {
        void onLoadDataSuccess(User user);
        void onLoadDataFailure(String error);
    }

    interface Presenter {

    }
}
