package io.apptizer.nsdclientapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMessageService extends Service {
    private static final String TAG = "ServerMessageService";
    private boolean isRunning  = false;
    private Socket socket;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private Context context;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");

        context = this;
        String mServiceName = intent.getStringExtra("mServiceName");
        String mServiceType = intent.getStringExtra("mServiceType");

        DiscoverServices discoverServices = new DiscoverServices(mServiceType, mServiceName, this.getApplicationContext(), new AsyncTaskCallback() {
            @Override
            public void onTaskCompleted(Object response) {
                Log.d(TAG, "onTaskCompleted: " + response);
                if (response instanceof NsdServiceInfo) {
                    NsdServiceInfo nsdServiceInfoObj = (NsdServiceInfo) response;
                    Log.d(TAG, "nsdServiceInfo: " + nsdServiceInfoObj);

                    try {
                        socket = new Socket(nsdServiceInfoObj.getHost(), nsdServiceInfoObj.getPort());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Log.d("socket : isClosed:", String.valueOf(socket.isClosed()));
                                while (true) {
//                                    Log.d("socket : isClosed:", String.valueOf(socket.isClosed()));
                                    try {
                                        if (socket != null && !socket.isClosed()) {
                                            inputStream = socket.getInputStream();
                                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                                            String s = null;
                                            while ((s = bufferedReader.readLine()) != null) {
                                                Intent local = new Intent();
                                                local.setAction("update.content");
                                                local.putExtra("messageContent", s);
                                                context.sendBroadcast(local);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });
        discoverServices.discover();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }
}
