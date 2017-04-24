package com.coolweather.kant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.kant.db.Dao;
import com.coolweather.kant.util.Utility;

import org.litepal.crud.DataSupport;

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
        daoListOn = DataSupport.select("name").where("on = ?", "1").find(Dao.class);

        for (Dao dao : daoListOn) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dao_set_item, onLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);
            final Button setButton = (Button) view.findViewById(R.id.set_button);

            final String name = dao.getName();

            nameText.setText(name);

            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(getActivity(), setButton);
                    popup.getMenuInflater().inflate(R.menu.item_set, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.shutDown_item:
                                    Dao dao = new Dao();
                                    dao.setOn(2);
                                    dao.setEnd_date(Utility.getTodayCount());
                                    dao.updateAll("name = ?", name);
                                    Toast.makeText(getActivity(), name + "已关闭", Toast.LENGTH_SHORT).show();
                                    popup.dismiss();

                                    //更新界面
                                    refreshDaoListOn();
                                    refreshDaoListOff();
                                    break;
                                case R.id.change_item:

                                    popup.dismiss();

                                    Dao dao1 = (DataSupport.where("name = ?", name).find(Dao.class)).get(0);
                                    String name1 = dao1.getName();
                                    String type1 = dao1.getSort();
                                    int fre1 = dao1.getFrequency();
                                    int goal1 = dao1.getGoal();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", name1);
                                    bundle.putString("type", type1);
                                    bundle.putInt("fre", fre1);
                                    bundle.putInt("goal", goal1);

                                    ChangeDaoFragment cg = new ChangeDaoFragment();
                                    cg.setArguments(bundle);
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.add(R.id.drawer_layout, cg);
                                    ft.addToBackStack(null);
                                    ft.commit();

                                    break;
                                case R.id.delete_item:
                                    DataSupport.deleteAll(Dao.class, "name = ?", name);
                                    Toast.makeText(getActivity(), name + "已删除", Toast.LENGTH_SHORT).show();
                                    popup.dismiss();

                                    //更新界面
                                    refreshDaoListOn();
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

            onLayout.addView(view);
        }

    }

    public void refreshDaoListOff() {

        //获得数据库中的数据
        offLayout.removeAllViews();
        daoListOff = DataSupport.select("name").where("on = ?", "2").find(Dao.class);

        for (Dao dao : daoListOff) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dao_set_item, offLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);
            final Button setButton = (Button) view.findViewById(R.id.set_button);

            final String name = dao.getName();
            nameText.setText(name);

            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(getActivity(), setButton);
                    popup.getMenuInflater().inflate(R.menu.hold_item_set, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.open_item:
                                    Dao dao = new Dao();
                                    dao.setOn(1);
                                    dao.setStart_date(Utility.getTodayCount());
                                    dao.updateAll("name = ?", name);
                                    Toast.makeText(getActivity(), name + "已开启", Toast.LENGTH_SHORT).show();
                                    popup.dismiss();

                                    //更新界面
                                    refreshDaoListOn();
                                    refreshDaoListOff();
                                    break;
                                case R.id.delete_item:
                                    DataSupport.deleteAll(Dao.class, "name = ?", name);
                                    Toast.makeText(getActivity(), name + "已删除", Toast.LENGTH_SHORT).show();
                                    popup.dismiss();

                                    //更新界面
                                    refreshDaoListOff();
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

            offLayout.addView(view);
        }

    }

}
