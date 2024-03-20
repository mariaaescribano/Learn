package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {
    private EditText nom;
    private EditText contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.editTextText);
        contra = findViewById(R.id.editTextTextPassword);

        if(dbhelper.TABLE_USUARIO.equals("null") && dbhelper.TABLE_USUARIO!= null && !dbhelper.TABLE_USUARIO.isEmpty())
            traePosibleUsuarioGuardado();
    }

    public void entrar(View view)
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
            new NetworkTask1().execute(Usuario.direHost+"existeUsuValidez.php");
        }
    }
    public void registraForm(View view)
    {
        Intent inte = new Intent(this, registrar.class);
        startActivity(inte);
    }

    private void traePosibleUsuarioGuardado()
    {

            String[] devBruto = new String[100];
            int contar = 0;
            dbhelper dbHelper = new dbhelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            if (db != null) {
                String[] columnas = {"nombre", "contra"};
                Cursor cursor = db.query("t_usuario", columnas, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    //verificar q existen las tablas
                    int columnIndex = cursor.getColumnIndex("nombre");
                    int columnIndex2 = cursor.getColumnIndex("contra");

                    if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
                        Usuario.nombre = cursor.getString(columnIndex);
                        Usuario.contra = cursor.getString(columnIndex2);

                        /*while (cursor.moveToNext()) {
                            valorRestsNombres = cursor.getString(columnIndex2);

                            // Verifica si el nombre de usuario es igual a 'user' antes de agregar a 'devBruto'
                            if (valorNombre.equals(user)) {
                                devBruto[contar] = valorRestsNombres;
                                contar++;
                            }
                        }*/
                    }
                    cursor.close();
                }
            }

    }

    class NetworkTask1 extends AsyncTask<String, Void, String> {


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
            InputStream caInput = null;
            Context context1 = getApplicationContext();
            caInput = context1.getResources().openRawResource(R.raw.server);
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
            hazlo();//aqui llamamos a


            return null;
        }
    }


    private void hazlo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Usuario.direHost+"existeUsuValidez.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    if(response.equals("no existe"))
                    {
                        Toast.makeText(MainActivity.this, "Usuario no existente", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Usuario.id = Integer.parseInt(response);
                        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent inte = new Intent(MainActivity.this, Principal.class);
                        startActivity(inte);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Usuario mal registrado o no existente", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
}