package com.foijapan.app.led_indicator;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;

public class Flash{
    private String mCameraID = "";
    private CameraManager mCameraManager = null;
    private Context mCtx = null;
    private final Handler handler = new Handler();

    Flash(Context context){
        mCtx = context;
    }

    private boolean flashLightOn() {
        try {
            if (mCameraManager == null) {
                initCameraManagerInstance();
            }
            String cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            return false;
        }
        return true;
    }

    //    Stop Torch mode using below code:
    private boolean flashLightOff() {
        try {
            if (mCameraManager == null) {
                initCameraManagerInstance();
            }
            String cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            return false;
        }
        return true;
    }

    private void initCameraManagerInstance() {
        if (mCameraManager == null) {
            mCameraManager = (CameraManager) mCtx.getSystemService(Context.CAMERA_SERVICE);
        }
    }

    private void setTorch(boolean isFlash){
        if(isFlash) {
            flashLightOn();
        }
        else{
            flashLightOff();
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