package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class StartStop extends Activity{
    private String mCameraID = "";
    private CameraManager mCameraManager = null;

    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ToggleButton btnRun = findViewById(R.id.id_btb_start_stop);

        btnRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
 //                   blink1();
                    blink2();
                    //                    setTorch(true);
                }else{
                    setTorch(false);
                }
            }
        });
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
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
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

    public void blink1(){
        final Handler handler = new Handler();
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
    }

    public void blink2(){
        final Handler handler = new Handler();
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
    }
}