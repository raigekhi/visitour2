package com.sinto.sinto.visitour2.legacy.tabs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sinto.sinto.visitour2.chat.ChatActivity;
import com.sinto.sinto.visitour2.legacy.MapsActivity;
import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.webview.WebViewActivity;

/**
 * Created by Arvin on 6/25/2017.
 */

public class Groups extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);

        // Assign fields
        Button mapButton = (Button) rootView.findViewById(R.id.map_button);
        // Set click listeners
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });

        Button chatButton = (Button) rootView.findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        Button adminButton = (Button) rootView.findViewById(R.id.admin_button);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebViewActivity.class));
            }
        });

        return rootView;
    }
}
