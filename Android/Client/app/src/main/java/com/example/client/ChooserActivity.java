package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.example.client.ClientActivity.padRight;

public class ChooserActivity extends AppCompatActivity {

    private Button offline, online;
    private DataInputStream input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        offline = findViewById(R.id.bOffline);
        online = findViewById(R.id.bOnline);

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "LOCAL";
                        try {
                            if(input == null)
                                input = new DataInputStream(SocketHandler.getSocket().getInputStream());
                            int songCount = input.readInt();
                            for(int i = 0; i < songCount-1;i++){
                                String temp = input.readUTF();
                                
                            }


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
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                startActivity(intent);
            }
        });
    }
}