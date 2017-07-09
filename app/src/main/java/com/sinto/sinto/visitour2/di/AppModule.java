package com.sinto.sinto.visitour2.di;

import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.VisitourApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nmcap on 7/9/2017.
 */

@Module
public class AppModule {
    VisitourApplication application;

    public AppModule(VisitourApplication application) {
        this.application = application;
    }

    @Provides
    public Context provideContext() {
        return application;
    }

    @Provides
    public GoogleApiClient provideGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(application)
                .addApi(LocationServices.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Provides
    public FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
