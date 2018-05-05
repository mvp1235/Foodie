package com.example.mvp.foodie.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Conversation;
import com.example.mvp.foodie.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.CONVERSATION_ID;
import static com.example.mvp.foodie.UtilHelper.TO_USER_ID;
import static com.example.mvp.foodie.UtilHelper.USER_ID;
import static com.example.mvp.foodie.UtilHelper.USER_NAME;

public class ChatActivity extends AppCompatActivity implements MessageContract.DetailView{

    Toolbar toolbar;
    private AppCompatEditText messageET;
    private AppCompatButton sendBtn;

    private RecyclerView recyclerView;
    private MessageRecyclerAdapter adapter;
    private List<Message> messageList;

    private MessageContract.Presenter presenter;

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

        presenter = new MessagePresenter(this);
        presenter.loadMessagesBetweenTheUsers(FirebaseAuth.getInstance().getCurrentUser().getUid(), getIntent().getStringExtra(TO_USER_ID));

        setUpListeners();
    }

    private void setUpListeners() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent = messageET.getText().toString();
                if (!TextUtils.isEmpty(messageContent)) {
                    String conversationID = getIntent().getStringExtra(CONVERSATION_ID);
                    String fromUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String toUserID = getIntent().getStringExtra(TO_USER_ID);
                    presenter.sendMessage(conversationID, fromUserID, toUserID, messageContent);
                }
            }
        });
    }

    @Override
    public void onLoadMessagesSuccess(List<Message> messageList) {
        adapter.setMessageList(messageList);
    }

    @Override
    public void onLoadMessagesFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageSuccess(Message message) {
        messageET.setText("");
        adapter.addMessage(message);
    }

    @Override
    public void onSendMessageFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
