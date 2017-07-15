package com.sinto.sinto.visitour2.chat;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinto.sinto.visitour2.data.MessageEntity;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by nmcap on 7/9/2017.
 */

public class ChatActivityPresenter {
    public static final String ANONYMOUS = "anonymous";

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ChatActivityView view;

    @Inject
    public ChatActivityPresenter(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
    }

    public void attachView(ChatActivityView view) {
        this.view = view;
    }

    public void onCreate(Bundle savedInstanceState) {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            view.redirectToLoginActivity();
            return;
        }
    }

    public void sendMessage(String text) {
        MessageEntity message = new MessageEntity();
        message.senderId = firebaseUser.getUid();
//        message.photoUrl = firebaseUser.getPhotoUrl();
        message.text = text;
        message.timestamp = new Date().getTime();

        firebaseDatabase.getReference("messages").push().setValue(message);
    }

    public DatabaseReference getMessagesChild() {
        return firebaseDatabase.getReference("messages");
    }

    public FirebaseUser getUser() {
        return firebaseUser;
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}

