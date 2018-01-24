package com.moocall.moocall.util;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.exception.ErrorException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpGetContent {
    private static final String ERROR = "error";
    private static final String TAG = "HttpGetContent";
    private String NO_AUTHORIZATION_DATA = "No authorization data.";
    private String SIGNATURE_MISMATCH = "Signature mismatch.";
    private Context context;

    class C06221 implements HostnameVerifier {
        C06221() {
        }

        public boolean verify(String hostname, SSLSession session) {
            if (hostname.equalsIgnoreCase("app.moocall.bitgeardev.com") || hostname.equalsIgnoreCase("52.209.54.231")) {
                return true;
            }
            return false;
        }
    }

    class C06232 implements X509TrustManager {
        C06232() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public HttpGetContent(Context context) {
        this.context = context;
    }

    public String postContent(String url, String signatureBalance, String message) throws IOException, ErrorException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList();
        urlParameters.add(new BasicNameValuePair("key", Account.getUsername()));
        urlParameters.add(new BasicNameValuePair("message", message));
        urlParameters.add(new BasicNameValuePair("signature", signatureBalance));
        urlParameters.add(new BasicNameValuePair("ts", String.valueOf(Utils.getUnixTs())));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String str = "";
        while (true) {
            str = rd.readLine();
            if (str == null) {
                return result.toString();
            }
            result.append(str);
        }
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new C06221());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new C06232()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContent(String params) throws IOException, ErrorException {
        Log.d("LINK SERVERA", params);
        long lStartTime = System.currentTimeMillis();
        trustEveryone();
        new BasicHttpParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        HttpResponse response = new DefaultHttpClient().execute(new HttpGet(params));
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            Log.d("ODGOVOR SERVERA", responseString);
            Log.d(TAG, "Elapsed milliseconds: " + (System.currentTimeMillis() - lStartTime));
            out.close();
            if (responseString.toLowerCase().contains(this.SIGNATURE_MISMATCH.toLowerCase()) || responseString.toLowerCase().contains(this.NO_AUTHORIZATION_DATA.toLowerCase())) {
                StorageContainer.removeCredentialsFromPreferences(this.context);
            }
            if (responseString.toLowerCase().contains(ERROR.toLowerCase())) {
                String error = ".";
                try {
                    throw new ErrorException(new JSONObject(responseString).getString(ERROR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return responseString;
        }
        response.getEntity().getContent().close();
        throw new IOException(statusLine.getReasonPhrase());
    }

    public String getContentWithArray(String params, ArrayList<NameValuePair> nameValuePairs) throws IOException, ErrorException {
        Log.d("LINK SERVERA", params);
        trustEveryone();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            Log.d("ODGOVOR SERVERA", responseString);
            out.close();
            if (responseString.toLowerCase().contains(this.SIGNATURE_MISMATCH.toLowerCase()) || responseString.toLowerCase().contains(this.NO_AUTHORIZATION_DATA.toLowerCase())) {
                StorageContainer.removeCredentialsFromPreferences(this.context);
            }
            if (responseString.toLowerCase().contains(ERROR.toLowerCase())) {
                String error = ".";
                try {
                    throw new ErrorException(new JSONObject(responseString).getString(ERROR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return responseString;
        }
        response.getEntity().getContent().close();
        throw new IOException(statusLine.getReasonPhrase());
    }

    public String getLoginContent(String params) throws IOException, ErrorException {
        trustEveryone();
        HttpResponse response = new DefaultHttpClient().execute(new HttpPost(params));
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            Log.d("LINK SERVERA", params);
            Log.d("ODGOVOR SERVERA", responseString);
            out.close();
            if (responseString.toLowerCase().contains(ERROR.toLowerCase())) {
                String error = ".";
                try {
                    throw new ErrorException(new JSONObject(responseString).getString(ERROR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return responseString;
        }
        response.getEntity().getContent().close();
        throw new IOException(statusLine.getReasonPhrase());
    }

    private String patternCompile(String responseString) {
        Matcher m = Pattern.compile("\"error\":\"([^\"]*)\"").matcher(responseString);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
