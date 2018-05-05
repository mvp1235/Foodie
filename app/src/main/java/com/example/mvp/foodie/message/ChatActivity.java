package com.example.mvp.foodie.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Message;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.USER_NAME;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    private AppCompatEditText messageET;
    private AppCompatButton sendBtn;

    private RecyclerView recyclerView;
    private MessageRecyclerAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
    }

    private void initViews() {

        recyclerView = findViewById(R.id.recyclerView_id);
        toolbar = findViewById(R.id.toolbar);
        messageET = findViewById(R.id.messageText_id);
        sendBtn = findViewById(R.id.sendBtn_id);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(USER_NAME));


        messageList = new ArrayList<>();
        adapter = new MessageRecyclerAdapter(this, messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
