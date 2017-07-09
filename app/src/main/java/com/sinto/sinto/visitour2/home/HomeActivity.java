package com.sinto.sinto.visitour2.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.VisitourApplication;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by nmcap on 7/9/2017.
 */

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    @Inject
    Lazy<HomeActivityPresenter> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ((VisitourApplication) getApplication()).graph.inject(this);

        presenter.get().attachView(this);
        presenter.get().onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.get().onStart();
    }
}
