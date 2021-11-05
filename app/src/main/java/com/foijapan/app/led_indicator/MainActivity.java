package com.foijapan.app.led_indicator;

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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    PackageManager pm = null;

    ListView listView1;
    static List<AppInfos> dataList = new ArrayList<AppInfos>();
    static PackageListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        pm = getApplicationContext().getPackageManager();

        listView1 = (ListView) findViewById(R.id.listView1);

        adapter = new PackageListAdapter();
        listView1.setAdapter(adapter);

        updatePackageList();
    }

    private String[] getAplicationList() throws IOException {
        AppList al = new AppList(getApplicationContext());
        return  al.readFromFile();
    }

    protected void updatePackageList(){
        // 端末にインストール済のアプリケーション一覧情報を取得
        final PackageManager pm = getPackageManager();
        final int flags = PackageManager.GET_META_DATA;
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(flags);
        String[] strs = null;

        dataList.clear();

        for (ApplicationInfo app : installedAppList) {
            boolean ret = false;
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM &&
                !(app.packageName.equals("com.google.android.gm"))){
                continue;
            }

            try {
                if (strs == null) {
                    strs = getAplicationList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (strs.length < 1) {
                Toast.makeText(getApplicationContext(), getString(R.string.filenotfound), Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < strs.length; i++) {
                ret = strs[i].equals(app.packageName);
                if (ret) {
                    String label = app.loadLabel(pm).toString();
                    Drawable drawable = app.loadIcon(pm);
                    String packagename = app.packageName;
                    dataList.add(new AppInfos(packagename, label, drawable, false));
                }
            }
        }
    }

    private class PackageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            View v = convertView;
            TextView textView1;
            ImageView imageView1;
            CheckBox cb;
            if (v == null) {
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.list, null);
            }

            AppInfos aInfo = (AppInfos) getItem(position);
            if (aInfo != null) {
                textView1 = v.findViewById(R.id.label);
                imageView1 = v.findViewById(R.id.icon);
                cb = v.findViewById(R.id.id_pref_cb);

                textView1.setText(aInfo.getLabel());
                imageView1.setImageDrawable(aInfo.getDrawable());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cb.isChecked()) {
                            Toast.makeText(getApplicationContext(), aInfo.getPackageName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            return v;
        }
    }
}