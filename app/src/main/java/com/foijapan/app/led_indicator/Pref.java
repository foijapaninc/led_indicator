package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Pref extends Activity implements TextWatcher
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
        final CheckBox cbInfinity = findViewById(R.id.id_cb_tn_infinity);
        final EditText etMin = findViewById(R.id.id_et_min);
        final EditText etSec = findViewById(R.id.id_et_sec);

        etMin.addTextChangedListener(this);
        etSec.addTextChangedListener(this);

        cbInfinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbInfinity.isChecked()){
                    etMin.setText("");
                }
                etMin.setEnabled(!(cbInfinity.isChecked()));
            }
        });

        Button btnSettings = findViewById(R.id.id_btn_setting);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String warning = "";
                SharedPreferences pref = getSharedPreferences(Common.PREF,MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt(Common.CHECKED_NUM,pns.size());

                for (int i = 0; i < pns.size();i++) {
                    edit.putString(Common.CHECKED_APP+String.valueOf(i), pns.get(i));
                }

                int setMin = getMin(etMin);
                if(setMin <= 0 || setMin > 99) {
                    warning = String.format(getString(R.string.str_tst_err_pref),
                                                    getString(R.string.str_time_notification1),
                                                    getString(R.string.str_time_notification2));
                }

                if(!(cbInfinity.isChecked()) && !warning.isEmpty()){
                    Toast.makeText(getApplicationContext(),warning,Toast.LENGTH_SHORT).show();
                    warning = "";
                }

                int setSec = getSec(etSec);
                if(setSec <= 0 || setSec> 99) {
                    warning = String.format(getString(R.string.str_tst_err_pref),
                            getString(R.string.str_repeat_interval1),
                            getString(R.string.str_repeat_interval2));
                }

                if(!warning.isEmpty()){
                    Toast.makeText(getApplicationContext(),warning,Toast.LENGTH_SHORT).show();
                }

                initialize(edit);

                edit.putBoolean(Common.DURATION_INFINITY,cbInfinity.isChecked());
                if(cbInfinity.isChecked()){
                    edit.putInt(Common.DURATION, 0);
                }
                else {
                    edit.putInt(Common.DURATION,Integer.parseInt(etMin.getText().toString()));
                }
                edit.putInt(Common.INTERVAL,Integer.parseInt(etSec.getText().toString()));
                edit.apply();
            }
        });

        Button btnPreview = findViewById(R.id.id_btn_preview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Flash flash = new Flash(getApplicationContext());
                flash.blink2();
            }
        });
    }

    private void initialize(SharedPreferences.Editor editor){
        editor.putBoolean(Common.DURATION_INFINITY,false);
        editor.putInt(Common.DURATION, 0);
        editor.putInt(Common.INTERVAL,0);
    }

    private int getMin(EditText etMin){
        if(etMin.getText().toString().equals(getString(R.string.tn_min))){
            return 0;
        }
        return Integer.parseInt(etMin.getText().toString());
    }

    private int getSec(EditText etSec){
        if(etSec.getText().toString().equals(getString(R.string.tn_sec))){
            return 0;
        }
        return Integer.parseInt(etSec.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}