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

public class LoadingComp extends AppCompatActivity {
    String name,opponent;
    boolean matchFound=false;
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE2 = "message";
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
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
                Log.d("LOADINGSQLTAG1",name);
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
                while(matchFound==false) {
                    Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers", "newuser", "1234");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select* from user");
                    while (rs.next() && matchFound == false) {
                        if ((rs.getString(5)).equals("2") && !name.equals(rs.getString(3))) {
                            matchFound = true;
                            opponent=rs.getString(3);
                        }
                    }
                    Log.d("MatchFound", String.valueOf(matchFound));
                    if (matchFound) {
                        Intent intent = new Intent(LoadingComp.this, CompetitivePlay.class);
                        Log.d("LOADINGSQLTAG2",name);
                        Bundle bundle = new Bundle();
                        bundle.putString("EXTRA_MESSAGE", name);
                        bundle.putString("EXTRA_MESSAGE2",opponent);
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
                Log.d("SQLTag", "Failed to execute SearchForOpponent");
            }
        }
    }
}
