package com.sinto.sinto.visitour2.data;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nmcap on 7/9/2017.
 */

public class UserEntity {
    public String userId;
    public String email;
    public String displayName;
    public Uri photoUrl;
    public LatLng latLng;

    public UserEntity(String id, String email, String displayName) {
        this.userId = id;
        this.email = email;
        this.displayName = displayName;
    }

    public UserEntity() {

    }
}
