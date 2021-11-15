package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Pref extends Activity
{
    private int mMin = 0;
    private int mSec = 0;

    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pref);

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

                int setMin = getMin();
                if(setMin <= 0 || setMin > 99) {
                    Toast.makeText(getApplicationContext(),getString(R.string.str_tst_err_min), Toast.LENGTH_SHORT).show();
                }

                int setSec = getSec();
                if(setMin <= 0 || setMin > 99) {
                    Toast.makeText(getApplicationContext(),getString(R.string.str_tst_err_sec), Toast.LENGTH_SHORT).show();
                }

                edit.apply();
            }
        });

        Button btnPreview = findViewById(R.id.id_btn_preview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flash flash = new Flash(getApplicationContext());
                flash.blink1();
            }
        });
    }

    private int getMin(){
        EditText etMin = findViewById(R.id.id_et_min);
        if(etMin.getText().toString().equals(getString(R.string.tn_min))){
            return 0;
        }
        return Integer.parseInt(etMin.getText().toString());
    }

    private int getSec(){
        EditText etSec = findViewById(R.id.id_et_sec);
        if(etSec.getText().toString().equals(getString(R.string.tn_sec))){
            return 0;
        }
        return Integer.parseInt(etSec.getText().toString());
    }
}