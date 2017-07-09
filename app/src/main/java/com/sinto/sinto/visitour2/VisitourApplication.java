package com.sinto.sinto.visitour2;

import android.app.Application;

import com.sinto.sinto.visitour2.di.AppComponent;
import com.sinto.sinto.visitour2.di.AppModule;
import com.sinto.sinto.visitour2.di.DaggerAppComponent;

import dagger.Lazy;

/**
 * Created by nmcap on 7/9/2017.
 */

public class VisitourApplication extends Application {
    public AppComponent graph;

    @Override
    public void onCreate() {
        super.onCreate();

        graph = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        graph.inject(this);
    }
}
