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

public class EndRoundsCompPlay extends AppCompatActivity {
    String name,opponent;
    int score=0;
    int round=0;
    boolean playerFound,opponentFound;
    String matchCount,victoryCount,looseCount,temp;
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE2 = "message";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endrounds);
        Intent intent = getIntent();
        name = intent.getStringExtra("EXTRA_MESSAGE");
        Bundle extras = intent.getExtras();
        opponent = extras.getString("EXTRA_MESSAGE2");
        score = extras.getInt("ScoreVariableName", 0);
        round = extras.getInt("RoundVariableName", 0);
        Thread UpS = new Thread(new UpdateStats());
        UpS.start();
        Thread UpSO = new Thread(new UpdateOpponent());
        UpSO.start();
        setRound();
    }
    public void setRound() {
        TextView messageView = (TextView)findViewById(R.id.textDisplay);
        messageView.setText("Congratulations! You scored "+score+" out of "+round+" rounds!");
    }
    public class UpdateStats implements Runnable{   //increments match count in the database
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
                        matchCount = rs.getString(10);
                        victoryCount = rs.getString(11);
                        looseCount = rs.getString(12);
                        temp = rs.getString(9);
                    }
                }
                Statement stamt = con.createStatement();
                int matches= Integer.parseInt(matchCount);
                matches++;
                stamt.executeUpdate("update user set cp_matches_played=" + (matches) + " where username='" + name + "'");
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute UpdateStats");
            }
        }
    }
    public class UpdateOpponent implements Runnable{    //checks if the enemy finished the game/left the game
        public void run () {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed to find com.mysql.jdbc.Driver");
            }
            try {
                String optemp="";
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers", "newuser", "1234");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select* from user");
                while (rs.next() && opponentFound == false) {
                    if (opponent.equals(rs.getString(3))) {
                        opponentFound = true;
                        optemp = rs.getString(9);
                    }
                }
                if(Integer.valueOf(optemp)<Integer.valueOf(temp)){
                    Statement stbmt = con.createStatement();
                    int wins= Integer.parseInt(victoryCount);
                    wins++;
                    stbmt.executeUpdate("update user set cp_matches_won=" + (wins) + " where username='" + name + "'");
                } else{
                    Statement stbmt = con.createStatement();
                    int lost= Integer.parseInt(looseCount);
                    lost++;
                    stbmt.executeUpdate("update user set cp_matches_lost=" + (lost) + " where username='" + name + "'");
                }
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute UpdateOpponent");
            }
        }
    }
}
