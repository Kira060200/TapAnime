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

public class Loading extends AppCompatActivity {
    String name;
    public static final String EXTRA_MESSAGE = "message";
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
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
                stmt.executeUpdate("update user set status="+"'1'"+" where username='"+name+"'");
                Log.d("LOADINGSQLTAG",name);
                con.close();
                //check=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
}
