package com.foijapan.app.led_indicator;

import android.content.SharedPreferences;
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

        }
    }

    private boolean isSetPackageName(String pn){
        boolean ret = false;
        SharedPreferences pref = getSharedPreferences(Common.PREF,MODE_PRIVATE);
        int numofpn = pref.getInt(Common.CHECKED_NUM,0);
        for(int i = 0;i < numofpn;i++){
            String storedPn = pref.getString(Common.CHECKED_APP+String.valueOf(i),"");
            if(storedPn.equals(pn)){
                ret = true;
                break;
            }
        }
        return ret;
    }
}