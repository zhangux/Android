package com.qdzl.map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.qdzl.R;
import com.qdzl.util.ToastUtil;

/**
 * Created by QDZL on 2017/12/1.
 */
public class MapActivity extends Activity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    RadioGroup rgMapType;
    CheckBox cbTraffic;
    CheckBox cbHeat;
    //定位类型
    MyLocationConfiguration.LocationMode myLocationMode;
    //定位客户端
    LocationClient mLocClient;
    MyLocationListenner myListener = new MyLocationListenner();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        bind();
        init();
    }

    private void bind() {
        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();
        rgMapType = (RadioGroup) this.findViewById(R.id.rg_map_type);
        cbTraffic = (CheckBox) this.findViewById(R.id.rg_traffic);
        cbHeat = (CheckBox) this.findViewById(R.id.rg_heat);

        cbTraffic.setOnCheckedChangeListener(onCheckedChangeListenerCheck);
        cbHeat.setOnCheckedChangeListener(onCheckedChangeListenerCheck);
    }

    private void init() {
        rgMapType.setOnCheckedChangeListener(onCheckedChangeListener);

        myLocationMode = MyLocationConfiguration.LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
//        myLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;//定位跟随态
//        myLocationMode = MyLocationConfiguration.LocationMode.COMPASS;  //定位罗盘态


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);  //定位用到的一个类


        mLocClient.registerLocationListener(myListener);

        ///LocationClientOption类用来设置定位SDK的定位方式，
        LocationClientOption option = new LocationClientOption(); //以下是给定位设置参数
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    boolean isFirstLoc = true; // 是否首次定位

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);

            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.bd);
//        int accuracyCircleFillColor = 0xAAFFFF88;//自定义精度圈填充颜色
//        int accuracyCircleStrokeColor = 0xAA00FF00;//自定义精度圈边框颜色
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            MyLocationConfiguration config = new MyLocationConfiguration(myLocationMode, true, null);
            mBaiduMap.setMyLocationConfiguration(config);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    boolean rg_traffic = false;
    boolean rg_heat = false;
    CompoundButton.OnCheckedChangeListener onCheckedChangeListenerCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.rg_traffic:
                    if (rg_traffic) {
                        ToastUtil.show("关闭交通图");
                        rg_traffic = false;
                    } else {
                        ToastUtil.show("开启交通图");
                        rg_traffic = true;
                    }
//                    开启交通图
                    mBaiduMap.setTrafficEnabled(rg_traffic);
                    break;
                case R.id.rg_heat:
                    if (rg_heat) {
                        ToastUtil.show("关闭热力图");
                        rg_heat = false;
                    } else {
                        ToastUtil.show("开启热力图");
                        rg_heat = true;
                    }
//                    开启热力图
                    mBaiduMap.setBaiduHeatMapEnabled(rg_heat);
                    break;
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rg_normal:
//                    普通
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    break;
                case R.id.rg_satellite:
//                    卫星
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    break;
                case R.id.rg_withe:
//                    空白
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
