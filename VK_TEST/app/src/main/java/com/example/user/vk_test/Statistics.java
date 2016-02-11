package com.example.user.vk_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends Activity {
    TextView count;
    TextView online;
    TextView women;
    TextView men;
    TextView in_city;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        count = (TextView) findViewById(R.id.friends);
        online = (TextView) findViewById(R.id.online);
        women = (TextView) findViewById(R.id.women);
        men = (TextView) findViewById(R.id.men);
        in_city = (TextView)findViewById(R.id.in_city);
        Intent intent = getIntent();
        int friends_count =  intent.getIntExtra("com.example.user.vk_test",0);
        count.setText(String.valueOf(friends_count));
        int online_count = intent.getIntExtra("com.example.user.vk_test1", 0);
        online.setText(String.valueOf(online_count));
        int woman_count = intent.getIntExtra("com.example.user.vk_test2",0);
        women.setText(String.valueOf(woman_count));
        int men_count = intent.getIntExtra("com.example.user.vk_test3",0);
        men.setText(String.valueOf(men_count));
        int city_count = intent.getIntExtra("com.example.user.vk_test4",1);
        in_city.setText(String.valueOf(city_count));

    }

}
