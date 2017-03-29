package com.sprocomm.com.mqtttest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String host = "ssl://androidtest.mqtt.iot.gz.baidubce.com:1884";
    private String userName = "androidtest/test01";
    private String passWord = "3vx/JpnTxCx8ZXe/g/yZT8rKeZDkw9A01U83if46aZk=";
    private int i = 1;
    private int[] randData = new int[100];
    private int index = 0;
    private int mProgressStatus = 0;
    private MqttClient client;

    private String myTopic = "test-iot-service";
    private String myTopic1 = "abc";
    private MqttConnectOptions options;

    private ScheduledExecutorService scheduler;

    private  Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String getId = msg.obj.toString();
                index =0;
                if(getId.contains(androidId)&& getId.contains("unlock")){
                    Toast.makeText(getApplicationContext(),"正在开锁中",Toast.LENGTH_LONG).show();
                    btLock.setText("正在开锁中....");
                    tvBar.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            super.run();
                            while(index < 100) {
                                doWork();
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                            }
                        }

                    }.start();
            }else if(getId.contains(androidId)&& getId.contains("lock")){
                    Toast.makeText(getApplicationContext(),"正在关锁中",Toast.LENGTH_SHORT).show();
                    btLock.setText("开锁");
                }
            } else if (msg.what == 2) {
                System.out.println("连接成功");
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                try {
                    client.subscribe(myTopic, 1);
                    client.subscribe(myTopic1, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 3) {
                Toast.makeText(MainActivity.this, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                System.out.println("连接失败，系统正在重连");
            }else if(msg.what == 4){
                mProgressStatus = index;
                //设置进度条当前的完成进度
                bar.setProgress(mProgressStatus);
                tvBar.setText(mProgressStatus +"/100%");
                if(index == 100){
                    bar.setVisibility(View.GONE);
                    tvBar.setVisibility(View.GONE);
                    mProgressStatus =0;
                    btLock.setText("关锁");
                    Toast.makeText(MainActivity.this, "开锁成功.可以骑车啦!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private Button btPublish;
    private EditText etMessage;
    private String androidId = null;
    private Button btLock;
    private ProgressBar bar;
    private TextView tvBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        initView();
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startReconnect();
            }
        }).start();
    }

    private void initView() {
        etMessage = (EditText)findViewById(R.id.et_message);
        btPublish = (Button) findViewById(R.id.publish);
        btLock = (Button) findViewById(R.id.lock_bycle);
        bar = (ProgressBar)findViewById(R.id.bar);
        tvBar = (TextView) findViewById(R.id.tv_bar);
        btPublish.setOnClickListener(this);
    }

    private int doWork() {
        index++;
        try {
        Thread.sleep(200);
        } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        return index;
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

    private void init() {
        try {
            if(androidId !=null){
                client = new MqttClient(host, androidId,
                        new MemoryPersistence());
            }else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (client != null && keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.publish:
                MqttMessage message= new MqttMessage();
                message.setPayload(etMessage.getText().toString().getBytes());
                try {
                    client.publish("abc",message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}