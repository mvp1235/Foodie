package com.example.mvp.foodie.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements MessageContract.View {
    private ConversationRecyclerAdapter adapter;
    RecyclerView recyclerView;
    private MessageContract.Presenter presenter;
    private FirebaseAuth mAuth;
    private List<Conversation> conversations;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        presenter.removeConversationEventListener();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);


        recyclerView = view.findViewById(R.id.recyclerView_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        conversations = new ArrayList<>();
        adapter = new ConversationRecyclerAdapter(getContext(), conversations);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(mDividerItemDecoration);

        mAuth = FirebaseAuth.getInstance();
        presenter = new MessagePresenter(this);

        presenter.loadConversations(mAuth.getCurrentUser().getUid());

        return view;
    }

    @Override
    public void onLoadConversationsSuccess(Conversation c) {
        adapter.addConversation(c);
        adapter.sortConversations();
    }

    @Override
    public void onLoadConversationsFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
