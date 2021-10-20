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

        // 端末にインストール済のアプリケーション一覧情報を取得
        final PackageManager pm = getPackageManager();
        final int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;
        @SuppressLint("WrongConstant")
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(flags);

        // リストに一覧データを格納する
//        final List<AppData> dataList = new ArrayList<AppData>();
        for (ApplicationInfo app : installedAppList) {

            if (((app.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) &&
                    execludePackagename(app.packageName)) {
                continue;
            }
            AppData data = new AppData();
            data.label = app.loadLabel(pm).toString();
            data.icon = app.loadIcon(pm);
            data.pn = app.packageName;
            mDataList.add(data);
        }

//        Collections.sort(dataList, null);

        // リストビューにアプリケーションの一覧を表示する
        final ListView listView = new ListView(this);
        if(sAdapter == null){
            sAdapter = new AppListAdapter(this, mDataList);
        }
        listView.setAdapter(sAdapter);
        setContentView(listView);
    }

    static public void removeFromList(int num) {
        for( int i = 0;i < mDataList.size();i++){
            if(i == num){
                mDataList.remove(mDataList.get(i));
                // リストビューにアプリケーションの一覧を表示する
                final ListView listView = new ListView(sAdapter.getContext());
                if(sAdapter == null){
                    sAdapter = new AppListAdapter(sAdapter.getContext(), mDataList);
                }
                listView.setAdapter(sAdapter);
                sAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    static public void listupOnLaunchList(){
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
            holder.cb.setOnClickListener( v -> {
                int num = position;
                removeFromList(num);
                listupOnLaunchList();
            });

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView textLabel;
        ImageView imageIcon;
        CheckBox cb;
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