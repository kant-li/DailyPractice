package com.coolweather.kant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kant on 2017/3/24.
 */

public class SetDaoFragment extends Fragment {

    private Button backButton;
    private Button addButton;
    private ListView daoListView;
    private ArrayAdapter<String> adapter;
    private List<String> daoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_settings, container, false);
        backButton = (Button) view.findViewById(R.id.back_button);
        addButton = (Button) view.findViewById(R.id.add_button);
        daoListView = (ListView) view.findViewById(R.id.dao_list);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_city, daoList);
        daoListView.setAdapter(adapter);
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

            }
        });

    }

}
