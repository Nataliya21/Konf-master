package com.example.konf.API;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class X509_Trust_Manager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {}
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {}
    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
