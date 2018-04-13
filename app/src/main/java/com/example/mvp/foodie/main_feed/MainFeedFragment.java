package com.example.mvp.foodie.main_feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.post.PostRecyclerAdapter;
import com.example.mvp.foodie.models.Post;

import java.util.ArrayList;
import java.util.List;

public class MainFeedFragment extends Fragment implements MainFeedContract.View {

    private MainFeedContract.Presenter presenter;
    private PostRecyclerAdapter adapter;
    RecyclerView recyclerView;

    public MainFeedFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PostRecyclerAdapter(getContext(), new ArrayList<Post>());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(mDividerItemDecoration);

        presenter = new MainFeedPresenter(this);
        presenter.loadPosts();
        
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPostsLoadedSuccess(List<Post> posts) {
        adapter.setPosts(posts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostsLoadedFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

}
