package com.foijapan.app.led_indicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pref extends Activity {
    static private List<AppData> mDataList = new ArrayList<AppData>();
    static private AppListAdapter sAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pref);

        getInstalledApplication(Common.getNum());

        // リストビューにアプリケーションの一覧を表示する
        final ListView listView = new ListView(this);
        if (sAdapter == null) {
            sAdapter = new AppListAdapter(this, mDataList);
        }
        listView.setAdapter(sAdapter);
        setContentView(listView);
    }

    private void getInstalledApplication(int cntmax){
        String strs[] = new String[Common.getNum()];
        AppList al = new AppList(getApplicationContext());
        try {
            strs = al.readFromFile(cntmax);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appInfoList
                = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo appInfo : appInfoList){
            String pn = appInfo.packageName;
            for (int i = 0; i < cntmax; i++) {
//                if (pn.equals(strs[i])) {
                  if (pn.contains("google")) {
                    AppData data = new AppData();
                    data.label = appInfo.loadLabel(pm).toString();
                    data.icon = appInfo.loadIcon(pm);
                    data.pn = appInfo.packageName;
                    mDataList.add(data);
                }
            }
        }
    }

    // アプリケーションデータ格納クラス
    private static class AppData {
        String label;
        Drawable icon;
        String pn;
    }

    // アプリケーションのラベルとアイコンを表示するためのアダプタークラス
    private static class AppListAdapter extends ArrayAdapter<AppData> {

        private final LayoutInflater mInflater;

        public AppListAdapter(Context context, List<AppData> dataList) {
            super(context, R.layout.pref);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            addAll(dataList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.pref, parent, false);
                holder.textLabel = convertView.findViewById(R.id.label);
                holder.imageIcon = convertView.findViewById(R.id.icon);
                holder.cb = convertView.findViewById(R.id.id_pref_cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 表示データを取得
            final AppData data = getItem(position);
            // ラベルとアイコンをリストビューに設定
            holder.textLabel.setText(data.label);
            holder.imageIcon.setImageDrawable(data.icon);
            holder.cb.setOnClickListener(v -> {
                int num = position;
            });

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView textLabel;
        ImageView imageIcon;
        CheckBox cb;
    }

}
