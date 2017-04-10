package com.sprocomm.com.mqtttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.sprocomm.com.mqtttest.fragment.MapFragment;
import com.sprocomm.com.mqtttest.widget.SecondTopBar;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String FRAGMENT_CONTENT = "fragment_content";
    private ImageButton MenuImage;
    private ImageButton MenuSetting;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private SecondTopBar mTopBar;

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