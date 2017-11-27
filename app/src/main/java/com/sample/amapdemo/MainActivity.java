package com.sample.amapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnCameraChangeListener, AMap.OnMapTouchListener {

    private AMap amap;
    private MapView mapView;
    private GroundOverlay groundoverlay;
    private List<GroundOverlay> groundoverlayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写

        if (amap == null) {
            amap = mapView.getMap();
            amap.setOnCameraChangeListener(this);
            amap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            amap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            amap.setOnMapTouchListener(this);
//            amap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            addOverlayToMap();
        }
    }

    /**
     * 往地图上添加一个groundoverlay覆盖物
     */
    private void addOverlayToMap() {
        amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.936713,
                116.386475), 15));// 设置当前地图显示为北京市恭王府

//        amap.moveCamera(CameraUpdateFactory.newCameraPosition(
//                new CameraPosition(new LatLng(39.936713, 116.386475), 18, 30, 0)));// 设置当前地图显示为北京市恭王府
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(39.935029, 116.384377))
                .include(new LatLng(39.939577, 116.388331)).build();

        LatLngBounds bounds2 = new LatLngBounds.Builder()
                .include(new LatLng(39.935029, 116.388331))
                .include(new LatLng(39.939577, 116.392285)).build();

        groundoverlayList.add(amap.addGroundOverlay(new GroundOverlayOptions()
                .zIndex(15)
                .anchor(0.5f, 0.5f)
                .transparency(0.0f)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.overlay))
                .positionFromBounds(bounds)));
        groundoverlayList.add(amap.addGroundOverlay(new GroundOverlayOptions()
                .zIndex(16)
                .anchor(0.5f, 0.5f)
                .transparency(0.0f)
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .positionFromBounds(bounds)));
        groundoverlayList.add(amap.addGroundOverlay(new GroundOverlayOptions()
                .zIndex(15)
                .anchor(0.5f, 0.5f)
                .transparency(0.0f)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.overlay))
                .positionFromBounds(bounds2)));
        groundoverlayList.add(amap.addGroundOverlay(new GroundOverlayOptions()
                .zIndex(16)
                .anchor(0.5f, 0.5f)
                .transparency(0.0f)
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .positionFromBounds(bounds2)));

        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(39.935346, 116.381313)).title("38便利店")
                .snippet("到这里去").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true).period(50);
        amap.addMarker(markerOption);
    }

    /**
     * 方法必须重写
     */
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.i("msg","CHANGE " +  cameraPosition.toString());
        int zIndex = (int) cameraPosition.zoom;
        for (int i = 0; i < groundoverlayList.size(); i++) {
            GroundOverlay groundOverlay = groundoverlayList.get(i);
            groundOverlay.setVisible(groundOverlay.getZIndex() == zIndex);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.i("msg", "FINISH " + cameraPosition.toString());
        if (isActionUp) {
            amap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.936713,
                    116.386475), 15));// 设置当前地图显示为北京市恭王府
            isActionUp = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place:
                Log.i("msg", "place");
                break;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i("msg", "点击了窗口");
        startActivity(new Intent(this, GPSNaviActivity.class));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private boolean isActionUp = false;

    @Override
    public void onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("msg", "isActionUp false");
                isActionUp = false;
                break;
            case MotionEvent.ACTION_UP:
                Log.i("msg", "isActionUp true");
                isActionUp = true;
                break;
        }
    }
}