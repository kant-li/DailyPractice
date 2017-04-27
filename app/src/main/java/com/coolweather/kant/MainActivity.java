package com.coolweather.kant;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coolweather.kant.db.Dao;
import com.coolweather.kant.db.Record;
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

        //如果是第一次登陆
        SharedPreferences sp = getSharedPreferences("isFirstIn", Activity.MODE_PRIVATE);
        boolean isFirstIn = sp.getBoolean("isFirstIn", true);

        if(isFirstIn) {
            //第一次打开应用
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();

            //创建数据库初始数据
            Utility.createDao("每天早上7点起床","生活","1","30");
            Utility.createDao("每天睡觉前刷牙","生活","1","30");
            Utility.createDao("每天读书1小时","学习","1","30");
            Utility.createDao("每3天跑步3公里","运动","3","10");
            Utility.createDao("每周给老爸打电话","生活","7","100");

        } else {

        }

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
        daoListNow = DataSupport.where("status = ?", "now").find(Dao.class);

        //如果没有数据，加载无事项界面
        if (daoListNow.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.no_item, nowLayout, false);
            nowLayout.addView(view);
        }

        for (Dao dao : daoListNow) {

            View view = createViewFromDao(this, R.layout.dao_item, nowLayout, dao);

            nowLayout.addView(view);
        }

    }

    //更新接下来事件列表
    public void refreshHoldList() {

        //获得数据库中的数据

        //先更新事项状态
        Utility.refreshDaoStatus();
        holdLayout.removeAllViews();
        daoListHold = DataSupport.where("status = ?", "hold").find(Dao.class);

        //如果没有数据，加载无事项界面
        if (daoListHold.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.no_item, holdLayout, false);
            holdLayout.addView(view);
        }

        for (Dao dao : daoListHold) {

            View view = createViewFromDao(this, R.layout.dao_item, holdLayout, dao);

            holdLayout.addView(view);
        }

    }

    //生成列表视图
    protected View createViewFromDao(Activity activity, int item_layout, ViewGroup layout, Dao dao) {

        View view = LayoutInflater.from(activity).inflate(item_layout, layout, false);
        CheckBox itemCheck = (CheckBox) view.findViewById(R.id.item_check);

        final String daoName = dao.getName();
        final String daoType = dao.getSort();
        final int count = dao.getCount();
        long endDate = dao.getEnd_date();
        String status = dao.getStatus();

        if (endDate > 0) {
            //单次事项，根据不同情况设置显示信息
            String checkShowSingle = "";
            long days = endDate - Utility.getTodayCount();
            if (days < 0) {
                checkShowSingle = daoName + "（已逾期）";
            } else if (days == 0) {
                checkShowSingle = daoName + "（今天到期）";
            } else if (days == 1) {
                checkShowSingle = daoName + "（明天到期）";
            } else {
                checkShowSingle = daoName + "（" + Long.toString(days) + "天后到期" + "）";
            }

            itemCheck.setText(checkShowSingle);

        } else {
            //重复事项，根据不同情况设置显示信息
            long today = Utility.getTodayCount();
            int goal = dao.getGoal();

            int fre = dao.getFrequency();
            long startDate = dao.getStart_date();
            long daysToNewEndDate = fre - ((today - startDate) % fre) - 1;

            String checkShowMulti = "";

            if (status.equals("now")) {
                checkShowMulti = daoName + "（" + Integer.toString(count) + "/" + Integer.toString(goal) + "）";
            } else {
                if (daysToNewEndDate == 1) {
                    checkShowMulti = daoName + "（" + Integer.toString(count) + "/" + Integer.toString(goal)
                            + "/" + "明天到期）";
                } else if (daysToNewEndDate == 2) {
                    checkShowMulti = daoName + "（" + Integer.toString(count) + "/" + Integer.toString(goal)
                            + "/" + "后天到期）";
                } else {
                    checkShowMulti = daoName + "（" + Integer.toString(count) + "/" + Integer.toString(goal)
                            + "/" + Long.toString(daysToNewEndDate) + "天后到期）";
                }
            }
            itemCheck.setText(checkShowMulti);
        }

        itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    //保存记录，用于统计
                    Record record = new Record();
                    record.setName(daoName);
                    record.setType(daoType);
                    record.setDate(Utility.getTodayCount());

                    record.save();

                    //更新事项数据与界面
                    Dao dao = (DataSupport.where("name = ?", daoName).find(Dao.class)).get(0);
                    dao.setRecent(Utility.getTodayCount());
                    int newCount = count + 1;
                    dao.setCount(newCount);

                    if (dao.save()) {
                        //给出提示
                        Toast.makeText(MainActivity.this, buttonView.getText().toString() + "已完成", Toast.LENGTH_SHORT).show();
                        //刷新界面
                        refreshHoldList();
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

        return view;
    }

}
