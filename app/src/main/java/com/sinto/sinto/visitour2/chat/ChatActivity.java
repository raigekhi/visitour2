package com.sinto.sinto.visitour2.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.VisitourApplication;
import com.sinto.sinto.visitour2.data.MessageEntity;
import com.sinto.sinto.visitour2.legacy.SignInActivity;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Lazy;


public class ChatActivity extends AppCompatActivity implements ChatActivityView {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE = 2;

    ProgressBar progressBar;
    Button sendButton;
    EditText editText;
    RecyclerView messageRecyclerView;

    @Inject
    Lazy<ChatActivityPresenter> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ((VisitourApplication) getApplication()).graph.inject(this);

        presenter.get().attachView(this);
        presenter.get().onCreate(savedInstanceState);

        setupBindViews();
        setupRecyclerView();
        setupListeners();
    }

    public void setupBindViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sendButton = (Button) findViewById(R.id.sendButton);
        editText = (EditText) findViewById(R.id.messageEditText);
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
    }

    public void setupRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        final ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(
                MessageEntity.class, R.layout.item_message,
                ChatMessageViewHolder.class, presenter.get().getMessagesChild());
        adapter.attachView(this);
        adapter.attachUser(presenter.get().getUser());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    messageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(adapter);
    }

    public void setupListeners() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.get().sendMessage(editText.getText().toString());
                editText.setText("");
            }
        });
    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void redirectToLoginActivity() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public void hideLoadingBar() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                presenter.get().signOut();
                redirectToLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

