package com.sprocomm.com.mqtttest.fragment.menu_detail;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.LoginActivity;
import com.sprocomm.com.mqtttest.MenuActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.utils.DataCleanManager;
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
    private ProgressBar mProgressBar;
    private AlertDialog.Builder alertBuilder;

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
        mProgressBar = (ProgressBar)view.findViewById(R.id.pb_progress);
        String[] my = {"常用地址","清理缓存","关于我们","","用户协议","押金说明","充值协议",""};
        for(int i = 0; i<my.length; i++){
            settingsItem.add(my[i]);
        }
        MySettingsAdapter settingsAdapter  = new MySettingsAdapter();
        mListView.setAdapter(settingsAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 1:
                break;
            case 2:
                showDialog();
                break;

        }
    }
    protected void showQuitAccountDialog() {
        alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setMessage("确定退出账号吗?");
        alertBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                dialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    protected void showDialog() {
        alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setMessage("确定清理本地缓存数据");
        alertBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new MyProgressAsyncTask().execute();
                mProgressBar.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    private class MyProgressAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        int duration = 0;
        int current = 0;
        @Override
        protected Void doInBackground(Void... params) {
           do{
                current += 10;
                try {
                    publishProgress(current); //这里的参数类型是 AsyncTask<Void, Integer, Void>中的Integer决定的，在onProgressUpdate中可以得到这个值去更新UI主线程，这里是异步线程
                    DataCleanManager.cleanApplicationData(mContext);
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }while(current != 100);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            if(values[0] == 100){
                mProgressBar.setVisibility(View.GONE);
            }
        }
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
        public int getItemViewType(int position) {
            if(settingsItem.size() -1  == position){
                return 1;
            }else if(position == 3){
                return 2;
            }else{
                return 3;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(getItemViewType(position)  == 3) {
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
            }else if(getItemViewType(position) == 2){
                convertView = View.inflate(mContext,
                        R.layout.driver_layout, null);
            }
            else if(getItemViewType(position) == 1){
                    convertView = View.inflate(mContext,
                            R.layout.settings_quit_bt, null);
                    Button btQuitAccount = (Button)convertView.findViewById(R.id.btn_quit_account);
                    btQuitAccount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showQuitAccountDialog();
                        }
                    });
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
