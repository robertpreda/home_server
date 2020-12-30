package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    private Socket clientSocket;
    private EditText IP;
    private EditText port;
    private Button connect;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.IP = findViewById(R.id.ipField);
        this.port = findViewById(R.id.portField);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_LONG).show();
            }
        };

    }

    public void createSocket(View view){
        switch(view.getId()) {
            case R.id.connectButton:
                Thread thread = new Thread(new Runnable() {
                    String serverIP = IP.getText().toString();
                    int portNum = Integer.parseInt(port.getText().toString());
                    @Override
                    public void run() {
                        try {
                            clientSocket = new Socket(serverIP, portNum);
                            SocketHandler.setSocket(clientSocket);
                            Message message = mHandler.obtainMessage();
                            message.sendToTarget();
                            Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                            startActivity(intent);

                        } catch (IOException i) {
                            try{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Can't connect to server", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
                break;
        }
    }

}