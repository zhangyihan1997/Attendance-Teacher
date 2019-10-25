package com.yukino.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Iterator;
public class Local extends AppCompatActivity{
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    EditText mText;
    private LocationManager lm;
    private static final String TAG = "Local";
    String bestProvider;
    String provider;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG,"onRequestPermissionsResult");

        //requestpermisiion result , permission
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lm != null) {
            lm.removeGpsStatusListener(listener);
        }
    }

    //位置监听
    public LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateView(location);
            Log.e(TAG, "时间：" + location.getTime());
            Log.e(TAG, "经度：" + location.getLongitude());
            Log.e(TAG, "纬度：" + location.getLatitude());
            Log.e(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG,"onStatusChanged status=" + status);
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            Log.e(TAG,"onProviderEnabled");
            if (Build.VERSION.SDK_INT >= 23) {
                int checkPermission = ContextCompat.checkSelfPermission(Local.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Local.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Log.d("TTTT", "弹出提示");
                }
            }

            Location location = lm.getLastKnownLocation(provider);
            updateView(location);

        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateView(null);
        }

    };

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            Log.e(TAG,"onGpsStatusChanged event =" + event);
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                //卫星状态改变
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(Local.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Local.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(Local.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.d("TTTT", "弹出提示");
            }
        }
        //为获取地理位置信息时设置查询条件
        bestProvider = lm.getBestProvider(getCriteria(), true);
        //获取位置信息
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location;

        location = lm.getLastKnownLocation(bestProvider);
        updateView(location);
        //监听状态
        lm.addGpsStatusListener(listener);
        //绑定监听，有4个参数
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        //参数4，监听
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_attendance);
        initView();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private void initView() {
        mText = (EditText) this.findViewById(R.id.showresult);
    }

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            mText.setText("Location：\n\nLongitude：");
            mText.append(String.valueOf(location.getLongitude()));
            mText.append("\nLatitude：");
            mText.append(String.valueOf(location.getLatitude()));
        } else {
            //清空EditText对象
            mText.getEditableText().clear();
        }
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}
