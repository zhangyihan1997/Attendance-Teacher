package com.yukino.myapplication;

import android.content.Intent;

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



import com.yukino.http.UserHttpController;

public class Local extends AppCompatActivity{
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    EditText mText;
    private LocationManager lm;
    private static final String TAG = "Local";
    public String account;
    public static double lng;
    public static  double lat;


    String bestProvider;
    //get permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG,"onRequestPermissionsResult");

    }
    //destroy location information if exit
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lm != null) {
            lm.removeGpsStatusListener(listener);
        }
    }

    //Location monitoring
    public LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateView(location, account);
            Log.e(TAG, "time：" + location.getTime());
            Log.e(TAG, "longitude：" + location.getLongitude());
            Log.e(TAG, "latitude：" + location.getLatitude());
        }

        /**
         * Triggered when GPS status changes
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG,"onStatusChanged status=" + status);
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "The current GPS status is visible");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "The current GPS status is out of service area");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "The current GPS status is suspended");
                    break;
            }
        }

        /**
         * Triggered when GPS is on
         */
        public void onProviderEnabled(String provider) {
            Log.e(TAG,"onProviderEnabled");
            if (Build.VERSION.SDK_INT >= 23) {
                int checkPermission = ContextCompat.checkSelfPermission(Local.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Local.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Log.d("T", "Pop-up prompt");
                }
            }

            Location location = lm.getLastKnownLocation(provider);
            updateView(location, account);

        }

        /**
         * Triggered when GPS is disabled
         */
        public void onProviderDisabled(String provider) {
            updateView(null, account);
        }

    };

    //Status monitoring
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            Log.e(TAG,"onGpsStatusChanged event =" + event);
            switch (event) {
                //First positioning
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "first location");
                    break;
                //Satellite status change
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
                Log.d("T", "Pop-up prompt");
            }
        }
        //Setting query conditions for acquiring geographic location information
        bestProvider = lm.getBestProvider(getCriteria(), true);

        Location location;

        location = lm.getLastKnownLocation(bestProvider);

        updateView(location, account);
        //Listening state
        lm.addGpsStatusListener(listener);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_local);
        initView();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private void initView() {
        mText = (EditText) this.findViewById(R.id.showresult);
    }

    /**
     *
     *
     * @param location
     */
    //update location info to the textview and send to the teacher location database
    private void updateView(Location location, String account) {
        if (location != null) {
            mText.setText("Location：\n\nLongitude：");
            lng = location.getLongitude();
            mText.append(String.valueOf(location.getLongitude()));
            lat = location.getLatitude();
            mText.append("\nLatitude：");
            mText.append(String.valueOf(location.getLatitude()));
            //use sendteacherlocation method
            UserHttpController.SendTeacherLocation(account, String.valueOf(location.getLongitude()),
                    String.valueOf(location.getLatitude()), String.valueOf(location.getTime()), new UserHttpController.UserHttpControllerListener() {
                        @Override
                        public void success() {
                            Toast.makeText(Local.this, "succeed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            Toast.makeText(Local.this, "uploading location",Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            //clear EditText
            mText.getEditableText().clear();
        }
    }

    /**
     * Return to query criteria
     *
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //Setting the positioning accuracy
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //Set whether speed is required
        criteria.setSpeedRequired(false);
        //Set whether operators are allowed to charge
        criteria.setCostAllowed(false);
        //Set whether orientation information is required
        criteria.setBearingRequired(false);
        //Set whether altitude information is required
        criteria.setAltitudeRequired(false);
        //Set the power demand
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}
