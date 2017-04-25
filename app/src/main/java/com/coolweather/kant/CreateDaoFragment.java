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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CreateDaoFragment extends Fragment {

    private Button backButton;
    private Button createButton;

    private EditText nameInput;
    private EditText typeInput;
    private EditText freInput;
    private EditText goalInput;
    private EditText deadlineInput;
    private CheckBox multiCheck;
    private CheckBox singleCheck;

    private int opCode = 0;           //这个参数用于判断是重复事项还是单次事项，以采取不同保存逻辑；

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_create, container, false);
        backButton = (Button) view.findViewById(R.id.back_button);
        createButton = (Button) view.findViewById(R.id.save_button);
        nameInput = (EditText) view.findViewById(R.id.name_input);
        typeInput = (EditText) view.findViewById(R.id.type_input);
        freInput = (EditText) view.findViewById(R.id.fre_input);
        goalInput = (EditText) view.findViewById(R.id.goal_input);
        deadlineInput = (EditText) view.findViewById(R.id.deadline_input);
        multiCheck = (CheckBox) view.findViewById(R.id.multi_check);
        singleCheck = (CheckBox) view.findViewById(R.id.single_check);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

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

                ArrayList<String> nameList = new ArrayList<String>();
                List<Dao> daoList = DataSupport.select("name").find(Dao.class);
                for (Dao dao : daoList) {
                    nameList.add(dao.getName());
                }

                if (nameList.contains(name)) {
                    //命名重复，给出提示
                    Toast.makeText(getActivity(), "命名重复，请注意核对信息", Toast.LENGTH_SHORT).show();
                } else {

                    //开始保存数据，根据opCode判断操作逻辑
                    boolean result = false;

                    if (opCode == 2) {
                        //单次模式,输入异常通过调用函数的返回值处理，如果有异常，返回值为false
                        String endDays = deadlineInput.getText().toString();

                        result = Utility.createDao(name, type, endDays);

                    } else if (opCode == 1) {
                        //多次模式，输入异常通过调用函数的返回值处理，如果有异常，返回值为false
                        String fre = freInput.getText().toString();
                        String goal = goalInput.getText().toString();

                        result = Utility.createDao(name, type, fre, goal);

                    } else {
                        //没有选择单次还是重复，给出提示
                        Toast.makeText(getActivity(), "请选择类别并完善信息", Toast.LENGTH_SHORT).show();
                    }

                    //判断结果，退出界面
                    if (result) {
                        //关闭软键盘，给出提示
                        hideKeyboard(v.getWindowToken());
                        Toast.makeText(getActivity(), "新建成功，请下拉刷新", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "保存失败，请注意核对信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        multiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    freInput.setHint(" 请输入频率 ");
                    freInput.setFocusable(true);
                    freInput.setFocusableInTouchMode(true);
                    freInput.requestFocus();
                    goalInput.setHint(" 请输入目标 ");
                    goalInput.setFocusable(true);
                    goalInput.setFocusableInTouchMode(true);
                    opCode = 1;

                    //让单次事项不可选
                    singleCheck.setClickable(false);
                    singleCheck.setEnabled(false);
                    Toast.makeText(getActivity(), "设置为重复事项", Toast.LENGTH_SHORT).show();

                } else {
                    freInput.setHint(" 不需要输入 ");
                    freInput.setFocusable(false);
                    freInput.setFocusableInTouchMode(false);
                    goalInput.setHint(" 不需要输入 ");
                    goalInput.setFocusable(false);
                    goalInput.setFocusableInTouchMode(false);
                    opCode = 0;

                    //恢复单次事项可选
                    singleCheck.setClickable(true);
                    singleCheck.setEnabled(true);
                }
            }
        });

        singleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    deadlineInput.setHint(" 截止的天数 ");
                    deadlineInput.setFocusable(true);
                    deadlineInput.setFocusableInTouchMode(true);
                    deadlineInput.requestFocus();
                    opCode = 2;

                    //让重复事项不可选
                    multiCheck.setClickable(false);
                    multiCheck.setEnabled(false);
                    Toast.makeText(getActivity(), "设置为单次事项", Toast.LENGTH_SHORT).show();

                } else {
                    deadlineInput.setHint(" 不需要输入 ");
                    deadlineInput.setFocusable(false);
                    deadlineInput.setFocusableInTouchMode(false);
                    opCode = 0;

                    //恢复重复事项可选
                    multiCheck.setClickable(true);
                    multiCheck.setEnabled(true);
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
