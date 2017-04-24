package com.sprocomm.com.mqtttest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.utils.ToastUtil;
import com.sprocomm.com.mqtttest.widget.NormalTopBar;
import com.sprocomm.com.mqtttest.widget.ParallaxScollListView;

import java.util.ArrayList;

public class MenuActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private ParallaxScollListView mListView;
    private ImageView mImageView;
    private NormalTopBar mTopBar;
    private ArrayList<String> personItem = new ArrayList<String>();
    private static final int TYPE_OPERATIO_PARAMETERS = 1;
    private static final int TYPE_USER_CONFIG = 2;
    private static final int TYPE_DRIVED = 3;

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
        startService(new Intent(this,OpenScreenService.class));
        String[] my = {"","我的钱包","我的优惠","我的行程","","我的消息","邀请好友","用户指南","设置"};
        personConfigAdapter personAdapter = new personConfigAdapter(this);
        for(int i = 0; i<my.length; i++){
            personItem.add(my[i]);
        }

        mListView.setAdapter(personAdapter);
        mListView.setOnItemClickListener(this);
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
        Log.d("wjb simon","i:" + i+" i1:" + i1 + " i2:" + i2);
        if(i > 0 || i1 >9){
            mTopBar.setTitle("个人中心");
        }else{
            mTopBar.setTitle("好奇单车");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(position == 1) {
            return;
        }else if(position > 5){
            position = position -1;
        }
        Intent intent  = new Intent(this,MenuDetailActivity.class);
        intent.putExtra("detail_menu",position);
        startActivity(intent);
    }

    public class personConfigAdapter extends BaseAdapter {
        private Context mContext;

        private personConfigAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_OPERATIO_PARAMETERS;
            } else if(position == 4){
                return TYPE_DRIVED;
            } else{
                return TYPE_USER_CONFIG;
            }
        }

        @Override
        public int getCount() {
            return personItem.size();
        }

        @Override
        public Object getItem(int position) {
            return personItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (getItemViewType(position) == TYPE_USER_CONFIG) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = View.inflate(mContext,
                            R.layout.list_item_menu, null);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_my = (TextView) convertView.findViewById(R.id.tv_my_title);
                    viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_my_money);
                    viewHolder.iv_go = (ImageView) convertView.findViewById(R.id.iv_go);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tv_my.setText(personItem.get(position));
            }else if(getItemViewType(position) == TYPE_DRIVED){
                convertView = View.inflate(mContext,
                        R.layout.driver_layout, null);
            }else if (getItemViewType(position) == TYPE_OPERATIO_PARAMETERS) {
                if (convertView == null) {
                    convertView  = View.inflate(mContext,R.layout.paramenters_operatio_layout,null);
                    TextView tv_cycle_dur = (TextView) convertView.findViewById(R.id.tv_cycling_dur);
                    TextView tv_save_carbon = (TextView) convertView.findViewById(R.id.tv_saving_carbon);
                    TextView tv_athletic = (TextView) convertView.findViewById(R.id.tv_athletic_achievements);
                }
            }
            return convertView;
        }
    }
    private class ViewHolder{
        private TextView tv_my;
        private TextView tv_money;
        private ImageView iv_go;
    }
}
