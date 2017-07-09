package com.sinto.sinto.visitour2.services;

import com.google.firebase.database.FirebaseDatabase;
import com.sinto.sinto.visitour2.data.MessageEntity;

import javax.inject.Inject;

/**
 * Created by nmcap on 7/9/2017.
 */

public class GroupChatRepository {
    FirebaseDatabase firebaseDatabase;

    @Inject
    public GroupChatRepository(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public MessageEntity[] listMessages(String groupId) {
        return null;
    }

    public void addMessage(String groupId, MessageEntity messageEntity) {

    }

    public String[] listMemberId(String groupId) {
        return null;
    }

    public void addMember(String groupId, String userId) {

    }
}
