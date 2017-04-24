package com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sprocomm.com.mqtttest.MenuDetailActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.alipay.Alipay;
import com.sprocomm.com.mqtttest.weixin.WXPay;
import com.sprocomm.com.mqtttest.widget.ThirdTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment implements View.OnClickListener {


    private ThirdTopBar actionBar;
    private ListView lvPay;
    private Context mContext;
    private ArrayList<String> payItem = new ArrayList<>();
    private HashMap<Integer, Boolean> isSelected;
    private Button btnPay;

    public DepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        setMyActionBar();
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);
        lvPay = (ListView)view.findViewById(R.id.lv_pay);
        btnPay = (Button) view.findViewById(R.id.btn_top_up);
        btnPay.setOnClickListener(this);
        String[] item = {"支付宝","微信"};
        for(int i = 0; i<item.length; i++){
            payItem.add(item[i]);
        }
        final MyPayAdapter myPayAdapter = new MyPayAdapter();
        lvPay.setAdapter(myPayAdapter);
        init();
        lvPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder holder = (ViewHolder)view.getTag();
                if(holder.cbPayType.isChecked()){
                    isSelected.put(position,false);
                }else{
                    for(int i =0;i<payItem.size();i++){
                        if(position == i){
                            continue;
                        }else{
                            isSelected.put(i,false);
                        }
                    }
                    isSelected.put(position,true);
                }
                myPayAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < payItem.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnPay){
            int realPosition = -1;
            for(int i =0;i<payItem.size();i++) {
                if(isSelected.get(i)){
                    realPosition = i;
                }
            }
            if(realPosition ==  -1){
                return;
            }else if(realPosition == 0){
                doAlipay("21321321321");
            }else if(realPosition  == 1){
                doWXPay("21321321321");
            }
        }
    }
    private void doAlipay(String pay_param) {
        new Alipay(this, pay_param, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDealing() {
                Toast.makeText(mContext, "支付处理中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case Alipay.ERROR_RESULT:
                        Toast.makeText(mContext, "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_NETWORK:
                        Toast.makeText(mContext, "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_PAY:
                        Toast.makeText(mContext, "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(mContext, "支付错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            }
        }).doPay();
    }

    /**
     * 微信支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doWXPay(String pay_param) {
        String wx_appid = "wxXXXXXXX";     //替换为自己的appid
        WXPay.init(mContext, wx_appid);      //要在支付前调用
        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(mContext, "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(mContext, "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private class MyPayAdapter extends BaseAdapter {
        private int tempPosition = -1;
        @Override
        public int getCount() {
            return payItem.size();
        }

        @Override
        public Object getItem(int position) {
            return payItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.item_pay_type_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.ivPay = (ImageView) convertView.findViewById(R.id.iv_alipay);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.pay_title);
                viewHolder.cbPayType = (CheckBox) convertView.findViewById(R.id.cb_select_pay);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                viewHolder.ivPay.setImageResource(R.mipmap.alipay_icon);
            } else if (position == 1) {
                viewHolder.ivPay.setImageResource(R.mipmap.wx_icon);
            }
            viewHolder.tvTitle.setText(payItem.get(position));
            viewHolder.cbPayType.setChecked(isSelected.get(position));
            return convertView;
        }
    }

    private class ViewHolder{
        private ImageView ivPay;
        private TextView tvTitle;
        private CheckBox cbPayType;
    }


    private void setMyActionBar(){
        actionBar  = ((MenuDetailActivity)getActivity()).getTopBar();
        actionBar.setInfoVisibility(false);
        actionBar.setTvTitle("押金充值");
    }

}
