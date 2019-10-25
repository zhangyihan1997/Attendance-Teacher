package com.yukino.myapplication;


import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;


import android.os.Bundle;

import com.baidu.mapapi.map.MapStatus;

import com.baidu.mapapi.utils.CoordinateConverter;
import com.yukino.utils.RetrofitAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BaiDuMapActivity extends AppCompatActivity {

    /**
     * SDK
     */
    public LocationClient locationClient;
    /**
     * Location monitoring
     */
    public MyLocationListenner myListener = new MyLocationListenner();
    /**
     * Baidu map control
     */
    private MapView mapView;
    /**
     * Baidu map object
     */
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker,othersCurrentMarker;
    private BaiduMap baiduMap;
    private SensorManager mSensorManager;
    double degree = 0;
    // UI
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    ToggleButton togglebtn = null;
    boolean isFirstLoc = true; // First positioning or not

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bai_du_map);
        //Get Baidu map control
        judgePermission();
        mapView = (MapView) findViewById(R.id.bmapView);
        //Get Baidu map object
        baiduMap = mapView.getMap();
        // Turn on positioning layer
        baiduMap.setMyLocationEnabled(true);
        /**
         * Location initialization
         */
        //Declare and locate SDK core classes
        locationClient = new LocationClient(this);
        //Registered monitoring
        locationClient.registerLocationListener(myListener);
        //Location configuration information
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // open GPS
        option.setCoorType("bd09ll"); // Set coordinate type
        option.setScanSpan(1000);//Location request interval
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        //open GPS
        locationClient.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Content-Type", "application/json");
        //use getstudentlocation method to get student location from database
        Call<String> studentCall = service.getStudentLocation(header);

        studentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    String returnJson = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(returnJson);

                        if(jsonObject.getBoolean("status")) {
                            JSONArray locations = jsonObject.getJSONArray("locations");
                            StringBuilder sb = new StringBuilder();
                            double longtitude, latitude;
                            int attendance;
                            for (int i = 0; i < locations.length(); i++) {
                                //get attendance result and longitude and latitude for all students
                                    attendance = locations.getJSONObject(i).getInt("attendance");
                                    longtitude = locations.getJSONObject(i).getDouble("longitude");
                                    latitude = locations.getJSONObject(i).getDouble("latitude");
                                     LatLng dbPoint = new LatLng(latitude, longtitude);
                                     //transform the location info to Baidu MAP
                                     CoordinateConverter converter = new CoordinateConverter()
                                        .from(CoordinateConverter.CoordType.GPS)
                                        .coord(dbPoint);
                                     LatLng desLatLng = converter.convert();
                                     //if the attendance result is 0, show it in green point
                                    if (attendance == 0) {
                                        addOthersLocation1(desLatLng.longitude, desLatLng.latitude);
                                    }
                                    //else show it is red point
                                    else{
                                        addOthersLocation(desLatLng.longitude, desLatLng.latitude);
                                    }
                            }
                        } else {
                            Log.i("call failed", "diu lei lou mou");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("call failed", "Stack");
                    }
                } else {
                    Log.i("call failed", response.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.i("call failed", throwable.getMessage());
                //userHttpControllerListener.fail();
            }
        });
    }
    //use red point to show student location
    public void addOthersLocation(double longitude, double latitude) {


        //Define coordinate points
        LatLng point = new LatLng(latitude, longitude);
        //red point pattern
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //use it on map
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //Add to the map and display
        baiduMap.addOverlay(option);
    }
    //use green point to show student location
    public void addOthersLocation1(double longitude, double latitude) {
        //Define coordinate points
        LatLng point = new LatLng(latitude, longitude);
        //green point pattern
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka1);
        //use it on map
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //Add to the map and display
        baiduMap.addOverlay(option);
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Map view is not in the processing position after destruction
            BaiduMap mBaiduMap = mapView.getMap();
            if (location == null || mapView == null) {
                return;
            }
            LatLng dbPoint = new LatLng(Local.lat, Local.lng);
            CoordinateConverter converter = new CoordinateConverter()
                    .from(CoordinateConverter.CoordType.GPS)
                    .coord(dbPoint);
            LatLng desLatLng = converter.convert();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            LatLng point = new LatLng(desLatLng.latitude, desLatLng.longitude);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka2);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //show maker on MAP
            baiduMap.addOverlay(option);
            //baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //Define maker coordinate points
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());

                float f = mBaiduMap.getMaxZoomLevel();
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f - 2);
                mBaiduMap.animateMapStatus(u);
                //Map Location Display
                Toast.makeText(BaiDuMapActivity.this, location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // Destroy location if exit
        locationClient.stop();
        // Turn off anchor layer
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    protected void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check whether the permission has been obtained

            // sd card permission
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //Mobile status permission
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //Positioning permission
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted, prompt the user for the request
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }

        }else{
        }
    }

}
