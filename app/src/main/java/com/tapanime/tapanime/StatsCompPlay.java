package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsCompPlay extends AppCompatActivity {
    String name;
    int matches_played,matches_won,matches_lost;
    float ratio;
    public static final String EXTRA_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_competitive);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        Log.d("CHECKSQLTAG",name);
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
    }
    public void onSearch(View view){
        Intent intent = new Intent(this, LoadingComp.class);
        intent.putExtra(Menu.EXTRA_MESSAGE, name);
        startActivity(intent);
        finish();
    }
    public class setUpSQL implements Runnable{
        public void run () {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers","newuser","1234");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select* from user where username='"+name+"'");
                rs.next();
                Log.d("CHECKSQLTAG",name);
                matches_played=rs.getInt(10);
                matches_won=rs.getInt(11);
                matches_lost=rs.getInt(12);
                Log.d("CHECKSQLTAG",String.valueOf((matches_played)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView MatchesView = (TextView) findViewById(R.id.textMatch);
                        TextView WonView = (TextView) findViewById(R.id.textWin);
                        TextView LostView = (TextView) findViewById(R.id.textLost);
                        TextView RatioView = (TextView) findViewById(R.id.textRatio);
                        MatchesView.setText(String.valueOf("Matches played: "+matches_played));
                        WonView.setText(String.valueOf("Won: "+matches_won));
                        LostView.setText(String.valueOf("Lost: "+matches_lost));
                        if(matches_played!=0) {
                            ratio = (float) matches_won / matches_played;
                            if(ratio<0.2)
                            RatioView.setText("RANK: PLASTIC");
                            else if(ratio<0.3)
                                RatioView.setText("RANK: BRONZE");
                            else if (ratio<0.4)
                                RatioView.setText("RANK: SILVER");
                            else if (ratio<0.666)
                                RatioView.setText("RANK: GOLD");
                            else if (ratio<0.8)
                                RatioView.setText("RANK: Platinum");
                            else if(ratio==1)
                                RatioView.setText("RANK: Intergalactic Weeb");
                            Log.d("RATIO", String.valueOf(ratio));
                        }
                    }
                });
                con.close();
                //check=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
}
