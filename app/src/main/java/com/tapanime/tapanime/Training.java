package com.tapanime.tapanime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Training extends AppCompatActivity {
    String name,QuestionId="0";
    boolean ok=false;
    boolean correct=false;
    boolean clicked=false;
    boolean exit=false;
    int score=0;
    int round=0;
    int time=15;
    String question="Question Not Found?",answer1,answer2,answer3,CorrectAnswer;
    public static final String EXTRA_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
        Bundle extras = intent.getExtras();
        try {
            score = extras.getInt("ScoreVariableName", 0);
            round = extras.getInt("RoundVariableName", 0);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread sqlThread = new Thread(new setUpSQL());
        sqlThread.start();
        while (ok==false)
        {

        }
        round++;
        setScore();
        setRound();
        setQuestion();
        setAnswer1();
        setAnswer2();
        setAnswer3();
        Thread checkThread = new Thread(new recall());
        checkThread.start();
        new ServerTime().execute();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            exit=true;

        }
        return super.onKeyDown(keyCode, event);
    }
    public void setRound() {
        TextView messageView = (TextView)findViewById(R.id.Roundboard);
        messageView.setText("Round:"+round);
    }
    public void setScore() {
        TextView messageView = (TextView)findViewById(R.id.Scoreboard);
        messageView.setText("Score:"+score);
    }
    public void setQuestion() {
        TextView messageView = (TextView)findViewById(R.id.textQuestion);
        messageView.setText(question);
    }
    public void setAnswer1() {
        Button btn = (Button) findViewById(R.id.ans1);
        btn.setText(answer1);
    }
    public void setAnswer2() {
        Button btn = (Button) findViewById(R.id.ans2);
        btn.setText(answer2);
    }
    public void setAnswer3() {
        Button btn = (Button) findViewById(R.id.ans3);
        btn.setText(answer3);
    }
    public void CheckAnswer1(View view) {
        TextView errorView = (TextView) findViewById(R.id.mark);
        if(answer1.equals(CorrectAnswer)) {
            correct = true;
            errorView.setText("Correct!");
            score++;
        }
        else {
            errorView.setText("Wrong!");
        }
        clicked=true;
        Button btn1 = (Button) findViewById(R.id.ans1);
        Button btn2 = (Button) findViewById(R.id.ans2);
        Button btn3 = (Button) findViewById(R.id.ans3);
        btn1.setClickable(false);
        btn2.setClickable(false);
        btn3.setClickable(false);
    }
    public void CheckAnswer2(View view) {
        TextView errorView = (TextView) findViewById(R.id.mark);
        if(answer2.equals(CorrectAnswer)){
            correct = true;
            errorView.setText("Correct!");
            score++;
        }
        else {
            errorView.setText("Wrong!");
        }
        clicked=true;
        Button btn1 = (Button) findViewById(R.id.ans1);
        Button btn2 = (Button) findViewById(R.id.ans2);
        Button btn3 = (Button) findViewById(R.id.ans3);
        btn1.setClickable(false);
        btn2.setClickable(false);
        btn3.setClickable(false);
    }
    public void CheckAnswer3(View view) {
        TextView errorView = (TextView) findViewById(R.id.mark);
        if(answer3.equals(CorrectAnswer)){
            correct = true;
            errorView.setText("Correct!");
            score++;
        }
        else {
            errorView.setText("Wrong!");
        }
        clicked=true;
        Button btn1 = (Button) findViewById(R.id.ans1);
        Button btn2 = (Button) findViewById(R.id.ans2);
        Button btn3 = (Button) findViewById(R.id.ans3);
        btn1.setClickable(false);
        btn2.setClickable(false);
        btn3.setClickable(false);
    }
    public class recall implements Runnable{
        public void run() {
            while (clicked == false && time > 0 && exit == false) {

            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (exit == false) {
                if (round < 10) {
                    Intent intent = new Intent(Training.this, Training.class);
                    intent.putExtra(Menu.EXTRA_MESSAGE, name);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ScoreVariableName", score);
                    bundle.putInt("RoundVariableName", round);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Training.this, EndRounds.class);
                    intent.putExtra(Menu.EXTRA_MESSAGE, name);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ScoreVariableName", score);
                    bundle.putInt("RoundVariableName", round);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
    private class ServerTime extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voids) {
            try{
                Thread timeThread = new Thread(new setTimer());
                timeThread.start();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }
    public class setTimer implements Runnable{
        public void run(){
                for(int i=14;i>=0;i--)
                {
                    time--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    //TextView timerView = (TextView) findViewById(R.id.counter);
                    //timerView.setText(time);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {                        ///Displays the message received

                            TextView timerView = (TextView) findViewById(R.id.counter);
                            timerView.setText(""+time);
                        }
                    });

                }
        }
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
                //ResultSet rs = stmt.executeQuery("select* from questions where id="+QuestionId);
                ResultSet rs = stmt.executeQuery("select* from questions");
                int randomId = new Random().nextInt(4) + 1;
                while(randomId!=0)
                {
                    rs.next();
                    randomId--;
                }
                question=rs.getString(2);
                Log.d("Question",question);
                CorrectAnswer=rs.getString(3);
                final int random = new Random().nextInt(3) + 1;
                if(random==3) {
                    answer1 = rs.getString(2 + random);
                    answer2 = rs.getString(2 + random-1);
                    answer3 = rs.getString(random);
                }
                else if(random==2){
                    answer1 = rs.getString(2 + random);
                    answer2 = rs.getString(2 + random-1);
                    answer3 = rs.getString(2 + random+1);
                }
                    else {
                        answer1 = rs.getString(3);
                        answer2 = rs.getString(4);
                        answer3 = rs.getString(5);
                }
                ok=true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.d("SQLTag", "Failed to execute 2");
            }
        }
    }
}
