package com.sinto.sinto.visitour2.chat;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

/**
 * Created by nmcap on 7/9/2017.
 */

public class ChatActivityPresenter {
    ChatActivityView view;

    @Inject
    public ChatActivityPresenter(FirebaseDatabase firebaseDatabase) {

    }

    public void onCreate(Bundle savedInstanceState) {

    }
}

