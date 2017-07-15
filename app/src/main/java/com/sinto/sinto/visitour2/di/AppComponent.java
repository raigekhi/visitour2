package com.sinto.sinto.visitour2.di;

import com.sinto.sinto.visitour2.VisitourApplication;
import com.sinto.sinto.visitour2.chat.ChatActivity;
import com.sinto.sinto.visitour2.home.HomeActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by nmcap on 7/9/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    public void inject(VisitourApplication application);
    public void inject(HomeActivity homeActivity);
    public void inject(ChatActivity chatActivity);
}
