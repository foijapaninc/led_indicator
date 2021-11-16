package com.foijapan.app.led_indicator;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SharedArrayList {

    private Context mCtx = null;

    SharedArrayList(Context context){
        mCtx = context;
    }

    public void putStringArrayPref(String key, ArrayList<String> values) {
        SharedPreferences prefs = mCtx.getSharedPreferences(Common.PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public ArrayList<String> getStringArrayPref(String key) throws JSONException {
        SharedPreferences prefs = mCtx.getSharedPreferences(Common.PREF,Context.MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            JSONArray a = new JSONArray(json);
            for (int i = 0; i < a.length(); i++) {
                String url = a.optString(i);
                urls.add(url);
            }
        }
        return urls;
    }

    public void clearArrayList(String key){
        SharedPreferences prefs = mCtx.getSharedPreferences(Common.PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.apply();
    }
}