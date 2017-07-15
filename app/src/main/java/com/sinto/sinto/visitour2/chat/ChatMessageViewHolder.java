package com.sinto.sinto.visitour2.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinto.sinto.visitour2.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nmcap on 7/16/2017.
 */

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView messageTextView;
    public ImageView messageImageView;
    public TextView messengerTextView;
    public CircleImageView messengerImageView;

    public ChatMessageViewHolder(View v) {
        super(v);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
        messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
    }
}
