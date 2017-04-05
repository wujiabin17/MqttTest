package com.sprocomm.com.mqtttest.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sprocomm.com.mqtttest.MainActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.base.BaseFragment;
import com.sprocomm.com.mqtttest.utils.ToastUtil;


public class SignInFra extends BaseFragment implements OnClickListener {
	private String TAG = "SignInFra";

	private EditText etAccount;
	private EditText etPwd;
	private Button btnSignIn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_sign_in, container, false);
		initView(view);
		initEvent();
		return view;
	}

	private void initEvent() {
		btnSignIn.setOnClickListener(this);
	}

	private void initView(View view) {
		etAccount = (EditText) view.findViewById(R.id.et_sign_in_account);
		etPwd = (EditText) view.findViewById(R.id.et_sign_in_pwd);
		btnSignIn = (Button) view.findViewById(R.id.btn_sign_in);
	}

	@Override
	public void onClick(View v) {
		if (v == btnSignIn) {
			doSignIn();
		}
	}

	private void doSignIn() {
		Context context = getActivity();
		if (context == null) {
			return;
		}

		String account = etAccount.getText().toString().trim();
		if (TextUtils.isEmpty(account)) {
			ToastUtil.show(context, "用户名为空");
			return;
		}
		String password = etPwd.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(context, "密码为空");
			return;
		}
		if(account.equalsIgnoreCase("123") && password.equalsIgnoreCase("123")){
			getActivity().finish();
			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
			ToastUtil.show(context,"登录成功!!");
		}
	}
}