package com.coolweather.kant;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coolweather.kant.db.Dao;
import com.coolweather.kant.db.Record;
import com.coolweather.kant.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    private RadioGroup periodSelect;
    private LinearLayout recordLayout;
    private PieChart typeChart;
    private LineChart progressChart;

    private int period = 7;             //这个参数用于判断当前选择的时间长度，有7／30／90三个值，在选择按钮响应逻辑中改变

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //定义视图及其控件
        setContentView(R.layout.activity_static);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        periodSelect = (RadioGroup) findViewById(R.id.period_select);

        recordLayout = (LinearLayout) findViewById(R.id.record_layout);

        typeChart = (PieChart) findViewById(R.id.type_chart);
        progressChart = (LineChart) findViewById(R.id.progress_chart);

        //定义导航按钮响应逻辑
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //定义选择按钮响应逻辑
        periodSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton radioButton = (RadioButton) findViewById(i);
                String buttonName = radioButton.getText().toString();
                switch (buttonName) {
                    case "一星期":
                        period = 7;
                        break;
                    case "一个月":
                        period = 30;
                        break;
                    case "三个月":
                        period = 90;
                        break;
                    default:
                        break;
                }
                //刷新界面
                refreshData(period);
            }
        });

    }

    //重写onResume()方法
    @Override
    public void onResume() {
        refreshData(period);
        super.onResume();
    }

    //定义界面刷新
    private  void refreshData(int period) {

        //从数据库取得数据
        long startDate = Utility.getTodayCount() - period;
        List<Record> recordList = DataSupport.where("date > ?", Long.toString(startDate)).find(Record.class);

        //获得计数值，用map存储
        Map<String, Integer> nameCount = new HashMap<String, Integer>();
        Map<String, Integer> typeCount = new HashMap<String, Integer>();
        Map<Long, Integer> dateCount = new HashMap<Long, Integer>();

        for (Record record : recordList) {
            String name = record.getName();
            if (nameCount.containsKey(name)) {
                int count = nameCount.get(name) + 1;
                nameCount.put(name, count);
            } else {
                nameCount.put(name, 1);
            }

            String type = record.getType();
            if (typeCount.containsKey(type)) {
                int count = typeCount.get(type) + 1;
                typeCount.put(type, count);
            } else {
                typeCount.put(type, 1);
            }

            Long date = record.getDate();
            if (dateCount.containsKey(date)) {
                int count = dateCount.get(date) + 1;
                dateCount.put(date, count);
            } else {
                dateCount.put(date, 1);
            }
        }

        //定义列表生成逻辑
        //先删除旧数据
        recordLayout.removeAllViews();

        for (String key : nameCount.keySet()) {

            View view = createViewFromMap(this, R.layout.static_item, recordLayout, key, nameCount.get(key));
            recordLayout.addView(view);
        }

        //定义分类饼图数据逻辑

        //定义折线图数据逻辑

    }

    //应用函数
    protected View createViewFromMap(Activity activity, int item_layout, ViewGroup layout, String key, int count) {

        View view = LayoutInflater.from(activity).inflate(item_layout, layout, false);
        TextView nameText = (TextView) view.findViewById(R.id.name_text);
        TextView countText = (TextView) view.findViewById(R.id.name_count);

        String countString = Integer.toString(count) + "次";

        nameText.setText(key);
        countText.setText(countString);

        return view;
    }
}
