package com.sprocomm.com.mqtttest.fragment.menu_detail.money_detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sprocomm.com.mqtttest.MenuDetailActivity;
import com.sprocomm.com.mqtttest.R;
import com.sprocomm.com.mqtttest.widget.ThirdTopBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {


    private ThirdTopBar actionBar;

    public DepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setMyActionBar();
        return inflater.inflate(R.layout.fragment_deposit, container, false);
    }

    private void setMyActionBar(){
        actionBar  = ((MenuDetailActivity)getActivity()).getTopBar();
        actionBar.setInfoVisibility(false);
        actionBar.setTvTitle("押金充值");
    }

}