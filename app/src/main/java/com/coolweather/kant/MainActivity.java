package com.coolweather.kant;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coolweather.kant.db.Dao;
import com.coolweather.kant.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button setButton;
    private FloatingActionButton floatAddButton;

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
        floatAddButton = (FloatingActionButton) findViewById(R.id.float_add_button);

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

        //悬浮新增按钮
        floatAddButton.setOnClickListener(new View.OnClickListener() {
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

        //先更新事项状态
        Utility.refreshDaoStatus();
        nowLayout.removeAllViews();
//        daoListNow = DataSupport.findAll(Dao.class);
        daoListNow = DataSupport.select("name").where("status = ?", "now").find(Dao.class);

        for (Dao dao : daoListNow) {

            View view = LayoutInflater.from(this).inflate(R.layout.dao_item, nowLayout, false);
            CheckBox itemCheck = (CheckBox) view.findViewById(R.id.item_check);

            itemCheck.setText(dao.getName());

            itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {

                        Dao dao = (DataSupport.where("name = ?", buttonView.getText().toString()).find(Dao.class)).get(0);
                        dao.setRecent(Utility.getTodayCount());

                        if (dao.save()) {
                            Toast.makeText(MainActivity.this, buttonView.getText().toString() + "已完成", Toast.LENGTH_SHORT).show();
                            refreshNowList();
                        } else {
                            Toast.makeText(MainActivity.this, "啊，未知异常！", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "啊，未知异常！", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            nowLayout.addView(view);
        }

    }

    //更新接下来事件列表
    public void refreshHoldList() {

        //获得数据库中的数据

        //先更新事项状态
        Utility.refreshDaoStatus();
        holdLayout.removeAllViews();
//        daoListHold = DataSupport.findAll(Dao.class);
        daoListHold = DataSupport.select("name").where("status = ?", "hold").find(Dao.class);

        for (Dao dao : daoListHold) {

            View view = LayoutInflater.from(this).inflate(R.layout.dao_item, holdLayout, false);
            CheckBox itemCheck = (CheckBox) view.findViewById(R.id.item_check);

            itemCheck.setText(dao.getName());

            itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {

                        Dao dao = (DataSupport.where("name = ?", buttonView.getText().toString()).find(Dao.class)).get(0);
                        dao.setRecent(Utility.getTodayCount());

                        if (dao.save()) {
                            Toast.makeText(MainActivity.this, buttonView.getText().toString() + "已完成", Toast.LENGTH_SHORT).show();
                            refreshHoldList();
                        } else {
                            Toast.makeText(MainActivity.this, "啊，未知异常！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "啊，未知异常！", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holdLayout.addView(view);
        }

    }

}
