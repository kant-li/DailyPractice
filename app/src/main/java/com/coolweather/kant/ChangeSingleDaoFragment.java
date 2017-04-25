package com.coolweather.kant;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coolweather.kant.db.Dao;
import com.coolweather.kant.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kant on 2017/3/24.
 */

public class ChangeSingleDaoFragment extends Fragment {

    private Button backButton;
    private Button createButton;

    private EditText nameInput;
    private EditText typeInput;
    private EditText deadlineInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_change_single, container, false);
        backButton = (Button) view.findViewById(R.id.back_button);
        createButton = (Button) view.findViewById(R.id.save_button);
        nameInput = (EditText) view.findViewById(R.id.name_input);
        typeInput = (EditText) view.findViewById(R.id.type_input);
        deadlineInput = (EditText) view.findViewById(R.id.deadline_input);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final String name1 = getArguments().getString("name");
        String type1 = getArguments().getString("type");
        String endDays = getArguments().getString("endDays");

        nameInput.setText(name1);
        typeInput.setText(type1);
        deadlineInput.setText(endDays);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v.getWindowToken());
                getActivity().onBackPressed();

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String type = typeInput.getText().toString();

                //检查命名重复问题
                ArrayList<String> nameList = new ArrayList<String>();
                List<Dao> daoList = DataSupport.select("name").find(Dao.class);
                for (Dao dao : daoList) {
                    nameList.add(dao.getName());
                }

                //去掉本体
                nameList.remove(name1);

                if (nameList.contains(name)) {
                    Toast.makeText(getActivity(), "事项已存在，请注意核对", Toast.LENGTH_SHORT).show();
                } else {

                    //依然要处理输入异常问题
                    Long deadline = 0l;

                    try {
                        int deadlineDays = Integer.valueOf(deadlineInput.getText().toString());
                        deadline = Utility.getTodayCount() + deadlineDays;

                        //保存修改
                        Dao dao = new Dao();
                        dao.setName(name);
                        dao.setSort(type);
                        dao.setEnd_date(deadline);
                        dao.updateAll("name = ?", name1);

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "请核对截止天数", Toast.LENGTH_SHORT).show();
                    }

                    hideKeyboard(v.getWindowToken());
                    Toast.makeText(getActivity(), "修改成功，请下拉刷新", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });

    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
