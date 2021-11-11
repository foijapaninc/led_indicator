package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

public class Pref2 extends Activity
{
    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pref2);

        Intent intent = getIntent();
        final ArrayList<String> pns = intent.getStringArrayListExtra(Common.LIST_OF_CHECKED_APP);

        Button btnSettings = findViewById(R.id.id_btn_setting);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(Common.PREF,MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt(Common.CHECKED_NUM,pns.size());

                for (int i = 0; i < pns.size();i++) {
                    edit.putString(Common.CHECKED_APP+String.valueOf(i), pns.get(i));
                }
                edit.apply();
            }
        });

        Button btnPreview = findViewById(R.id.id_btn_preview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Flash.class);
                startActivity(intent);
            }
        });
    }
}