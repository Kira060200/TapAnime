package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StatsQuickPlay extends AppCompatActivity {
    String name;
    public static final String EXTRA_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_quickplay);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        /*Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        checkThread.start();*/
    }
}
