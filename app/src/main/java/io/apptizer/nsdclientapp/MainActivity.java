package io.apptizer.nsdclientapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import io.apptizer.nsdclientapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClientActivity";

    private ActivityMainBinding activityMainBinding;
    private String mServiceName = "BIZ_1no8197w237_PA";
    private final String SERVICE_TYPE = "_http._tcp.";
    private Context mContext;

    private ServerSocket serverSocket;
    private Socket socket;

    Handler updateConversationHandler;


    NsdServiceInfo nsdServiceInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        DiscoverServices discoverServices = new DiscoverServices(SERVICE_TYPE, this.getApplicationContext(), new AsyncTaskCallback() {
            @Override
            public void onTaskCompleted(Object response) {
                Log.d(TAG, "onTaskCompleted: " + response);
                if (response instanceof NsdServiceInfo) {
                    NsdServiceInfo nsdServiceInfoObj = (NsdServiceInfo) response;
                    Log.d(TAG, "nsdServiceInfo: " + nsdServiceInfoObj);

                    updateConversationHandler = new Handler();
                    nsdServiceInfo = nsdServiceInfoObj;
                    Thread serverThread = new Thread(new ClientThread());
                    serverThread.start();

                }
            }
        });
        discoverServices.discover();
    }

    class ClientThread implements Runnable {

        public void run() {
            if (nsdServiceInfo != null) {
                Socket socket = null;
                try {
                    socket = new Socket(nsdServiceInfo.getHost(), nsdServiceInfo.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        InputStream input = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        final String line = reader.readLine();    // reads a line of text
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activityMainBinding.message.setText(line);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
