package com.jihee.demo.gcm.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by 서지희 on 2015-08-19.
 */
public class MyGcmListenerService extends IntentService {
    private static final String TAG = "MyGcmListenerService";

    public MyGcmListenerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
