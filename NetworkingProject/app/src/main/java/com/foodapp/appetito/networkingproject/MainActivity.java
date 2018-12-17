package com.foodapp.appetito.networkingproject;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);

        Thread myThread =  new Thread(new MyServer());
        myThread.start();
    }

    class MyServer implements Runnable
    {
        ServerSocket ss;
        Socket mySocket;
        DataInputStream dis;
        BufferedReader bufferedReader;
        String message;
        Handler handler=new Handler();
        @Override
        public void run() {
            try {
                ss=new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Waiting for client", Toast.LENGTH_SHORT).show();
                    }
                });
                while (true)
                {
                    mySocket=ss.accept();
                    dis=new DataInputStream(mySocket.getInputStream());
                    message=dis.readUTF();


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Message recieved from client", Toast.LENGTH_SHORT).show();
                            if(message.equals("Open Camera"))
                            {
                                Toast.makeText(MainActivity.this, "Opening Camera on other device", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void button_click(View view)
    {
        BackgroundTask b=new BackgroundTask();
        b.execute(e1.getText().toString(),e2.getText().toString());
    }

    class BackgroundTask extends AsyncTask<String, Void, String>
    {
        Socket s;
        DataOutputStream dataOutputStream;
        String ip, message;
        @Override
        protected String doInBackground(String... strings) {
            ip=strings[0];
            message=strings[1];
            try {
                s=new Socket(ip,9700);
                dataOutputStream=new DataOutputStream(s.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.close();
                s.close();
            } catch (IOException e) {
                 e.printStackTrace();
            }
            return null;
        }
    }
}
