package com.coolweather.kant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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
    private List<String> daoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_settings, container, false);
        setLayout = (LinearLayout) view.findViewById(R.id.set_layout);
        backButton = (Button) view.findViewById(R.id.back_button);
        addButton = (Button) view.findViewById(R.id.add_button);
        daoListView = (ListView) view.findViewById(R.id.dao_list);
        daoAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_city, daoList);
        daoListView.setAdapter(daoAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

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

    }

}
