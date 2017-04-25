package com.coolweather.kant;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coolweather.kant.db.Record;
import com.coolweather.kant.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.litepal.crud.DataSupport;

import java.util.List;

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

            }
        });

        //从数据库取得数据
        long startDate = Utility.getTodayCount() - period;
        List<Record> recordList = DataSupport.where("date >= ?", Long.toString(startDate)).find(Record.class);
        

        //定义列表生成逻辑

        //定义分类饼图数据逻辑

        //定义折线图数据逻辑

        //应用函数

    }
}
