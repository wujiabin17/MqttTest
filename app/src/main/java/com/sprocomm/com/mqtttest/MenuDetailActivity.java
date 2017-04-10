package com.sprocomm.com.mqtttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.sprocomm.com.mqtttest.fragment.menu_detail.InviteFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.MyMessageFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail.DepositFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail.MoneyDetailFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail.MoneyRefundFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail.MyMoneyFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail.TopUpFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.perferential.PerferentialFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.SettingsFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.trip_fragment.NeedHelpFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.trip_fragment.TripFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.UserGuideFragment;
import com.sprocomm.com.mqtttest.fragment.menu_detail.perferential.UseInstructionsFragment;
import com.sprocomm.com.mqtttest.widget.ThirdTopBar;

public class MenuDetailActivity extends FragmentActivity implements View.OnClickListener {
    private static final int ENTER_FIRST = 1;
    private static final int MY_MONKEY = 2;
    private static final int MY_PREFERENTIAL = 3;
    private static final int MY_TRIP= 4;
    private static final int MY_MESSAGE =5;
    private static final int INVITE_FRIENDS = 6;
    private static final int USER_GUIDE = 7;
    private static final int MY_SETTINGS = 8;
    private static final int MY_MONEY_DETAIL = 9;
    private static final int MY_MONEY_REFUND_INSTRUCTIONS = 10;
    private static final int MY_PREFERENTIAL_USE_INSTRUCTIONS = 11;
    private static final int MY_TRIP_NEED_HELP = 12;
    private static final int MY_DEPOSIT = 13;
    private static final int MY_TOP_UP = 14;
    public static final String TAG_MONEY = "money";
    private static final String TAG_MONEY_DETAIL = "money_detail";
    private static final String TAG_MONEY_REFUND_INSTRUCTIONS = "money_refund_instructions";
    private static final String TAG_PREFERENTIAL_USE_INSTRUCTIONS = "perferentel_use_instructions";
    private static final String TAG_TRIP_NEED_HELP = "trip_need_help";
    private static final String TAG_PREFERENTIAL= "perferential";
    private static final String TAG_TRIP = "trip";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_INVITE = "invite";
    private static final String TAG_USER_GUIDE= "user_guide";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_DEPOSIT = "deposit";
    private static final String TAG_TOP_UP  ="top_up";
    private static final String ENTER_KEY = "detail_menu";
    private ThirdTopBar mTopBar;
    private FragmentManager fm;
    private int enterFlag;
    private Fragment currentFra;
    private String currentTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        initView();
        fm = getSupportFragmentManager();
        enterFlag = getIntent().getIntExtra(ENTER_KEY, ENTER_FIRST);
        if(enterFlag  == 1){
            return;
        }
        if (enterFlag == MY_MONKEY) {
            mTopBar.setVisibility(View.VISIBLE);
            currentFra = new MyMoneyFragment();
            currentTag = TAG_MONEY;
        } else if (enterFlag == MY_PREFERENTIAL) {
            mTopBar.setVisibility(View.VISIBLE);
            currentFra = new PerferentialFragment();
            currentTag = TAG_PREFERENTIAL;
        } else if (enterFlag == MY_TRIP) {
            mTopBar.setVisibility(View.VISIBLE);
            currentFra = new TripFragment();
            currentTag = TAG_TRIP;
        }else if (enterFlag == MY_MESSAGE) {
            currentFra = new MyMessageFragment();
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.setTvTitle("我的消息");
            mTopBar.setInfoVisibility(false);
            currentTag = TAG_MESSAGE;
        }else if (enterFlag == INVITE_FRIENDS) {
            currentFra = new InviteFragment();
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.setTvTitle("邀请好友");
            mTopBar.setInfoVisibility(false);
            currentTag = TAG_INVITE;
        }else if (enterFlag == USER_GUIDE) {
            currentFra = new UserGuideFragment();
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.setTvTitle("用户指南");
            mTopBar.setInfoVisibility(false);
            currentTag = TAG_USER_GUIDE;
        }else if (enterFlag == MY_SETTINGS) {
            currentFra = new SettingsFragment();
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.setTvTitle("设置");
            mTopBar.setInfoVisibility(false);
            currentTag = TAG_SETTINGS;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_detail, currentFra, currentTag);
        transaction.addToBackStack(currentTag);
        transaction.commit();
    }

    private void initView() {
        mTopBar = (ThirdTopBar) findViewById(R.id.third_top_bar);
        mTopBar.setOnBackOnListener(this);
        mTopBar.setOnInfoListener(this);
    }
    public void goMyMoney() {
        Fragment fragment = fm.findFragmentByTag(TAG_MONEY);
        if (fragment == null) {
            fragment = new MyMoneyFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        mTopBar.setTvTitle("退款说明");
        mTopBar.setInfoVisibility(false);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_MONEY);
        ft.addToBackStack(TAG_MONEY);
        ft.commit();
    }

    public void goMoneyDetail() {
        Fragment fragment = fm.findFragmentByTag(TAG_MONEY_DETAIL);
        if (fragment == null) {
            fragment = new MoneyDetailFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_MONEY_DETAIL);
        ft.addToBackStack(TAG_MONEY_DETAIL);
        ft.commit();
    }
    public void goDeposit() {
        Fragment fragment = fm.findFragmentByTag(TAG_DEPOSIT);
        if (fragment == null) {
            fragment = new DepositFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_DEPOSIT);
        ft.addToBackStack(TAG_DEPOSIT);
        ft.commit();
    }
    public void goTopUp() {
        Fragment fragment = fm.findFragmentByTag(TAG_TOP_UP);
        if (fragment == null) {
            fragment = new TopUpFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_TOP_UP);
        ft.addToBackStack(TAG_TOP_UP);
        ft.commit();
    }

    public void goMoneyRefund() {
        Fragment fragment = fm.findFragmentByTag(TAG_MONEY_REFUND_INSTRUCTIONS);
        if (fragment == null) {
            fragment = new MoneyRefundFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_MONEY_REFUND_INSTRUCTIONS);
        ft.addToBackStack(TAG_MONEY_REFUND_INSTRUCTIONS);
        ft.commit();
    }
    public void goUseInstruction() {
        Fragment fragment = fm.findFragmentByTag(TAG_PREFERENTIAL_USE_INSTRUCTIONS);
        if (fragment == null) {
            fragment = new UseInstructionsFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_PREFERENTIAL_USE_INSTRUCTIONS);
        ft.addToBackStack(TAG_PREFERENTIAL_USE_INSTRUCTIONS);
        ft.commit();
    }
    public void goNeedHelp() {
        Fragment fragment = fm.findFragmentByTag(TAG_TRIP_NEED_HELP);
        if (fragment == null) {
            fragment = new NeedHelpFragment();
        }
        mTopBar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_detail, fragment, TAG_TRIP_NEED_HELP);
        ft.addToBackStack(TAG_TRIP_NEED_HELP);
        ft.commit();
    }

    public ThirdTopBar getTopBar(){
        return mTopBar;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                clickBack();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void clickBack() {
        int count = fm.getBackStackEntryCount();
        Log.d("wjb sprocomm", "count : " + count);
        if (count <= 1) {
            finish();
        } else {
            fm.popBackStack();
        }
    }
}
