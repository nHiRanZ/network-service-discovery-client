package io.apptizer.nsdclientapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class DiscoverServices implements NsdManager.DiscoveryListener, NsdManager.ResolveListener {
    private static final String TAG = "DiscoverServices";
    private String mServiceType;

    private Context mContext;
    private NsdManager mNsdManager;

    private AsyncTaskCallback asyncTaskCallback;

    public DiscoverServices(String mServiceType, Context mContext, AsyncTaskCallback asyncTaskCallback) {
        this.mServiceType = mServiceType;
        this.mContext = mContext;
        this.asyncTaskCallback = asyncTaskCallback;
    }

    public void discover() {
        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(mServiceType, NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public void onStartDiscoveryFailed(String s, int i) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Start Discovery Failed: " + s);
        Log.d(TAG, "Service Start Discovery Failure Code: " + i);
        Log.d(TAG, "===============================================");
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onStopDiscoveryFailed(String s, int i) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Stop Discovery Failed: " + s);
        Log.d(TAG, "Service Stop Discovery Failure Code: " + i);
        Log.d(TAG, "===============================================");
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Discovery Started: " + regType);
        Log.d(TAG, "===============================================");
    }

    @Override
    public void onDiscoveryStopped(String s) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Discovery Stopped: " + s);
        Log.d(TAG, "===============================================");
    }

    @Override
    public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Discovery Success: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");

        resolveFoundService(nsdServiceInfo);
    }

    @Override
    public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Lost: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");
    }

    /////////////////////////////////////////////////////////////////////////////

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

    private void resolveFoundService(NsdServiceInfo nsdServiceInfo) {
        mNsdManager.resolveService(nsdServiceInfo, this);
    }
}
