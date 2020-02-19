package com.example.konf.API;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class Naive_SSLSocketFactory extends SSLSocketFactory {
    protected SSLContext Cur_SSL_Context = SSLContext.getInstance("TLS");
    public Naive_SSLSocketFactory()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        super(null, null, null, null, null, null);
        Cur_SSL_Context.init(null, new TrustManager[]{new X509_Trust_Manager()}, null);
    }
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException
    {
        return Cur_SSL_Context.getSocketFactory().createSocket(socket, host, port, autoClose);
    }
    @Override
    public Socket createSocket() throws IOException
    {
        return Cur_SSL_Context.getSocketFactory().createSocket();
    }
}

