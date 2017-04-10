package com.sprocomm.com.mqtttest.fragment.menu_detail.perferential;


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
public class PerferentialFragment extends Fragment implements View.OnClickListener {


    private ThirdTopBar actionBar;

    public PerferentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setMyActionBar();
        return inflater.inflate(R.layout.fragment_perferential, container, false);
    }
    private void setMyActionBar(){
        actionBar  = ((MenuDetailActivity)getActivity()).getTopBar();
        actionBar.setInfoVisibility(true);
        actionBar.setOnInfoListener(this);
        actionBar.setTvTitle("我的优惠");
        actionBar.setTvInfo("使用说明");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_info:
                ((MenuDetailActivity)getActivity()).goUseInstruction();
                break;
        }
    }
}
