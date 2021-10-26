package com.foijapan.app.led_indicator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppList {
    Context mCtx = null;

    AppList(Context context){
        mCtx = context;
    }
    public String[] readFromFile(int cntmax) throws IOException {
        String strs[] = new String[Common.getNum()];
        InputStream inputStream = mCtx.openFileInput(Common.FILEPATH);

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tempString = "";
            for (int i = 0; i < cntmax; i++) {
                tempString = bufferedReader.readLine();
                strs[i] = tempString;
            }
            inputStream.close();
        }
        return strs;
    }
}
