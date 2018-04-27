package com.example.mvp.foodie.interest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Interest;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.POST_ID;

public class InterestListActivity extends BaseActivity implements InterestContract.View{

    private RecyclerView recyclerView;
    private InterestRecyclerAdapter adapter;

    private List<Interest> interestList;

    private InterestContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_list);

        Intent intent = getIntent();
        String postID = intent.getStringExtra(POST_ID);

        interestList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView_id);
        adapter = new InterestRecyclerAdapter(this, interestList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        presenter = new InterestPresenter(this);
        presenter.loadInterests(InterestListActivity.this, postID);


    }

    @Override
    public void onLoadInterestsSuccess(Interest interest) {
        adapter.addInterest(interest);
    }

    @Override
    public void onLoadInterestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
