package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsQuickPlay extends AppCompatActivity {
    String name;
    int matches_played,matches_won,matches_lost;
    float ratio;
    public static final String EXTRA_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_quickplay);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        Log.d("CHECKSQLTAG",name);
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
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
                /*while (rs.next()&&ok==false) {
                    if (username.equals(rs.getString(3)) && password.equals(rs.getString(4)))
                        ok = true;
                }*/
                rs.next();
                Log.d("CHECKSQLTAG",name);
                matches_played=rs.getInt(6);
                matches_won=rs.getInt(7);
                matches_lost=rs.getInt(8);
                Log.d("CHECKSQLTAG",String.valueOf((matches_played)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {                        ///Displays the message received

                        TextView MatchesView = (TextView) findViewById(R.id.textMatch);
                        TextView WonView = (TextView) findViewById(R.id.textWin);
                        TextView LostView = (TextView) findViewById(R.id.textLost);
                        TextView RatioView = (TextView) findViewById(R.id.textRatio);
                        MatchesView.setText(String.valueOf("Matches played: "+matches_played));
                        WonView.setText(String.valueOf("Won: "+matches_won));
                        LostView.setText(String.valueOf("Lost: "+matches_lost));
                        if(matches_played!=0) {
                            ratio = (float) (matches_won / matches_played);
                            String ratio2 = Float.toString(ratio);
                            RatioView.setText("RATIO: "+ratio2);
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
