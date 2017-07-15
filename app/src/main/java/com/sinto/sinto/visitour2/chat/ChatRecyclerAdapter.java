package com.sinto.sinto.visitour2.chat;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.data.MessageEntity;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by nmcap on 7/16/2017.
 */

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<MessageEntity, ChatMessageViewHolder> {
    private static final String MESSAGE_URL = "https://visitour-2a6d8.firebaseio.com/messages";

    private ChatActivityView view;
    private FirebaseUser user;

    public ChatRecyclerAdapter(Class<MessageEntity> modelClass, int modelLayout, Class<ChatMessageViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public void attachView(ChatActivityView view) {
        this.view = view;
    }

    public void attachUser(FirebaseUser user) {
        this.user = user;
    }

    @Override
    protected MessageEntity parseSnapshot(DataSnapshot snapshot) {
        MessageEntity message = super.parseSnapshot(snapshot);
        if (message != null) {
            message.messageId = snapshot.getKey();
        }
        return message;
    }

    @Override
    protected void populateViewHolder(final ChatMessageViewHolder viewHolder, MessageEntity message, int position) {
        view.hideLoadingBar();
        if (message.text != null) {
            viewHolder.messageTextView.setText(message.text);
            viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messageImageView.setVisibility(ImageView.GONE);
        }

        viewHolder.messengerTextView.setText(message.senderId);
        if (message.photoUrl == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(view.getContext(),
                    R.drawable.logoo));
        } else {
            Glide.with(view.getContext())
                    .load(message.photoUrl)
                    .into(viewHolder.messengerImageView);
        }

        if (message.text != null) {
            FirebaseAppIndex.getInstance().update(getMessageIndexable(message));
        }
        FirebaseUserActions.getInstance().end(getMessageViewAction(message));
    }


    private Action getMessageViewAction(MessageEntity message) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(message.senderId, MESSAGE_URL.concat(message.messageId))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(MessageEntity message) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(user.getUid() == message.senderId)
                .setName(message.senderId)
                .setUrl(MESSAGE_URL.concat(message.messageId + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(user.getUid())
                .setUrl(MESSAGE_URL.concat(message.messageId + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(message.text)
                .setUrl(MESSAGE_URL.concat(message.messageId))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
    }
}
