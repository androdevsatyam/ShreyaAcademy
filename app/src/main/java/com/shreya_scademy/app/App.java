package com.shreya_scademy.app;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.androidnetworking.AndroidNetworking;
import com.common.managers.NetworkManager;
import com.shreya_scademy.app.data.AppDatabase;
import com.shreya_scademy.app.data.DaoHelper;
import com.shreya_scademy.app.utils.NetworkApiCaller;

import java.util.Collections;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

/**
 * @Authoer Dharmesh
 * @Date 19-03-2022
 * <p>
 * Information
 **/
public class App extends Application {

    private AppDatabase appDatabase;
    private static App instance;
    private NetworkManager networkManager;
    private DaoHelper daoHelper;
    private NetworkApiCaller networkApiCaller;

    @Override
    public void onCreate() {
        super.onCreate();
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        instance = this;
        initDatabase();
        initNetworkApiCaller();
        initNetworkManager();
        initNetworkObserver();
        AndroidNetworking.initialize(this,myUnsafeHttpClient());
    }

    private void initNetworkApiCaller() {
        networkApiCaller = new NetworkApiCaller(daoHelper);
    }

    private void initNetworkManager() {
        networkManager = new NetworkManager(this);
    }

    public static App getInstance() {
        return instance;
    }

    private void initDatabase() {
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "shready_academy_database").build();
        daoHelper = new DaoHelper(appDatabase);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    private void initNetworkObserver() {
        networkManager.setNetworkCallback(isConnected -> {
            Log.d("DDD", " Is Network Connected " + isConnected);
            networkApiCaller.setConnected(isConnected);
            networkApiCaller.updateContentView();
        });
    }

    private static OkHttpClient myUnsafeHttpClient() {

        try {

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {

                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) { }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            //Using TLS 1_2 & 1_1 for HTTP/2 Server requests
            // Note : The following is suitable for my Server. Please change accordingly
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                    .build();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.connectionSpecs(Collections.singletonList(spec));
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
