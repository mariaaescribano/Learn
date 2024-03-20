package com.example.learn;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import java.net.Inet4Address;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class registrar extends AppCompatActivity {
    private EditText nom;
    private EditText contra;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        nom = findViewById(R.id.editTextText4);
        contra = findViewById(R.id.editTextTextPassword4);
    }
    public void Registrar(View view)
    {
        if(nom.getText().toString().isEmpty()&&contra.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Rellena los datos, por favor", Toast.LENGTH_SHORT).show();
        }
        else if(nom.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Pon un nombre, por favor", Toast.LENGTH_SHORT).show();
        }
        else if(contra.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Pon una contraseña, por favor", Toast.LENGTH_SHORT).show();
        }
        else {
            Usuario.nombre = nom.getText().toString();
            Usuario.contra = contra.getText().toString();
            context = getApplicationContext();
            Toast.makeText(this, "Bienvenido "+nom.getText().toString(), Toast.LENGTH_SHORT).show();
            new NetworkTask().execute(Usuario.direHost + "registrarUsuario.php");
        }
    }
    public void volver(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    class NetworkTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = null;
            try {
                cf = CertificateFactory.getInstance("X.509");
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            }
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            InputStream caInput = registrar.context.getResources().openRawResource(R.raw.server);

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
            hazlo();
// Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url;
            try {//pasarle valores para q inserte
                url = new URL(Usuario.direHost+"registrarUsuario.php");
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
                String postData = "nombre=" + URLEncoder.encode(String.valueOf(Usuario.nombre), "UTF-8") +
                        "&contra=" + URLEncoder.encode(Usuario.contra, "UTF-8");

                // Escribir los datos en el OutputStream
                writer.write(postData);
                writer.flush();
                writer.close();
                out.close();

                // Obtener la respuesta del servidor
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                urlConnection.disconnect();

                return null;
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void hazlo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Usuario.direHost+"registrarUsuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    if(response.equals("no existe"))
                    {
                        Toast.makeText(registrar.this, "Usuario no existente", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Usuario.id = Integer.parseInt(response);
                        mensajeGuardarDatosRegistro();
                    }
                } else {
                    Toast.makeText(registrar.this, "Usuario mal registrado o no existente", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", nom.getText().toString());
                params.put("contra", contra.getText().toString());
                return params;
            }
        };
        Context con = getApplicationContext();
        RequestQueue req = Volley.newRequestQueue(con);
        req.add(stringRequest);
    }

    private void guardarDatosRegistro()//xxx no se guarda POR Q?!?!?!
    {
        if(dbhelper.TABLE_USUARIO.equals("null") && !dbhelper.TABLE_USUARIO.isEmpty())
        {
            dbhelper dbHelper = new dbhelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM t_usuario");
        }

        dbhelper dbHelper = new dbhelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_usu", Usuario.id);
        values.put("nombre", nom.getText().toString());
        values.put("contra", contra.getText().toString());
        long newRowId = db.insert("t_usuario", null, values);
        dbHelper.close();
    }
    private void mensajeGuardarDatosRegistro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("IMPORTANTE");
        builder.setMessage("Quieres que recuerde tus datos de usuario?");

        // Botón de "Sí"
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar si el usuario hace clic en "Sí"
                // Puedes poner el código correspondiente aquí
                guardarDatosRegistro();
                Intent intent2 = new Intent(registrar.context, Principal.class);
                startActivity(intent2);
            }
        });

        // Botón de "No"
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar si el usuario hace clic en "No"
                // Puedes poner el código correspondiente aquí
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        builder.show();
    }
}