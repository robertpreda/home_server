package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {
    private Socket socket;
    private EditText query;
    private Button search;
    private DataOutputStream out;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        this.socket = SocketHandler.getSocket();
        this.query = findViewById(R.id.searchText);
        this.search = findViewById(R.id.searchButton);
    }

    public void sendRequest(View view) {
        switch (view.getId()) {
            case R.id.searchButton:

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = query.getText().toString();
                        try {
                            if(out == null)
                                out = new DataOutputStream(socket.getOutputStream());
                            int textLength = text.length();
                            String textLengthStr = Integer.toString(textLength);
                            int toPad = SocketHandler.getHeader() - textLength;
                            textLengthStr = padRight(textLengthStr, toPad);
                            out.write(textLengthStr.getBytes("UTF-8"));
                            out.write(text.getBytes("UTF-8"));

                        } catch (IOException i) {
                            try{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });

                thread.start();
                break;
        }
    }

    public void disconnect(View view) {
        if (view.getId() == R.id.disconnectFromServer) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String text = "bye";
                        int msgLen = text.length();
                        String msgLenStr = Integer.toString(msgLen);
                        int toPad = SocketHandler.getHeader() - msgLen;
                        msgLenStr = padLeft(msgLenStr, toPad);
                        if (out == null)
                            out = new DataOutputStream(socket.getOutputStream());
                        out.write(msgLenStr.getBytes("UTF-8"));
                        out.write(text.getBytes("UTF-8"));
                        out.close();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } catch (IOException i) {


                    }
                }
//


            });
            thread.start();
        }
    }
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

}