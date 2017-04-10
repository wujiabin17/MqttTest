package com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sprocomm.com.mqtttest.MenuDetailActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.widget.ThirdTopBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMoneyFragment extends Fragment implements View.OnClickListener {


    private ThirdTopBar actionBar;
    private TextView tvDeposit;
    private Button btnTopUp;
    private TextView btnMoney;

    public MyMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_money, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        setMyActionBar();
        tvDeposit= (TextView)view.findViewById(R.id.tv_deposit);
        btnTopUp   = (Button) view.findViewById(R.id.btn_top_up);
        btnMoney = (TextView) view.findViewById(R.id.tv_money_num1);
        tvDeposit.setOnClickListener(this);
        btnTopUp.setOnClickListener(this);
    }

    private void setMyActionBar(){
        actionBar  = ((MenuDetailActivity)getActivity()).getTopBar();
        actionBar.setOnInfoListener(this);
        actionBar.setVisibility(View.VISIBLE);
        actionBar.setTvTitle("我的钱包");
        actionBar.setTvInfo("明细");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_info:
                ((MenuDetailActivity)getActivity()).goMoneyDetail();
                break;
            case R.id.tv_deposit:
                ((MenuDetailActivity)getActivity()).goDeposit();
                break;
            case R.id.btn_top_up:
                ((MenuDetailActivity)getActivity()).goTopUp();
                break;
        }
    }
}
