package com.example.learn;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class NullX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // No action, accepting all client certificates
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // No action, accepting all server certificates
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // Returning null means we accept all issuers
        return null;
    }
}
