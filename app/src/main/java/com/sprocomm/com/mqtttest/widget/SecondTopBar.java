package com.sprocomm.com.mqtttest.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.R;

public class SecondTopBar extends RelativeLayout {
	private final TextView tvTitle;
	private ImageView btnMenu;
	private ImageView btSettings;

	public SecondTopBar(Context context) {
		this(context, null);
	}

	public SecondTopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.bar_second, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		btnMenu = (ImageView) findViewById(R.id.btn_menu);
		btSettings = (ImageView) findViewById(R.id.btn_settings);
	}
	public void setTvTitle(String title){
		tvTitle.setText(title);
	}
	public void setMenuVisibility(boolean show) {
		btnMenu.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	public void setSettingsVisibility(boolean show) {
		btSettings.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	public void setOnMenuOnListener(OnClickListener listener) {
		btnMenu.setOnClickListener(listener);
	}

	public void setOnSettingListener(OnClickListener listener) {
		btSettings.setOnClickListener(listener);
	}
}
