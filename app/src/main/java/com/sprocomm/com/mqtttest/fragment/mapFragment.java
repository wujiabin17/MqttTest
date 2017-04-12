package com.sprocomm.com.mqtttest.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.sprocomm.com.mqtttest.CaptureActivity;
import com.sprocomm.com.mqtttest.MainActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.utils.AMapUtil;
import com.sprocomm.com.mqtttest.utils.CoordinateUtil;
import com.sprocomm.com.mqtttest.utils.OffLineMapUtils;
import com.sprocomm.com.mqtttest.utils.ToastUtil;
import com.sprocomm.com.mqtttest.utils.WalkRouteOverlay;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements View.OnClickListener, LocationSource, RouteSearch.OnRouteSearchListener, GeocodeSearch.OnGeocodeSearchListener, AMapLocationListener, AMap.OnMarkerClickListener {


    private MapView mMapView;
    private Button btLocation;
    private ImageView btRefrest;
    private AMap aMap;
    private UiSettings mUiSettings;
    private Context mContext;
    private Toast mToast;
    private RouteSearch routeSearch;
    private Circle circle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mMapLocation;
    private MarkerOptions markerOption;
    private Marker marker;
    private LatLng LatLngMarker;
    private String myTopic = "test-iot-service";
    private String myTopic1 = "abc";
    private MqttConnectOptions options;
    private Marker screenMarker = null;
    private String host = "ssl://androidtest.mqtt.iot.gz.baidubce.com:1884";
    private String userName = "androidtest/test01";
    private String passWord = "3vx/JpnTxCx8ZXe/g/yZT8rKeZDkw9A01U83if46aZk=";
    private int[] randData = new int[100];
    private static final int RESULT_FROM_CAPTURE_ACTIVITY = 1;
    private static final String RETURN_BYCLE_ID = "return_bycle_id";
    private static final String CONNECT_IP = "112.64.126.122";
    private static final int CONNECT_PORT = 7088;
    private int index = 0;
    private int mProgressStatus = 0;
    private MqttClient client;
    private ExecutorService mExecutorService;
    private ScheduledExecutorService scheduler;
    private LinearLayout llBikeConfig;
    private String androidId = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                final String mess = msg.obj.toString();
                String bycleId = null;
                String cmd = null;
                if (index == 100) {
                    index = 0;
                }
                if (mess.startsWith("#555,")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String afterLocation = mess.substring(5);
                            int index1 = afterLocation.indexOf(',');
                            int index2 = afterLocation.indexOf('&');
                            String lat = afterLocation.substring(0, index1);
                            String lng = afterLocation.substring(index1 + 1, index2);
                            double parseLat = Double.parseDouble(lat);
                            double parseLng = Double.parseDouble(lng);
                            for (int i = 1; i < 3; i++) {
                                double saveLat = -0.0005 * i + parseLat;
                                double saveLng = 0.0009 * i + parseLng;
                                MqttMessage message = new MqttMessage();
                                String roundPosition = "#333," + saveLat + "," + saveLng + "&&";
                                message.setPayload(roundPosition.getBytes());
                                try {
                                    client.publish("abc", message);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int j = 1; j < 3; j++) {
                                double saveLat = 0.001 * j + parseLat;
                                double saveLng = -0.001 * j + parseLng;
                                MqttMessage message = new MqttMessage();
                                String roundPosition = "#333," + saveLat + "," + saveLng + "&&";
                                message.setPayload(roundPosition.getBytes());
                                try {
                                    client.publish("abc", message);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else if (mess.startsWith("#333,")) {
                    String afterBycleLocation = mess.substring(5);
                    int index = afterBycleLocation.indexOf(',');
                    int index2 = afterBycleLocation.indexOf('&');
                    String lat = afterBycleLocation.substring(0, index);
                    String lng = afterBycleLocation.substring(index + 1, index2);
                    double parseLat = Double.parseDouble(lat);
                    double parseLng = Double.parseDouble(lng);
                    LatLng latLng = new LatLng(parseLat, parseLng);
                    addMarkersToMap(latLng);
                }
            } else if (msg.what == 2) {
                System.out.println("连接成功");
                Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                try {
                    client.subscribe(myTopic, 1);
                    client.subscribe(myTopic1, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 3) {
                Toast.makeText(mContext, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                System.out.println("连接失败，系统正在重连");
            }
        }
    };
    private TextView tv_money;
    private TextView tv_distance;
    private TextView tv_minute;
    private TextView tv_location;
    private SharedPreferences mPfs;
    private Socket mSocket = null;
    private Button btnScan;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.show_map_layout, container, false);
        initView(view);
        mMapView.onCreate(savedInstanceState);
        initEvent();
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startReconnect();
            }
        }).start();
        return view;
    }

    private void initEvent() {
        btLocation.setOnClickListener(this);
        btRefrest.setOnClickListener(this);
        mPfs = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void initView(View view) {
        mMapView = (MapView) view.findViewById(R.id.map);
        btLocation = (Button) view.findViewById(R.id.bt_location);
        btRefrest = (ImageView) view.findViewById(R.id.iv_refresh);
        tv_money = (TextView) view.findViewById(R.id.tv_money_num);
        tv_distance = (TextView)view.findViewById(R.id.tv_distance);
        tv_minute = (TextView)view.findViewById(R.id.tv_go_time);
        tv_location =(TextView)view.findViewById(R.id.location_name);
        llBikeConfig = (LinearLayout) view.findViewById(R.id.ll_title2);
        llBikeConfig.setVisibility(View.GONE);
        btnScan = (Button) view.findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        aMap = mMapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.gps_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        mUiSettings = aMap.getUiSettings();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        MapsInitializer.sdcardDir = OffLineMapUtils.getSdCacheDir(getContext());
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });
        // 设置可视范围变化时的回调的接口方法
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {
                //屏幕中心的Marker跳动
                startJumpAnimation();
            }
        });
        routeSearch = new RouteSearch(mContext);
        routeSearch.setRouteSearchListener(this);
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }
    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng) {
        Log.i("simon", "--------------latlng:" + latLng);
        aMap.setOnMarkerClickListener(this);
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .visible(true)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin)));
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_location:
                llBikeConfig.setVisibility(View.GONE);
                if (mMapLocation != null) {
                    LatLng latLng = new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude());
                    setUpMap();
                    String markerPosition = "#555," + latLng.latitude + "," + latLng.longitude + "&&";
                    MqttMessage message1 = new MqttMessage();
                    message1.setPayload(markerPosition.getBytes());
                    try {
                        client.publish("abc", message1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                } else {
                    ToastUtil.show(mContext, "定位不成功");
                }
                break;
            case R.id.iv_refresh:
                aMap.clear();
                llBikeConfig.setVisibility(View.GONE);
                addMarkerInScreenCenter();
                break;

            case R.id.btn_scan:
                startActivity(new Intent(mContext, CaptureActivity.class));
                break;
            default:
                break;
        }
    }

    private void init() {
        try {
            if (androidId != null) {
                client = new MqttClient(host, androidId,
                        new MemoryPersistence());
            } else {
                //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
                client = new MqttClient(host, androidId,
                        new MemoryPersistence());
            }
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message.toString();
                    handler.sendMessage(msg);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(mContext);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private int walkMode = RouteSearch.WALK_DEFAULT;
    private ProgressDialog progDialog;
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(mContext);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private WalkRouteResult mWalkRouteResult;

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    llBikeConfig.setVisibility(View.VISIBLE);
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            mContext, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String durTime = AMapUtil.getFriendlyTime(dur);
                    String disLength = AMapUtil.getFriendlyLength(dis);
                    tv_minute.setText(durTime);
                    tv_distance.setText(disLength);
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, errorCode);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    private GeocodeSearch geocoderSearch;
    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress();
                tv_location.setText(addressName);
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
    private void setUpMap() {
        LatLng alatLng =  new LatLng(mMapLocation.getLatitude(),mMapLocation.getLongitude());
        // 绘制一个圆形
        circle = aMap.addCircle(new CircleOptions().center(alatLng)
                .radius(350).strokeColor(Color.argb(10, 1, 1, 1))
                .fillColor(Color.argb(25, 1, 1, 1)).strokeWidth(1));
        //画虚线圆
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
                mMapLocation = amapLocation;
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                Log.d("simon", "amapLocation:" + amapLocation.getLatitude() + "," + amapLocation.getLongitude());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            final LatLng LatLngCurrent = new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude());
            if (Math.abs(LatLngCurrent.latitude - latLng.latitude) < 0.0015 ||
                    Math.abs(LatLngCurrent.longitude - latLng.longitude) < 0.0015) {
                return;
            }
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(mContext, 125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            TranslateAnimation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("ama", "screenMarker is null");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            LatLngMarker = marker.getPosition();
            LatLonPoint startPoint = new LatLonPoint(mMapLocation.getLatitude(),mMapLocation.getLongitude());
            LatLonPoint endPoint = new LatLonPoint(LatLngMarker.latitude,LatLngMarker.longitude);
            searchRouteResult(startPoint, endPoint);
        }
        return true;
    }

    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, walkMode);
        routeSearch.calculateWalkRouteAsyn(query);
        getAddress(endPoint);
    }

    /**
     * @param sourceLatLng:
     * @return
     */
    public LatLng fromGpsToAmap(LatLng sourceLatLng) {
        LatLng latLng = new LatLng(sourceLatLng.latitude, sourceLatLng.longitude);
        latLng = CoordinateUtil.transformFromWGSToGCJ(latLng);
        return latLng;
    }

    private LatLng parseLatLng(String source) {
        LatLng latLng = null;
        int fixLen = "**,101,A,YYMMDDHHMMSS,".length();
        //String comeServer = "**,101,A,160512140659,31.208258,121.629941&&";
        if (source.length() > fixLen) {
            String tmp = source.substring(fixLen);
            Log.i("simmon", "---------------:" + tmp);
            int index = tmp.indexOf(',');
            int index2 = tmp.indexOf('&');
            double longitude = Double.parseDouble(tmp.substring(0, index - 1));
            double latgitude = Double.parseDouble(tmp.substring(index + 1, index2 - 1));
            latLng = new LatLng(latgitude, longitude);
        }
        return latLng;
    }
    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what =3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

  /*  private void send(String msg) {
        if (mSocket != null) {
            try {
                OutputStream out = mSocket.getOutputStream();
                if (out != null) {
                    out.write(msg.getBytes());
                    out.flush();
                    return;
                } else {
                    Log.i("simon", "output stream null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        showErrorConnectToast();
    }
    private void showErrorConnectToast() {
        Toast.makeText(mContext, R.string.no_connect_to_server, Toast.LENGTH_SHORT).show();
    }
*/
    /*private void receiveMsg() {
        if (mSocket != null) {
            try {
                InputStream in = mSocket.getInputStream();
                if (in != null) {
                    byte[] buffer = new byte[1024];
                    int count = in.read(buffer);
                    if (count == 0) {
                        return;
                    }
                    String bufferToString = new String(buffer);
                    String realMessage = bufferToString.substring(0, count);
                    Log.d("wjb sprocomm", "realMessage:" + realMessage);
                    Message message = handler.obtainMessage();
                    message.obj = realMessage;
                    message.what = RECEIVE_FROM_SERVER;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connectSocket() {
        final String ip = CONNECT_IP;
        final int port = CONNECT_PORT;

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(InetAddress.getByName(ip), port);
                    if (mSocket.isConnected()) {
                        handler.sendEmptyMessage(BUTTON_STATE);
                        while (true) {
                            receiveMsg();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
*/
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.onDestroy();
    }

}
