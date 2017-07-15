package com.sinto.sinto.visitour2.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by nmcap on 7/9/2017.
 */

public interface ChatActivityView {
    public void redirectToLoginActivity();
    public void setupListeners();
    public void uploadImage();
    public void hideLoadingBar();
    public Context getContext();
}
