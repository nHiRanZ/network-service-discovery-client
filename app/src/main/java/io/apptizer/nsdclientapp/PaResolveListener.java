package io.apptizer.nsdclientapp;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.TextView;

import java.net.InetAddress;

import io.apptizer.nsdclientapp.databinding.ActivityMainBinding;

public class PaResolveListener implements NsdManager.ResolveListener {
    private static final String TAG = "PaResolveListener";
    private String mServiceName;
    private ActivityMainBinding activityMainBinding;

    public PaResolveListener(String mServiceName, ActivityMainBinding activityMainBinding) {
        this.mServiceName = mServiceName;
        this.activityMainBinding = activityMainBinding;
    }

    @Override
    public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Resolve Failed: " + nsdServiceInfo);
        Log.d(TAG, "Service Resolve Failure Code: " + i);
        Log.d(TAG, "===============================================");
    }

    @Override
    public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Resolve Success: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");

//        if (nsdServiceInfo.getServiceName().equals(mServiceName)) {
//            Log.d(TAG, "Same IP.");
//            return;
//        }
        int port = nsdServiceInfo.getPort();
        InetAddress host = nsdServiceInfo.getHost();

        Log.d(TAG, "host: " + host);
        Log.d(TAG, "port: " + port);

        StringBuilder stringBuilder = new StringBuilder(host + ":" + port);

        activityMainBinding.message.setText(stringBuilder);
        Log.d(TAG, "===============================================");
    }

//    private void onConnect(NsdServiceInfo nsdServiceInfo) {
//        if (nsdServiceInfo != null) {
//            Log.d(TAG, "Connecting.");
//            mConnection.connectToServer(nsdServiceInfo.getHost(),
//                    nsdServiceInfo.getPort());
//        } else {
//            Log.d(TAG, "No service to connect to!");
//        }
//    }
}
