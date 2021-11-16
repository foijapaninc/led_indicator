package com.foijapan.app.led_indicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    PackageManager pm = null;

    ListView listView1;
    static List<AppInfos> dataList = new ArrayList<AppInfos>();
    static PackageListAdapter adapter = null;

    int mNumOfCheckedApp = 0;
    ArrayList<String> mPns = new ArrayList<String>();

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

    @Override
    public void onResume(){
        super.onResume();
        Button btnApply = findViewById(R.id.id_btb_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumOfCheckedApp = 0;
                for(int i = 0; i < dataList.size();i++){
                    if(isCheckedBtn(i)){
                        mNumOfCheckedApp++;
                        addPackages(getCheckedPackageName(i));
                    }
                }
                SharedArrayList sal = new SharedArrayList(getApplicationContext());
                sal.clearArrayList(Common.LIST_OF_CHECKED_APP);

                Intent intent = new Intent();
                intent.putExtra(Common.LIST_OF_CHECKED_APP, (Serializable) mPns);
                sal.putStringArrayPref(Common.LIST_OF_CHECKED_APP,mPns);
                intent.setClass(getApplicationContext(),Pref.class);
                startActivity(intent);
            }
        });

        Button btnCancel = findViewById(R.id.id_btb_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addPackages(String pn){
        mPns.add(pn);
    }

    private String getCheckedPackageName(int position){
        return dataList.get(position).getPackageName();
    }

    private boolean isCheckedBtn(int position){
        boolean isChecked = dataList.get(position).isOn();
        return isChecked;
    }

    private String[] getAplicationList() throws IOException {
        AppList al = new AppList(getApplicationContext());
        return  al.readFromFile();
    }

    protected void updatePackageList() {
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

                SharedArrayList sal = new SharedArrayList(getApplicationContext());
                try {
                    ArrayList<String> pns = sal.getStringArrayPref(Common.LIST_OF_CHECKED_APP);
                    for(int i = 0;i < pns.size();i++){
                        String packagename = pns.get(i);
                        if(packagename.equals(aInfo.getPackageName())){
                            cb.setChecked(true);
                            continue;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aInfo.setIsOn(cb.isChecked());
//                        if (cb.isChecked()) {
//                            Toast.makeText(getApplicationContext(), aInfo.isOn + ":"+aInfo.getPackageName(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }
            return v;
        }
    }
}