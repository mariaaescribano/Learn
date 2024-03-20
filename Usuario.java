package com.example.learn;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class Usuario {
    public static String nombre;
    public static String contra;
    public static int id;
    public static List<String> palabrasSinGuardarLista = new ArrayList<>(); //orden: chino--esp
    public static boolean palabrasSinGuardar=false;
    //uni: 172.27.200.204
    //casa : 192.168.1.134


    public static String direHost = "https://"+"192.168.1.134"+"/learnbd/";//casa
    //public static String direHost = "https://"+"172.27.200.204"+"/learnbd/";//uni



    public static class NetworkTask3 extends AsyncTask<String, Void, String> {
        private Context mContext;
        private String palChin = null;
        private String palEsp = null;

        public NetworkTask3(Context context) {
            mContext = context.getApplicationContext(); // Usamos getApplicationContext() para evitar memory leaks
        }

        @Override
        protected String doInBackground(String... params) {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            palChin = params[0];
            palEsp = params[1];

            CertificateFactory cf = null;

            try {
                cf = CertificateFactory.getInstance("X.509");
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            }
            if (palChin != null && palEsp != null) {

                InputStream caInput = mContext.getResources().openRawResource(R.raw.server);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                } catch (CertificateException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        caInput.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = null;
                try {
                    keyStore = KeyStore.getInstance(keyStoreType);
                } catch (KeyStoreException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    keyStore.load(null, null);
                } catch (CertificateException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    keyStore.setCertificateEntry("ca", ca);
                } catch (KeyStoreException ex) {
                    throw new RuntimeException(ex);
                }

                TrustManager[] trustAllCerts = new TrustManager[]{new NullX509TrustManager()};

                // Create an SSLContext that uses the TrustManager
                SSLContext sslContext;
                try {
                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, trustAllCerts, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                HostnameVerifier customHostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        try {
                            X509Certificate[] serverCertificates = (X509Certificate[]) session.getPeerCertificates();
                            //String commonName = getCommonName(serverCertificates[0]);
                        } catch (SSLPeerUnverifiedException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                };
                HttpsURLConnection.setDefaultHostnameVerifier(customHostnameVerifier);

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url;
                try {//pasarle valores para q inserte
                    url = new URL(Usuario.direHost + "guardaPalabra.php");
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

                    urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());//
                    urlConnection.setHostnameVerifier(customHostnameVerifier);//

                    // Continue with your code...
                    // Configurar la conexión para método POST
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);

                    // Crear un OutputStream para enviar datos al servidor
                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                    // Crear una cadena con los parámetros a enviar
                    String postData = "id=" + id +
                            "&palEsp=" + URLEncoder.encode(palEsp, "UTF-8") +
                            "&palChino=" + URLEncoder.encode(palChin, "UTF-8");

                    // Escribir los datos en el OutputStream
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    out.close();

                    // Obtener la respuesta del servidor
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    urlConnection.disconnect();
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
    }
}
