package com.sprocomm.com.mqtttest;

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.sprocomm.com.mqtttest.base.BaseActivity;
import com.sprocomm.com.mqtttest.db.AccountDao;
import com.sprocomm.com.mqtttest.db.MobileCycleDB;
import com.sprocomm.com.mqtttest.domain.Account;
import com.sprocomm.com.mqtttest.fragment.LogoFra;
import com.sprocomm.com.mqtttest.fragment.SignInFra;
import com.sprocomm.com.mqtttest.fragment.SignUpFra;
import com.sprocomm.com.mqtttest.widget.NormalTopBar;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private NormalTopBar mTopBar;
    private  FragmentManager fm;
    private int enterFlag = 0;
    public static final String TAG_LOGO = "logo";
    public static final String TAG_SIGN_IN = "sign_in";
    public static final String TAG_SIGN_UP = "sign_up";
    public static final String TAG_FILL_INFO = "fill_info";
    public static final String ENTER_KEY = "enter";
    public static final int ENTER_FIRST = 0;
    public static final int ENTER_LOGINED = 1;
    public static final int ENTER_SIGN_IN = 2;
    public static final int ENTER_SIGN_UP = 3;
    public static final int ENTER_FILL_INFO = 4;
    private AccountDao dao;
    private Fragment currentFra;
    private String currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        fm = getSupportFragmentManager();
        enterFlag = getIntent().getIntExtra(ENTER_KEY, ENTER_FIRST);

        dao = new AccountDao(this);
        Account account = dao.getCurrentAccount();
        if (account != null && !TextUtils.isEmpty(account.getName())) {
            enterFlag = ENTER_LOGINED;
        }

        if (enterFlag == ENTER_FIRST) {
            mTopBar.setVisibility(View.GONE);

            // 第一次登录
            currentFra = new LogoFra();
            Bundle args = new Bundle();
            args.putInt(LogoFra.ARG_KEY, LogoFra.ARG_TYPE_FIRST);
            currentFra.setArguments(args);
            currentTag = TAG_LOGO;
        } else if (enterFlag == ENTER_LOGINED) {
            mTopBar.setVisibility(View.GONE);

            // 用户已经登录
            currentFra = new LogoFra();
            Bundle args = new Bundle();
            args.putInt(LogoFra.ARG_KEY, LogoFra.ARG_TYPE_LOGINED);
            currentFra.setArguments(args);
            currentTag = TAG_LOGO;
        } else if (enterFlag == ENTER_SIGN_IN) {
            currentFra = new SignInFra();

            // 设置顶部title
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.setTitle(R.string.sign_in_title);
            mTopBar.setBackVisibility(false);

            currentTag = TAG_SIGN_IN;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contanier_login, currentFra, currentTag);
        transaction.addToBackStack(currentTag);
        transaction.commit();
    }

    private void initView() {
        mTopBar = (NormalTopBar) findViewById(R.id.activity_login_top_bar);
        mTopBar.setOnBackListener(this);

    }

    @Override
    public void onBackPressed() {
        clickBack();
    }
    private void clickBack() {
        int count = fm.getBackStackEntryCount();
        Log.d("", "count : " + count);
        if (count <= 1) {
            finish();
        } else {
            fm.popBackStack();
            if (count == 2) {
                FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(0);
                String name = entry.getName();
                if (TAG_LOGO.equals(name)) {
                    mTopBar.setVisibility(View.GONE);
                }
            }
        }
    }
    public void go2SignIn() {
        Fragment fragment = fm.findFragmentByTag(TAG_SIGN_IN);
        if (fragment == null) {
            fragment = new SignInFra();
        }

        // 设置头
        mTopBar.setVisibility(View.VISIBLE);
        mTopBar.setTitle(R.string.sign_in_title);
        mTopBar.setBackVisibility(true);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contanier_login, fragment, TAG_SIGN_IN);
        ft.addToBackStack(TAG_SIGN_IN);
        ft.commit();
    }

    public void go2SignUp() {
        Fragment fragment = fm.findFragmentByTag(TAG_SIGN_UP);
        if (fragment == null) {
            fragment = new SignUpFra();
        }

        mTopBar.setVisibility(View.VISIBLE);
        mTopBar.setTitle(R.string.sign_up_title);
        mTopBar.setBackVisibility(true);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contanier_login, fragment, TAG_SIGN_UP);
        ft.addToBackStack(TAG_SIGN_UP);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if (view == mTopBar.getBackView()) {
            clickBack();
        }
    }
}
