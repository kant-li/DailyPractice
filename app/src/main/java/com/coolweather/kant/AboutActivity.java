package com.coolweather.kant;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    private TextView introText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById((R.id.nav_button));
        introText = (TextView) findViewById(R.id.intro_text);

        introText.setText("修行是一个个人管理类应用，用于帮助习惯养成与事项安排。\n\n" +
                "《大学》曰：茍日新，日日新，又日新。\n" +
                "《中庸》曰：君子之中庸也，君子而时中。\n" +
                "《周易》曰：天行健，君子以自强不息。\n\n" +
                "祝你早日找到人生中真正重要的事！\n" +
                "祝你在行走的路上早得平安喜乐！");

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}
