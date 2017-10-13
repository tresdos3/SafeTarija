package com.tarija.tresdos.safetarija.other;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.tarija.tresdos.safetarija.receiver.NetworkReceiver;

public class myaplication extends MultiDexApplication {
    private static myaplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized myaplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkReceiver.ConnectivityReceiverListener listener) {
        NetworkReceiver.connectivityReceiverListener = listener;
    }
}
