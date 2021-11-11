package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Flash extends Activity{
    private String mCameraID = "";
    private CameraManager mCameraManager = null;
    private Context mCtx = null;
    private final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.flash_preview);

        setCameraInfo();
        blink3();

    }

    private void setTorch(boolean isFlash){
        if(mCameraManager == null || mCameraID.isEmpty()) {
            setCameraInfo();
        }
        try {
            mCameraManager.setTorchMode(mCameraID, isFlash);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setCameraInfo() {
        if(mCameraManager == null) {
            mCameraManager = (CameraManager) mCtx.getSystemService(Context.CAMERA_SERVICE);
        }

        if(mCameraID.isEmpty()) {
            mCameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                @Override
                public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                    super.onTorchModeChanged(cameraId, enabled);
                    mCameraID = cameraId;
                }
            }, new Handler());
        }
    }

    public Runnable blink1(){
        final Runnable r = new Runnable() {
            boolean isLight = false;
            @Override
            public void run() {
                if(!isLight){
                    setTorch(true);
                    isLight = true;
                    handler.postDelayed(this, 50);
                }
                else{
                    setTorch(false);
                    isLight = false;
                    handler.postDelayed(this, 9950);
                }
            }
        };
        handler.post(r);
        return r;
    }

    public Runnable blink2(){
        final Runnable r = new Runnable() {
            int count = 1;
            @Override
            public void run() {
                if(count%2 == 0 && count < 6){
                    setTorch(true);
                    handler.postDelayed(this, 50);
                    count++;
                }
                else if(count < 6){
                    setTorch(false);
                    handler.postDelayed(this, 500);
                    count++;
                }
                else if(count == 6){
                    setTorch(false);
                    handler.postDelayed(this, 8400);
                    count = 0;
                }
            }
        };
        handler.post(r);
        return r;
    }

    public Runnable blink3(){
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                for(int j = 0; j < 2 ; j++) {
                    for (int i = 0; i < 3; i++) {
                        setTorch(true);
                        handler.postDelayed(this, 50);
                        setTorch(false);
                        handler.postDelayed(this, 30);
                    }
                    setTorch(false);
                    handler.postDelayed(this, 80);
                }

                for (int i = 0; i < 7; i++) {
                    setTorch(true);
                    handler.postDelayed(this, 50);
                    setTorch(false);
                    handler.postDelayed(this, 30);
                }
                setTorch(false);
                handler.postDelayed(this, (1280 + 80));
            }
        };
        handler.post(r);
        return r;
    }

    private Runnable getRunnable() {
        Runnable retRunnable = null;
        SharedPreferences pref = mCtx.getSharedPreferences(Common.PREF, Context.MODE_PRIVATE);
        switch (pref.getInt(Common.PATTERN, 1)) {
            case 1:
                retRunnable = blink1();
                break;
            case 2:
                retRunnable = blink2();
                break;
            case 3:
                retRunnable = blink3();
                break;
            default:
                retRunnable = blink1();
                break;
        }
        return retRunnable;
    }

    public void stopFlash(){
        handler.removeCallbacks(getRunnable());
    }
}