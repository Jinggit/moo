package com.moocall.moocall.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.moocall.moocall.C0530R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class UrlContent {
    String address;
    Activity mMainActivity;
    String returnData;

    static class C06311 implements HostnameVerifier {
        C06311() {
        }

        public boolean verify(String hostname, SSLSession session) {
            if (hostname.equalsIgnoreCase("app.moocall.bitgeardev.com") || hostname.equalsIgnoreCase("52.209.54.231") || hostname.equalsIgnoreCase("mymoocall.com")) {
                return true;
            }
            return false;
        }
    }

    static class C06322 implements X509TrustManager {
        C06322() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    class C06333 implements OnClickListener {
        C06333() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public UrlContent(Activity mMainActivity) {
        this.mMainActivity = mMainActivity;
    }

    public String getContent(String address) {
        this.address = address;
        processContent(address);
        return this.returnData;
    }

    private void processContent(String address) {
        try {
            if (isNetworkAvailable(this.mMainActivity)) {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(address));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    InputStream ips = response.getEntity().getContent();
                    BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String s = buf.readLine();
                        if (s == null || s.length() == 0) {
                            buf.close();
                            ips.close();
                            this.returnData = sb.toString();
                        } else {
                            sb.append(s);
                        }
                    }
                    buf.close();
                    ips.close();
                    this.returnData = sb.toString();
                    return;
                }
                throw new Exception();
            }
            throw new Exception("No internet connectivity.");
        } catch (Exception e) {
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info == null) {
            return false;
        }
        for (NetworkInfo state : info) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                trustEveryone();
                HttpURLConnection urlc = (HttpURLConnection) new URL(StorageContainer.host).openConnection();
                urlc.setRequestProperty("Account-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
                urlc.connect();
                if (urlc.getResponseCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    return true;
                }
                return false;
            } catch (IOException e) {
                Log.e("internet", "Error checking internet connection", e);
            }
        } else {
            Log.d("no network", "No network available!");
            return false;
        }
    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new C06311());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new C06322()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlertDialog makeAndShowDialogBox(Context context) {
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(C0530R.string.no_internet).setTitle(C0530R.string.error);
        alertDialogBuilder.setPositiveButton("Pokusajte opet", new C06333());
        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();
    }
}
