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
    private ListView daoListView;
    private ArrayAdapter<String> daoAdapter;
    private List<String> dataList = new ArrayList<>();
    private List<Dao> daoList;

    public SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_settings, container, false);
        setLayout = (LinearLayout) view.findViewById(R.id.set_layout);
        backButton = (Button) view.findViewById(R.id.back_button);
        addButton = (Button) view.findViewById(R.id.add_button);
        daoListView = (ListView) view.findViewById(R.id.dao_list);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorTopic);

        daoAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_city, dataList);
        daoListView.setAdapter(daoAdapter);
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
                refreshDaoList();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        refreshDaoList();
        super.onResume();
    }

    public void refreshDaoList() {
        //获得数据库中的数据
        if (dataList != null) {
            dataList.clear();
        }
        daoList = DataSupport.findAll(Dao.class);
        for (Dao dao : daoList) {
            dataList.add(dao.getName());
        }
        daoAdapter.notifyDataSetChanged();
        daoListView.setSelection(0);
    }

}
