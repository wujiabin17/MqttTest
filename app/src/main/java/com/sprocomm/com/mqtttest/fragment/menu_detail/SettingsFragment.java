package com.sprocomm.com.mqtttest.fragment.menu_detail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.MenuActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.widget.NormalTopBar;
import com.sprocomm.com.mqtttest.widget.ParallaxScollListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ParallaxScollListView mListView;
    private ArrayList<String> settingsItem = new ArrayList<>();
    private ImageView mHeadView;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getContext();
        initView(view);
        return  view;

    }

    private void initView(View view) {
        mListView = (ParallaxScollListView) view.findViewById(R.id.lv_settings);
        View header = LayoutInflater.from(mContext).inflate(R.layout.settings_listview_header, null);
        mHeadView = (ImageView) header.findViewById(R.id.iv_head_ipc);
        mListView.setZoomRatio(ParallaxScollListView.ZOOM_X2);
        mListView.setParallaxImageView(mHeadView);
        mListView.addHeaderView(header);

        String[] my = {"常用地址","清理缓存","关于我们","用户协议","押金说明","充值协议"};
        for(int i = 0; i<my.length; i++){
            settingsItem.add(my[i]);
        }
        MySettingsAdapter settingsAdapter  = new MySettingsAdapter();
        mListView.setAdapter(settingsAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class MySettingsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return settingsItem.size();
        }

        @Override
        public Object getItem(int position) {
            return settingsItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
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
            viewHolder.tv_my.setText(settingsItem.get(position));

            return convertView;
        }
    }
    private class ViewHolder{
        private TextView tv_my;
        private TextView tv_money;
        private ImageView iv_go;
    }
}
