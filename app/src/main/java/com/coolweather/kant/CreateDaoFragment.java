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

import com.coolweather.kant.util.Utility;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dao_create, container, false);
        backButton = (Button) view.findViewById(R.id.back_button);
        createButton = (Button) view.findViewById(R.id.save_button);
        nameInput = (EditText) view.findViewById(R.id.name_input);
        typeInput = (EditText) view.findViewById(R.id.type_input);
        freInput = (EditText) view.findViewById(R.id.fre_input);
        goalInput = (EditText) view.findViewById(R.id.goal_input);
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
                String fre = freInput.getText().toString();
                String goal = goalInput.getText().toString();

                boolean result = false;
                result = Utility.createDao(name, type, fre, goal);
                hideKeyboard(v.getWindowToken());

                if (result) {
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), "保存失败，请注意核对信息", Toast.LENGTH_SHORT).show();
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
