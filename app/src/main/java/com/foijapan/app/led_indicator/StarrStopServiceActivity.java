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

public class StarrStopServiceActivity extends Activity{
    private PendingIntent notifierIntent;
    private AlarmManager alarmManager;
    int[] repeatIntervals;
    private int repeatIntervalIndex = 0;
    private String mCameraID = "";
    private CameraManager mCameraManager = null;


    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mCameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        //カメラマネージャーにトーチモードのコールバックを登録
        mCameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
            //トーチモードが変更された時の処理
            @Override
            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                super.onTorchModeChanged(cameraId, enabled);
                //カメラIDをセット
                mCameraID = cameraId;
            }
        }, new Handler());
        ToggleButton btnRun = findViewById(R.id.id_btb_start_stop);

        btnRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //SWがfalseならばトーチモードをtrueにしてLDEオン
                    try {
                        mCameraManager.setTorchMode(mCameraID, true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        mCameraManager.setTorchMode(mCameraID, false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    //SWがtrueならばトーチモードをfalseにしてLDEオフ
                }
            }
        });
    }
}