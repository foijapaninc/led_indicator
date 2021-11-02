package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pref extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);

        // 端末にインストール済のアプリケーション一覧情報を取得
        final PackageManager pm = getPackageManager();
        final int flags = PackageManager.GET_META_DATA;
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(flags);
        String[] strs = null;

        // リストに一覧データを格納する
        final List<AppData> dataList = new ArrayList<AppData>();
        for (ApplicationInfo app : installedAppList) {
            boolean ret = false;
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                continue;
            }

            try {
                if(strs == null) {
                    strs = getAplicationList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(strs.length < 1){
                Toast.makeText(getApplicationContext(), getString(R.string.filenotfound), Toast.LENGTH_SHORT).show();
                return;
            }

            for(int i = 0;i < strs.length;i++){
                ret = strs[i].equals(app.packageName);
                if(ret){
                    AppData data = new AppData();
                    data.label = app.loadLabel(pm).toString();
                    data.icon = app.loadIcon(pm);
                    dataList.add(data);
                }
            }
        }

        // リストビューにアプリケーションの一覧を表示する
        final ListView listView = new ListView(this);
        listView.setAdapter(new AppListAdapter(this, dataList));
        setContentView(listView);
    }

    private String[] getAplicationList() throws IOException {
        AppList al = new AppList(getApplicationContext());
        return  al.readFromFile();
    }

    // アプリケーションデータ格納クラス
    private static class AppData {
        String label;
        Drawable icon;
//        String pname;
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
//                holder.packageName = convertView.findViewById(R.id.pname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 表示データを取得
            final AppData data = getItem(position);
            // ラベルとアイコンをリストビューに設定
            if( holder.textLabel != null) {
                holder.textLabel.setText(data.label);
                if(data.label.startsWith("Tether")){
                    holder.textLabel.setText(data.label);
                }
                holder.imageIcon.setImageDrawable(data.icon);
//                holder.packageName.setText(data.pname);
            }

            return convertView;
        }
    }

    // ビューホルダー
    private static class ViewHolder {
        TextView textLabel;
        ImageView imageIcon;
//        TextView packageName;
    }
}