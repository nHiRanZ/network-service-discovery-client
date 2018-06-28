package io.apptizer.nsdclientapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;

import io.apptizer.nsdclientapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClientActivity";

    private ActivityMainBinding activityMainBinding;
    private String mServiceName = "BIZ_1whe199y29f";
    private final String SERVICE_TYPE = "_http._tcp.";
    private Socket socket;

    Handler updateConversationHandler;
    NsdServiceInfo nsdServiceInfo = null;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Intent serverMessageService = new Intent(this.getApplicationContext(), ServerMessageService.class);
        serverMessageService.putExtra("mServiceType", SERVICE_TYPE);
        serverMessageService.putExtra("mServiceName", mServiceName);
        this.getApplicationContext().startService(serverMessageService);


        IntentFilter filter = new IntentFilter();
        filter.addAction("update.content");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                activityMainBinding.message.setText(intent.getStringExtra("messageContent"));
            }
        };
        registerReceiver(broadcastReceiver,filter);

        getSupportActionBar().hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
