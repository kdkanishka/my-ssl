package com.example.ssl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class HttpsApacheExample {
    public static void testXXX() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://api.basware.com/peppol/as2");
        CloseableHttpResponse response = httpClient.execute(get);
        System.out.println(response.toString());
        httpClient.close();
        response.close();
    }

    public static void testUrlConnection() throws IOException {
        URL url = new URL("https://api.basware.com/peppol/as2");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoInput(true);
        InputStream is = conn.getInputStream();
        byte []b = new byte[1000];
        is.read(b);
        System.out.println("Data " + new String(b));

    }

    public static void test2() throws KeyManagementException, NoSuchAlgorithmException, IOException {
        SSLContext sslcontext = null;
        sslcontext = SSLContexts.custom().useTLS().build();
        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) { /* nothing */ }

            public void checkServerTrusted(X509Certificate[] certs, String authType) { /* nothing */ }
        }}, new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());

        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setRoutePlanner(routePlanner)
                .build();

        HttpGet get = new HttpGet("https://api.basware.com/peppol/as2");
        //post.setEntity(new ByteArrayEntity("test".getBytes()));
        CloseableHttpResponse response = httpclient.execute(get);
        byte b[] = new byte[20];
        response.getEntity().getContent().read(b);
        System.out.println("Test2 -------->");
        System.out.println(new String(b));
        httpclient.close();
        response.close();
    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
//        test1();
        test2();
//        testUrlConnection();
    }
}
