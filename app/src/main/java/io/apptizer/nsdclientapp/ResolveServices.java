package io.apptizer.nsdclientapp;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class ResolveServices implements NsdManager.ResolveListener {
    private static final String TAG = "ResolveServices";
    private AsyncTaskCallback asyncTaskCallback;

    public ResolveServices(AsyncTaskCallback asyncTaskCallback) {
        this.asyncTaskCallback = asyncTaskCallback;
    }

    @Override
    public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Resolve Failed: " + nsdServiceInfo);
        Log.d(TAG, "Service Resolve Failure Code: " + errorCode);
        Log.d(TAG, "===============================================");
    }

    @Override
    public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Resolve Success: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");

        asyncTaskCallback.onTaskCompleted(nsdServiceInfo);
    }
}
