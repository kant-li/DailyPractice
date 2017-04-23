package com.coolweather.kant;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.kant.db.Dao;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button setButton;

    private LinearLayout nowLayout;
    private List<Dao> daoListNow;

    private LinearLayout holdLayout;
    private List<Dao> daoListHold;

    public SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById((R.id.nav_button));
        setButton = (Button) findViewById(R.id.set_button);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorTopic);

        nowLayout = (LinearLayout) findViewById(R.id.now_layout);

        holdLayout = (LinearLayout) findViewById(R.id.hold_layout);

        //导航按钮
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //设置按钮
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDaoFragment sdFragment = new SetDaoFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.drawer_layout, sdFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNowList();
                refreshHoldList();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        refreshNowList();
        refreshHoldList();
        super.onResume();
    }

    //更新今日事件列表
    public void refreshNowList() {

        //获得数据库中的数据
        nowLayout.removeAllViews();
        daoListNow = DataSupport.findAll(Dao.class);

        for (Dao dao : daoListNow) {

            View view = LayoutInflater.from(this).inflate(R.layout.dao_item, nowLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);

            nameText.setText(dao.getName());

            nowLayout.addView(view);
        }

    }

    //更新接下来事件列表
    public void refreshHoldList() {

        //获得数据库中的数据
        holdLayout.removeAllViews();
        daoListHold = DataSupport.findAll(Dao.class);

        for (Dao dao : daoListHold) {

            View view = LayoutInflater.from(this).inflate(R.layout.dao_item, holdLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);

            nameText.setText(dao.getName());

            holdLayout.addView(view);
        }

    }

}
