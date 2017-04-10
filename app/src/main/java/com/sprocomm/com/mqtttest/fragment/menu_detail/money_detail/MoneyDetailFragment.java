package com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sprocomm.com.mqtttest.MenuDetailActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.widget.ThirdTopBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyDetailFragment extends Fragment implements View.OnClickListener {


    private ThirdTopBar actionBar;

    public MoneyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setMyActionBar();
        return inflater.inflate(R.layout.fragment_money_detail, container, false);
    }

    private void setMyActionBar(){
        actionBar  = ((MenuDetailActivity)getActivity()).getTopBar();
        actionBar.setOnInfoListener(this);
        actionBar.setInfoVisibility(true);
        actionBar.setTvTitle("明细");
        actionBar.setTvInfo("退款说明");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_info:
                ((MenuDetailActivity)getActivity()).goMoneyRefund();
                break;
        }
    }
}
