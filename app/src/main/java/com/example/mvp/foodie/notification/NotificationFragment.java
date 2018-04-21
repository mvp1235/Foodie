package com.example.mvp.foodie.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements NotificationContract.View{
    private NotificationRecyclerAdapter adapter;
    private NotificationContract.Presenter presenter;
    RecyclerView recyclerView;

    public NotificationFragment() {
        // Required empty public constructor
        presenter = new NotificationPresenter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotificationRecyclerAdapter(getContext(), new ArrayList<Notification>());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(mDividerItemDecoration);

        String userID = ((BaseActivity)getActivity()).getmAuth().getCurrentUser().getUid();

        presenter.loadNotifications((BaseActivity)getActivity(), userID);

        return view;

    }

    @Override
    public void onLoadSuccess(List<Notification> notifications) {
        adapter.setNotificationList(notifications);
    }

    @Override
    public void onLoadFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
