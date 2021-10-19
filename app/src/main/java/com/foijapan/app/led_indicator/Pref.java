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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Pref extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.pref);

        // 端末にインストール済のアプリケーション一覧情報を取得
        final PackageManager pm = getPackageManager();
        final int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;
        @SuppressLint("WrongConstant")
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(flags);

        // リストに一覧データを格納する
        final List<AppData> dataList = new ArrayList<AppData>();
        for (ApplicationInfo app : installedAppList) {

            if(((app.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) &&
                    execludePackagename(app.packageName)){
                continue;
            }
            AppData data = new AppData();
            data.label = app.loadLabel(pm).toString();
            data.icon = app.loadIcon(pm);
            dataList.add(data);
        }

//        Collections.sort(dataList, null);

        // リストビューにアプリケーションの一覧を表示する
        final ListView listView = new ListView(this);
        listView.setAdapter(new AppListAdapter(this, dataList));
        setContentView(listView);
    }

    // アプリケーションデータ格納クラス
    private static class AppData {
        String label;
        Drawable icon;
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 表示データを取得
            final AppData data = getItem(position);
            // ラベルとアイコンをリストビューに設定
            holder.textLabel.setText(data.label);
            holder.imageIcon.setImageDrawable(data.icon);

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView textLabel;
        ImageView imageIcon;
    }

    private boolean execludePackagename(String pn){
        boolean ret = true;
        if(("com.google.android.youtube".equals(pn))||
           ("jp.co.nttdocomo.carriermail".equals(pn)) ||
           ("com.nttdocomo.android.msg".equals(pn))  ||
           ("com.google.android.gm").equals(pn)){
            ret = false;
        }
        return ret;
    }
}