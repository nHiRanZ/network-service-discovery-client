package io.apptizer.nsdclientapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class DiscoverServices implements NsdManager.DiscoveryListener {
    private static final String TAG = "DiscoverServices";
    private String mServiceType;
    private String mServiceName;

    private Context mContext;
    private NsdManager mNsdManager;

    private AsyncTaskCallback asyncTaskCallback;

    public DiscoverServices(String mServiceType, String mServiceName, Context mContext, AsyncTaskCallback asyncTaskCallback) {
        this.mServiceType = mServiceType;
        this.mServiceName = mServiceName;
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

        if (!nsdServiceInfo.getServiceType().equals(mServiceType)) {
            Log.d(TAG, "Unknown Service Type: " + nsdServiceInfo.getServiceType());
        } else if (nsdServiceInfo.getServiceName().contains(mServiceName)){
            resolveFoundService(nsdServiceInfo);
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Service Lost: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");
    }

    /////////////////////////////////////////////////////////////////////////////

    private void resolveFoundService(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "===============================================");
        Log.d(TAG, "Resolving Service: " + nsdServiceInfo);
        Log.d(TAG, "===============================================");
        mNsdManager.resolveService(nsdServiceInfo, new ResolveServices(asyncTaskCallback));
    }
}
