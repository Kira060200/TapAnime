package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WaitingForOpponent extends AppCompatActivity {
    String name,opponent;
    int score=0;
    int round=0;
    boolean opponentFinished=false;
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE2 = "message";
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Intent intent = getIntent();
        name = intent.getStringExtra("EXTRA_MESSAGE");
        Bundle extras = intent.getExtras();
        opponent = extras.getString("EXTRA_MESSAGE2");
        score = extras.getInt("ScoreVariableName", 0);
        round = extras.getInt("RoundVariableName", 0);
        Log.d("CHECKSQLTAG",name);
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        Thread searchThread = new Thread(new SearchForOpponent());
        searchThread.start();
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
                stmt.executeUpdate("update user set status="+"'2'"+" where username='"+name+"'");
                stmt.executeUpdate("update user set temp="+score+" where username='"+name+"'");
                Log.d("LOADINGSQLTAG",name);
                con.close();
                //check=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public class SearchForOpponent implements Runnable{
        public void run () {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed to find com.mysql.jdbc.Driver");
            }
            try {
                while(opponentFinished==false) {
                    Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers", "newuser", "1234");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select* from user");
                    while (rs.next() && opponentFinished == false) {
                        if ((rs.getString(5)).equals("2") && opponent.equals(rs.getString(3))) {
                            opponentFinished = true;
                        }
                    }
                    Log.d("OpponentFinished", String.valueOf(opponentFinished));
                    if (opponentFinished) {
                        Intent intent = new Intent(WaitingForOpponent.this, EndRoundsQuickPlay.class);
                        intent.putExtra("EXTRA_MESSAGE", name);
                        Bundle bundle = new Bundle();
                        bundle.putString("EXTRA_MESSAGE2",opponent);
                        bundle.putInt("ScoreVariableName", score);
                        bundle.putInt("RoundVariableName", round);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Statement stamt = con.createStatement();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        stamt.executeUpdate("update user set status=" + "'0'" + " where username='" + name + "'");
                        finish();
                    }
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute OpponentSearch");
            }
        }
    }
}

