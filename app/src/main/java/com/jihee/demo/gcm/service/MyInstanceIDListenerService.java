package com.jihee.demo.gcm.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by 서지희 on 2015-08-19.
 */
public class MyInstanceIDListenerService  extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
