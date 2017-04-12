package com.sprocomm.com.mqtttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.sprocomm.com.mqtttest.fragment.MapFragment;
import com.sprocomm.com.mqtttest.utils.ToastUtil;
import com.sprocomm.com.mqtttest.widget.SecondTopBar;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String FRAGMENT_CONTENT = "fragment_content";
    private ImageButton MenuImage;
    private ImageButton MenuSetting;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private SecondTopBar mTopBar;
    private static final int RESULT_FROM_CAPTURE_ACTIVITY = 1;
    private static final String RETURN_BYCLE_ID = "return_bycle_id";
    private static final String CONNECT_IP = "112.64.126.122";
    private static final int CONNECT_PORT = 7088;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initFragment();
    }
    private void initFragment() {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();// 开启事务
        transaction.replace(R.id.contanier_map, new MapFragment(),
                FRAGMENT_CONTENT);
        transaction.commit();// 提交事务
        // Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
    }
    private void initView() {
        mTopBar = (SecondTopBar) findViewById(R.id.second_top_bar);
    }
    private void initEvent(){
        mTopBar.setOnMenuOnListener(this);
        mTopBar.setOnSettingListener(this);
    }
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_FROM_CAPTURE_ACTIVITY:
                if (data == null) {
                    return;
                }
                String returnBycleId = data.getStringExtra(RETURN_BYCLE_ID);
                if (returnBycleId != null) {
                    if (returnBycleId.length() == 15) {
                        send("##" + returnBycleId+ ",202&&");
                    } else {
                        ToastUtil.show(mContext,"验证码错误,请重新扫码");
                    }
                }
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                Intent menu_intent = new Intent(this,MenuActivity.class);
                startActivity(menu_intent);
                break;
            case R.id.btn_settings:
                Intent setting_intent = new Intent(this,AccountSettingActivity.class);
                startActivity(setting_intent);
                break;
            default:
                break;
        }
    }


    public MapFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment fragment = (MapFragment) fm
                .findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}