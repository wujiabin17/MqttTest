package com.sprocomm.com.mqtttest.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.R;

public class ThirdTopBar extends RelativeLayout {
	private final TextView tvTitle;
	private final TextView tvInfo;
	private final ImageView btnBack;

	public ThirdTopBar(Context context) {
		this(context, null);
	}

	public ThirdTopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.bar_third, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		btnBack = (ImageView) findViewById(R.id.btn_back);
		tvInfo = (TextView) findViewById(R.id.tv_info);

	}
	public void setTvTitle(String title){
		tvTitle.setText(title);
	}
	public void seBackVisibility(boolean show) {
		btnBack.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	public void setInfoVisibility(boolean show) {
		tvInfo.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	public void setTvInfo(String info){
		tvInfo.setText(info);
	}

	public void setOnBackOnListener(OnClickListener listener) {
		btnBack.setOnClickListener(listener);
	}

	public void setOnInfoListener(OnClickListener listener){
		tvInfo.setOnClickListener(listener);
	}

	public String getTvInfo() {
		return tvInfo.getText().toString();
	}
}
