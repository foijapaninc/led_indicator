package com.foijapan.app.led_indicator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppList {
    Context mCtx = null;

    AppList(Context context){
        mCtx = context;
    }
    public String[] readFromFile() throws IOException {
        String strs[] = new String[Common.getNum()];
        InputStream fis = mCtx.getResources().openRawResource(R.raw.applist);

        if (fis != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tempString = "";
            for (int i = 0; i < strs.length; i++) {
                tempString = bufferedReader.readLine();
                strs[i] = tempString;
            }
            fis.close();
        }
        return strs;
    }
}
