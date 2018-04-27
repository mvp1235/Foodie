package com.example.mvp.foodie.interest;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Interest;
import com.example.mvp.foodie.models.User;

public class InterestContract {

    interface View {
        void onLoadInterestsSuccess(Interest interest);
        void onLoadInterestFailure(String error);
    }


    interface Presenter {
        void loadInterests(BaseActivity activity, String postID);
    }
}
