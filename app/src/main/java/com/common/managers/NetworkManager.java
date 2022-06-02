package com.common.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class NetworkManager {

    private MutableLiveData<Boolean> networkData;
    private final Context context;
    private NetworkCallback mNetworkCallback;

    public MutableLiveData<Boolean> getNetworkData() {
        return networkData = (networkData == null) ? new MutableLiveData<>(isConnected(context)) : networkData;
    }

    public void setNetworkCallback(NetworkCallback networkCallback) {
        this.mNetworkCallback = networkCallback;
    }

    public NetworkManager(Context context) {
        this.context = context;
        // More here
        // https://developer.android.com/training/monitoring-device-state/connectivity-status-type#java
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        // Connectivity manager to access network service
        ConnectivityManager connectivityManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager = context.getSystemService(ConnectivityManager.class);
        } else {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        // network callback to handle network event turn off or turn on
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (networkData != null) networkData.postValue(true);
                if (mNetworkCallback != null) {
                    mNetworkCallback.onNetworkChanged(true);
                }

            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (networkData != null) networkData.postValue(false);
                if (mNetworkCallback != null) {
                    mNetworkCallback.onNetworkChanged(true);
                }
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
//                final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        };
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

    public static boolean isConnected(Context context) {
        // Connectivity manager to access network service
        ConnectivityManager cm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm = context.getSystemService(ConnectivityManager.class);
        } else {
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (nc != null) {
                    if (nc.hasTransport((NetworkCapabilities.TRANSPORT_WIFI))) {
                        return true;
                    } else if (nc.hasTransport((NetworkCapabilities.TRANSPORT_CELLULAR))) {
                        return true;
                    } else return nc.hasTransport((NetworkCapabilities.TRANSPORT_ETHERNET));
                }
            } else {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        return networkInfo.isConnected();
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        return networkInfo.isConnected();
                    }
                }
            }
        }
        return false;
    }

    public interface NetworkCallback {
        public void onNetworkChanged(boolean isConnected);
    }

}
