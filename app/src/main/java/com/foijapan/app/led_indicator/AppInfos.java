package com.foijapan.app.led_indicator;

import android.graphics.drawable.Drawable;

public class AppInfos {
    String pname;
    String label;
    Drawable drawable;
    boolean isOn;

    public AppInfos(
            String pname,
            String label,
            Drawable drawable,
            boolean isOn){
        this.pname = pname;
        this.label = label;
        this.drawable = drawable;
        this.isOn = isOn;
    }

    public String getPackageName(){
        return this.pname;
    }

    public String getLabel(){
        return this.label;
    }

    public Drawable getDrawable(){
        return this.drawable;
    }

    public boolean isOn(){
        return this.isOn;
    }
}