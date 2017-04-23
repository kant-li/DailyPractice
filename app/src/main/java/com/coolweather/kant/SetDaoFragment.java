package com.coolweather.kant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.kant.db.Dao;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kant on 2017/3/24.
 */

public class SetDaoFragment extends Fragment {

    public LinearLayout setLayout;
    private Button backButton;
    private Button addButton;

    private LinearLayout onLayout;
    private List<Dao> daoListOn;

    private LinearLayout offLayout;
    private List<Dao> daoListOff;

    public SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_settings, container, false);
        setLayout = (LinearLayout) view.findViewById(R.id.set_layout);
        backButton = (Button) view.findViewById(R.id.back_button);
        addButton = (Button) view.findViewById(R.id.add_button);

        onLayout = (LinearLayout) view.findViewById(R.id.on_layout);
        offLayout = (LinearLayout) view.findViewById(R.id.off_layout);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorTopic);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //新增按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDaoFragment crFragment = new CreateDaoFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.drawer_layout, crFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDaoListOn();
                refreshDaoListOff();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        refreshDaoListOn();
        refreshDaoListOff();
        super.onResume();
    }

    public void refreshDaoListOn() {

        //获得数据库中的数据
        onLayout.removeAllViews();
        daoListOn = DataSupport.findAll(Dao.class);

        for (Dao dao : daoListOn) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dao_set_item, onLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);

            nameText.setText(dao.getName());

            onLayout.addView(view);
        }

    }

    public void refreshDaoListOff() {

        //获得数据库中的数据
        offLayout.removeAllViews();
        daoListOff = DataSupport.findAll(Dao.class);

        for (Dao dao : daoListOff) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dao_set_item, offLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);

            nameText.setText(dao.getName());

            offLayout.addView(view);
        }

    }

}
