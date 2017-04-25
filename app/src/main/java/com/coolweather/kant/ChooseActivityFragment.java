package com.coolweather.kant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kant on 2017/3/24.
 */

public class ChooseActivityFragment extends Fragment {

    private TextView mottoText;
    private ListView activityListView;
    private ArrayAdapter<String> adapter;
    private List<String> activityList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_activity, container, false);
        mottoText = (TextView) view.findViewById(R.id.motto_text);
        activityListView = (ListView) view.findViewById(R.id.activity_list);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_activity, activityList);
        activityListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mottoText.setText("天行健\n君子以自强不息");
        activityList.add("事项");
        activityList.add("天气");
        activityList.add("关于");

        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Activity activity = new Activity();
                Intent intent;

                if (activityList.get(position).equals("事项")) {
                    activity = new MainActivity();
                } else if (activityList.get(position).equals("天气")) {
                    activity = new WeatherActivity();
                } else if (activityList.get(position).equals("关于")) {
                    activity = new AboutActivity();
                }

//                if (getActivity().getClass() == activity.getClass()) {
//                    activity = getActivity();
//                    activity.drawerLayout.closeDrawers();
//                } else {
//                    intent = new Intent(getActivity(), activity.getClass());
//                    startActivity(intent);
//                    getActivity().finish();
//                }

                intent = new Intent(getActivity(), activity.getClass());
                startActivity(intent);
                getActivity().finish();
            }

        });
    }

}
