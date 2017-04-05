package com.sprocomm.com.mqtttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.sprocomm.com.mqtttest.utils.ToastUtil;
import com.sprocomm.com.mqtttest.widget.NormalTopBar;
import com.sprocomm.com.mqtttest.widget.ParallaxScollListView;

public class MenuActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {
    private ParallaxScollListView mListView;
    private ImageView mImageView;
    private NormalTopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();

    }

    private void initView() {
        mListView = (ParallaxScollListView) findViewById(R.id.layout_listview);
        View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        mImageView = (ImageView) header.findViewById(R.id.layout_header_image);
        mTopBar = (NormalTopBar) findViewById(R.id.menu_title);
        mTopBar.setOnBackListener(this);
        mTopBar.setTitle("好奇单车");
        mListView.setZoomRatio(ParallaxScollListView.ZOOM_X2);
        mListView.setParallaxImageView(mImageView);
        mListView.addHeaderView(header);
        mListView.setOnScrollListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{
                        "First Item",
                        "Second Item",
                        "Third Item",
                        "Fifth Item",
                        "Sixth Item",
                        "Seventh Item",
                        "Eighth Item",
                        "Ninth Item",
                        "Tenth Item",
                        "....."
                }
        );
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view == mTopBar.getBackView()){
            finish();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        Log.d("simon","absListView:" + absListView + " i:" + i);
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if(i >= 1){
            mTopBar.setTitle("个人中心");
        }else{
            mTopBar.setTitle("好奇单车");
        }
    }
}
