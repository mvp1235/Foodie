package com.example.mvp.foodie.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private NotificationRecyclerAdapter adapter;
    RecyclerView recyclerView;

    public NotificationFragment() {
        // Required empty public constructor
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

        loadNotifications(userID);

        return view;

    }

    private void loadNotifications(String userID) {
        DatabaseReference notificationRef = ((BaseActivity)getActivity()).getmDatabase().child("Notifications");
        notificationRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Notification> notifications = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification n = snapshot.getValue(Notification.class);
                    notifications.add(n);
                }

                if (notifications.size() > 0) {
                    Collections.reverse(notifications);
                    adapter.setNotificationList(notifications);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
