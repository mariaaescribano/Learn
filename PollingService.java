package com.example.learn;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.learn.Usuario;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class PollingService extends Service {

    private static final String TAG = "PollingService";
    static RequestQueue req;
    private static Runnable runnable;
    private static Handler handler = new Handler();
    private PendingIntent pendingIntent;
    private List<String> fans = new ArrayList<>();
    private static final long POLL_INTERVAL = 1500000; // Intervalo de sondeo en milisegundos (5 segundos en este ejemplo)

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        //startPolling();
        // pollDatabase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Comienza el sondeo cuando el servicio se inicia
        startPolling();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Detiene el sondeo cuando el servicio se destruye
        stopPolling();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startPolling() {
        runnable = new Runnable() {
            @Override
            public void run() {
                pollDatabase();
                handler.postDelayed(this, 1500000);
            }
        };

        // Iniciar el primer ciclo del temporizador
        handler.postDelayed(runnable, 1500000);
    }

    private void stopPolling() {
        // Detiene la ejecución del Runnable y el sondeo
        handler.removeCallbacksAndMessages(null);
    }
    private void pollDatabase() {
        if(Usuario.palabrasSinGuardar==true)
        {
            Usuario.palabrasSinGuardar= false;
            Log.d(TAG, "Realizando solicitud de sondeo...");
            for(int i=0; i<Usuario.palabrasSinGuardarLista.size(); i++)
            {
                String[] split = Usuario.palabrasSinGuardarLista.get(i).split(" ");
                Usuario.NetworkTask3 networkTask = new Usuario.NetworkTask3(getApplicationContext());
                networkTask.execute(split[0],split[1]);
            }
        }
    }
}



