package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EndRounds extends AppCompatActivity {
    String name;
    int score=0;
    int round=0;
    public static final String EXTRA_MESSAGE = "message";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endrounds);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        Bundle extras = intent.getExtras();
        score = extras.getInt("ScoreVariableName", 0);
        round = extras.getInt("RoundVariableName", 0);
        setRound();
    }
    public void setRound() {
        TextView messageView = (TextView)findViewById(R.id.textDisplay);
        messageView.setText("Congratulations! You scored "+score+" out of "+round+" rounds!");
    }
}
