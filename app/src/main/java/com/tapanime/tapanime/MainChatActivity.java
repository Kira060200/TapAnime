package com.tapanime.tapanime;
import java.io.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.net.*;
public class MainChatActivity extends AppCompatActivity {
    BufferedReader reader;
    PrintWriter writer;
    String messageText;
    boolean mes=false;
    String username;
    int channel;
    public static final String EXTRA_MESSAGE = "message";
    public static final String CHANNEL = "-1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = extras.getString("EXTRA_USERNAME");
        channel = extras.getInt("EXTRA_CHANNEL");
        Log.d("Channel", String.valueOf(channel));
        TextView myTextView = (TextView)findViewById(R.id.displaymessage);
        myTextView.setMovementMethod(new ScrollingMovementMethod());
        new ConnectServer().execute();
    }
    public void onSendMessage(View view) {      ///Send button
        mes=true;
        EditText messageView1 = (EditText) findViewById(R.id.message);
        messageText = messageView1.getText().toString();
        writer.println(username+": " + messageText);
        writer.flush();
        messageView1.setText("");
    }
    private class ConnectServer extends AsyncTask<Void, Void, Void> {       ///Background task that connects to the server
        private Socket sock,sock2;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sock=new Socket("192.168.0.50", 5000);
                sock2=new Socket("192.168.0.50", 5001);
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                Log.d("Channel", String.valueOf(channel));
                //
                int ch=0;
                ch=channel;
                DataOutputStream isWriter = new DataOutputStream(sock2.getOutputStream());
                isWriter.writeInt(ch);
                isWriter.close();
                //Log.d("CH", String.valueOf(ch));
                //
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                Log.d("InternetTag", "Net established");
                Thread readerThread = new Thread(new IncomingReader());
                readerThread.start();
            }catch (IOException ex){
                ex.printStackTrace();
                Log.d("InternetTag", "Net failed");
            }
            return null;
        }
    }
    public class IncomingReader implements Runnable{            ///Thread that receives incoming messages from the server
        public void run() {
            String message;
            try {
                while ( (message=reader.readLine())!=null) {
                    Log.d("TAG", message);
                    final String finalMessage = message;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {                        ///Displays the message received

                            TextView messageView = (TextView)findViewById(R.id.displaymessage);
                            messageView.append(finalMessage +'\n');

                        }
                    });
                }
                Log.d("TAG","!while");
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

