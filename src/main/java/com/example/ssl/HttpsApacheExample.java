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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.net.ProxySelector;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class HttpsApacheExample {
    public static void test1() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://api.basware.com/peppol/as2");
        CloseableHttpResponse response = httpClient.execute(get);
        System.out.println(response.toString());
        httpClient.close();
        response.close();
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

        HttpPost post = new HttpPost("https://api.basware.com/peppol/as2");
        post.setEntity(new ByteArrayEntity("test".getBytes()));
        CloseableHttpResponse response = httpclient.execute(post);
        System.out.println(response.toString());
        httpclient.close();
        response.close();
    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        test1();
        test2();
    }
}
