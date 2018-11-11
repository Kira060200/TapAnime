package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EndRoundsQuickPlay extends AppCompatActivity {
    String name;
    int score=0;
    int round=0;
    boolean playerFound;
    String matchCount,victoryCount,looseCount;
    public static final String EXTRA_MESSAGE = "message";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endrounds);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        Bundle extras = intent.getExtras();
        score = extras.getInt("ScoreVariableName", 0);
        round = extras.getInt("RoundVariableName", 0);
        Thread UpS = new Thread(new UpdateStats());
        UpS.start();
        setRound();
    }
    public void setRound() {
        TextView messageView = (TextView)findViewById(R.id.textDisplay);
        messageView.setText("Congratulations! You scored "+score+" out of "+round+" rounds!");
    }
    public class UpdateStats implements Runnable{
        public void run () {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed to find com.mysql.jdbc.Driver");
            }
            try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers", "newuser", "1234");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select* from user");
                    while (rs.next() && playerFound == false) {
                        if (name.equals(rs.getString(3))) {
                            playerFound = true;
                            matchCount = rs.getString(6);
                            victoryCount = rs.getString(7);
                            looseCount = rs.getString(8);
                        }
                    }
                Statement stamt = con.createStatement();
                    int matches= Integer.parseInt(matchCount);
                    matches++;
                stamt.executeUpdate("update user set qp_matches_played=" + (matches) + " where username='" + name + "'");
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute UpdateStats");
            }
        }
    }
}
