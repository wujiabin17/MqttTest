package com.sprocomm.com.mqtttest.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sprocomm.com.mqtttest.LoginActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.base.BaseFragment;
import com.sprocomm.com.mqtttest.db.AccountDao;
import com.sprocomm.com.mqtttest.domain.Account;
import com.sprocomm.com.mqtttest.utils.ToastUtil;


public class SignUpFra extends BaseFragment implements OnClickListener {
	private String TAG = "SignUpFra";

	private EditText etAccount;
	private EditText etPwd;
	private Button btnSignUp;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_sign_up, container, false);

		initView(view);
		initEvent();
		return view;
	}

	private void initEvent() {
		btnSignUp.setOnClickListener(this);
	}

	private void initView(View view) {
		etAccount = (EditText) view.findViewById(R.id.et_sign_up_account);
		etPwd = (EditText) view.findViewById(R.id.et_sign_up_pwd);
		btnSignUp = (Button) view.findViewById(R.id.btn_sign_up);

	}
	@Override
	public void onClick(View v) {
		if (v == btnSignUp) {
			doSignUp();
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	// 处理注册
	private void doSignUp() {
		Context context = getActivity();
		if (context == null) {
			return;
		}

		final String account = etAccount.getText().toString().trim();
		if (TextUtils.isEmpty(account)) {
			ToastUtil.show(context, "用户名为空");
			return;
		}
		final String password = etPwd.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(context, "密码为空");
			return;
		}
	}
}