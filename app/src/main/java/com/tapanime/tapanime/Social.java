package com.tapanime.tapanime;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Social extends AppCompatActivity {
    String name;
    int ch;
    public static final String EXTRA_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_MESSAGE);
    }
    public void mainch(View view) {
        ch=0;
        Intent mainintent = new Intent(this, MainChatActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME",name);
        extras.putInt("EXTRA_CHANNEL",ch);
        Log.d("Intent","0");
        mainintent.putExtras(extras);
        startActivity(mainintent);
    }
    public void ch1(View view){
        ch=1;
        Intent mainintent = new Intent(this, MainChatActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME",name);
        extras.putInt("EXTRA_CHANNEL",ch);
        Log.d("Intent","1");
        mainintent.putExtras(extras);
        startActivity(mainintent);
    }
    public void ch2(View view){
        ch=2;
        Intent mainintent = new Intent(this, MainChatActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME",name);
        extras.putInt("EXTRA_CHANNEL",ch);
        Log.d("Intent","2");
        mainintent.putExtras(extras);
        startActivity(mainintent);
    }
}
