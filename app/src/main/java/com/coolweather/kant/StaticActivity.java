package com.coolweather.kant;

import android.app.Activity;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
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
        setData(typeCount);

        //定义折线图数据逻辑
        setLineData(dateCount, period);

    }

    //应用函数
    //创建统计列表视图
    protected View createViewFromMap(Activity activity, int item_layout, ViewGroup layout, String key, int count) {

        View view = LayoutInflater.from(activity).inflate(item_layout, layout, false);
        TextView nameText = (TextView) view.findViewById(R.id.name_text);
        TextView countText = (TextView) view.findViewById(R.id.name_count);

        String countString = Integer.toString(count) + "次";

        nameText.setText(key);
        countText.setText(countString);

        return view;
    }

    //设置饼图数据
    private void setData(Map<String, Integer> dataMap) {

        List<PieEntry> yValues = new ArrayList<PieEntry>();

        for (String key : dataMap.keySet()) {
            yValues.add(new PieEntry(dataMap.get(key), key));
        }

        PieDataSet yDataSet = new PieDataSet(yValues, "");
        yDataSet.setColors(new int[] {R.color.color0, R.color.color6, R.color.color5, R.color.color3, R.color.color2,
                R.color.color7, R.color.color1, R.color.color4}, getApplicationContext());

        yDataSet.setSliceSpace(1f);
        yDataSet.setSelectionShift(10f);

        PieData data = new PieData(yDataSet);

        data.setValueTextColor(getResources().getColor(R.color.colorText));
        data.setValueTextSize(16f);
        data.setValueFormatter(new PercentFormatter());

        //设置基本视图属性
        typeChart.setNoDataText("暂无数据");
        typeChart.setNoDataTextColor(getResources().getColor(R.color.colorText2));

        Description ds = typeChart.getDescription();
        ds.setEnabled(false);

        typeChart.setRotationAngle(30f);

        typeChart.setUsePercentValues(true);

        Legend typeLegend = typeChart.getLegend();
        typeLegend.setEnabled(false);

        typeChart.setData(data);
        typeChart.invalidate();
    }

    //设置折线图数据
    private void setLineData(Map<Long, Integer> dataMap, int period) {

        List<Entry> lineEntries = new ArrayList<Entry>();
        float xi = 0f;

        final ArrayList<String> dates = new ArrayList<String>(); //用于存放日期，用于X轴显示

        //X轴数值提取逻辑有问题，dates不会使用已更新的数据，切换时长时导致Index out of bound错误，
        // 为解决这个问题，只好先把dates初始化size为90；
        for (int i = 0; i < 90; i++) {
            dates.add(Integer.toString(0));
        }

        //把期间内每一天的数据获得并加入数据列表中，如果字典中没有，则为0
        for (int i = period - 1; i >= 0; i--) {
            Long date = Utility.getTodayCount() - i;
            if (dataMap.containsKey(date)) {
                lineEntries.add(new Entry(xi, dataMap.get(date)));
            } else {
                lineEntries.add(new Entry(xi, 0));
            }

            //在列表中加入当天的日期
            Calendar theDay = Calendar.getInstance();
            theDay.add(Calendar.DAY_OF_MONTH, (i * (-1)));
            String dayOfMonth = Integer.toString(theDay.get(Calendar.DAY_OF_MONTH));
            dates.set((int) xi, dayOfMonth);

            xi = xi + 1;
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "趋势数据");

        lineDataSet.setColor(Color.parseColor("#ff8282"));
        lineDataSet.setCircleColor(Color.parseColor("#ff8282"));
//        lineDataSet.setDrawValues(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setHighlightEnabled(false);

        LineData lineData = new LineData(lineDataSet);

        DefaultValueFormatter dformatter = new DefaultValueFormatter(0);
        lineData.setValueFormatter(dformatter);
        lineData.setValueTextSize(12f);
        lineData.setValueTextColor(Color.parseColor("#ff8282"));

        progressChart.setData(lineData);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.get((int) value);
            }
        };

        XAxis xAxis = progressChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        //设置基本视图属性
        progressChart.setNoDataText("暂无数据");
        progressChart.setNoDataTextColor(getResources().getColor(R.color.colorText2));
        progressChart.setScaleEnabled(false);
        progressChart.getAxisLeft().setEnabled(false);
        progressChart.getAxisRight().setEnabled(false);
        progressChart.enableScroll();
        if (period == 7) {
            progressChart.setVisibleXRangeMinimum(6f);
            progressChart.setVisibleXRangeMaximum(7f);
        } else if (period == 30) {
            progressChart.setVisibleXRangeMinimum(14f);
            progressChart.setVisibleXRangeMaximum(14f);
        } else if (period == 90) {
            progressChart.setVisibleXRangeMinimum(14f);
            progressChart.setVisibleXRangeMaximum(14f);
        }
        progressChart.moveViewToX(xi);
        XAxis xAxis1 = progressChart.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(false);
        xAxis1.setTextColor(getResources().getColor(R.color.colorText2));

        Legend legend = progressChart.getLegend();
        legend.setEnabled(false);
        Description ds = progressChart.getDescription();
        ds.setEnabled(false);


        progressChart.invalidate();
    }

}
