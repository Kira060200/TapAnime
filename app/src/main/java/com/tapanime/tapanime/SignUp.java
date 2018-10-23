package com.tapanime.tapanime;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public int EmailCheck() {
        EditText emailView = (EditText)findViewById(R.id.email);
        String email = emailView.getText().toString();
        if(!email.contains("@"))
            return 0;
        return 1;
    }
    public class setUpSQL implements Runnable{
        public void run () {
            EditText emailView = (EditText) findViewById(R.id.email);
            String email = emailView.getText().toString();
            EditText userView = (EditText) findViewById(R.id.username);
            String username = userView.getText().toString();
            EditText pass1View = (EditText) findViewById(R.id.password);
            String password = pass1View.getText().toString();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.50:3306/chatusers","newuser","1234");
                Statement stmt = con.createStatement();
                stmt.executeUpdate("insert into user(email,username,password) values ('" + email + "' ,'" + username + "' , '" + password + "' );");
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public int PasswordCheck() {
        EditText pass1View = (EditText)findViewById(R.id.password);
        String pass1 = pass1View.getText().toString();
        EditText pass2View = (EditText)findViewById(R.id.password2);
        String pass2 = pass2View.getText().toString();
        if(!pass1.equals(pass2)){
            return 0;
        }
        return 1;
    }
    public void onCreateAccount(View view) {
        boolean ok = true;
        if(EmailCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Invalid email address");
            ok = false;
        }
        if(PasswordCheck()==0) {
            TextView errorView = (TextView) findViewById(R.id.textView);
            errorView.setText("Password doesn't match!");
            ok = false;
        }
        if(ok) {
            Thread sqlThread = new Thread(new setUpSQL());
            sqlThread.start();
            EditText userView = (EditText)findViewById(R.id.username);
            String username =userView.getText().toString();
            Intent intent = new Intent(this, Menu.class);
            intent.putExtra(Menu.EXTRA_MESSAGE, username);
            startActivity(intent);
        }
    }
}
