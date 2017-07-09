package com.sinto.sinto.visitour2.home;

import javax.inject.Inject;

/**
 * Created by nmcap on 7/9/2017.
 */

public class HomeActivityPresenter {
    HomeActivityView view;

    @Inject
    public HomeActivityPresenter() {}

    public void attachView(HomeActivityView view) {
        this.view = view;
    }

    public void onCreate() {

    }

    public void onStart() {

    }
}
