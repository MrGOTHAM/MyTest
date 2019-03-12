package comt.example.dell_pc.test.Pagers.Three;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import comt.example.dell_pc.test.R;
import java.util.ArrayList;
import java.util.List;

//AMpLocationListener   定位回调监听器
public class ThreeFragment extends Fragment implements LocationSource, AMapLocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SendFragment";

    private String mParam1;
    private String mParam2;

    private View contentView;
    private MapView mapView;

    private AMap aMap;
    //定位蓝点
    MyLocationStyle myLocationStyle;

    //定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    //经纬度
    String localLatitude;
    String localLongtitude;

    List<MarkerOptions> markerOptions1= new ArrayList<>();
    List<Marker> markers = new ArrayList<>();


    public ThreeFragment() {
        // Required empty public constructor
    }

    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            Log.i("sys", "MF onCreateView() null");
            contentView = inflater.inflate(R.layout.fragment_three, container, false);
            mapView = contentView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            if (aMap == null) {
                aMap = mapView.getMap();
                //设置地图缩放0~20，这里写的14
                aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
//                LatLng latLng = new LatLng(39.906901,116.397972);
//                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
            }
            location();
            addMaker();
        } else {
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
        }
        location();
        return contentView;
    }

    //显示定位
    private void location(){
        //显示定位
        aMap.getUiSettings().setZoomControlsEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //蓝点初始化
        // 初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒,最小为1000.
        myLocationStyle.interval(2000);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置默认定位按钮是否显示，非必需设置
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
                Log.i(TAG, "onMyLocationChange: Longitude["+location.getLongitude()+"]"+",Latitude["+location.getLatitude()+"]s");
            }
        });
    }
    //添加标记
    private void addMaker(){
//
//		LatLng latLng = new LatLng(39.906901,116.397972);
//		final Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));

//		//自定义标记
//		MarkerOptions markerOption = new MarkerOptions();
//		markerOption.position(latLng);
//		markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//		markerOption.draggable(true);//设置Marker可拖动
//		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//				.decodeResource(getResources(),R.drawable.ic_add_shopping_cart_black_24dp)));
//		// 将Marker设置为贴地显示，可以双指下拉地图查看效果
//		markerOption.setFlat(true);//设置marker平贴地图效果
//		final Marker marker2 = aMap.addMarker(markerOption);

//		//动画
//		Animation animation = new RotateAnimation(marker2.getRotateAngle(),marker2.getRotateAngle()+180,0,0,0);
//		long duration = 1000L;
//		animation.setDuration(duration);
//		animation.setInterpolator(new LinearInterpolator());
//
//		marker2.setAnimation(animation);
//		marker2.startAnimation();
//
    }


    @Override
    //AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，回调参数是AMapLocation。
    public void onLocationChanged(AMapLocation aMapLocation) {//实现了SendFragment所继承的接口AMapLocationListener
        if (mListener != null && aMapLocation != null) {
            //判断AMapLocation对象不为空 ， aMapLocation的错误代码返回为0 时 定位成功
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                //在此中解析aMapLocation获取相应内容
                // 显示系统小蓝点
                mListener.onLocationChanged(aMapLocation);
                //经纬度
                localLatitude = String.valueOf(aMapLocation.getLatitude());
                localLongtitude = String.valueOf(aMapLocation.getLongitude());
            } else {
                //定位失败，getErrorCode()获取错误码信息，确定失败原因，errorInfo()是错误信息，可在错误码表中查看
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("定位AmapErr", errText);
            }
        }
    }

    @Override
    //活动
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getActivity());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);

            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    //停止活动
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            //停止定位后，本地定位服务并不会被销毁
            mlocationClient.stopLocation();
            //销毁定位客户端，同时销毁本地定位服务
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
}
