package com.sinto.sinto.visitour2.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinto.sinto.visitour2.R;

/**
 * Created by Arvin on 6/25/2017.
 */

public class Groups extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        return rootView;
    }
}
