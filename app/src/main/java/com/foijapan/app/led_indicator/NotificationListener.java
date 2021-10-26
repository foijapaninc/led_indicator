package com.foijapan.app.led_indicator;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(isSetPackageName(sbn.getPackageName())){
            StartStop ss = new StartStop();
            ss.blink2();
        }
    }

    private boolean isSetPackageName(String pn){
        boolean ret = false;

        return ret;
    }
}

