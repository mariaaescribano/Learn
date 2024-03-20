package com.example.learn;

import static com.example.learn.registrar.context;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class palabraCrear extends AppCompatActivity {
    private boolean ordencambiado = false;
    private EditText palabraChin=null;
    private EditText palabraEsp=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palabra_crear);
        palabraChin = findViewById(R.id.palChin);
        palabraEsp = findViewById(R.id.palChin2);
    }
    public void salvar(View view)
    {
        //se añade a lista INTERNA DEL MOVIL, todavia NO se cuelga en internet
        dbhelper dbHelper = new dbhelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("palabraChino", palabraChin.getText().toString());
        values.put("palabraEspañol", palabraEsp.getText().toString());
        long newRowId = db.insert("t_palabras", null, values);
        dbHelper.close();

        Usuario.palabrasSinGuardarLista.add(palabraChin.getText().toString()+"-"+palabraEsp.getText().toString());
        //guardar palabras en lista para comprobar q se han guardado en BD

        //hacer un Polling Service cada 10 min para ver si hay internet y si se pueden subir las palabras
        if (isOnline()) {
            Usuario.NetworkTask3 networkTask = new Usuario.NetworkTask3(getApplicationContext());
            networkTask.execute(palabraChin.getText().toString(), palabraEsp.getText().toString());
            Usuario.palabrasSinGuardar= false;
        } else {
            Usuario.palabrasSinGuardar= true;
        }

        //plan: colgar palabras en sqlite, cuando haya conexion a internet publicarlas a BD

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void ponLetra(String letra)
    {
        String texto = palabraChin.getText().toString();
        texto +=letra;
        palabraChin.setText(texto);
        palabraChin.requestFocus();
        palabraChin.setSelection(palabraChin.getText().length());
    }
    public void letra1a(View view)
    {
        //pone letra a y se queda el foco al final de palabra
        ponLetra("ā");
    }
    public void letra2a(View view)
    {
        ponLetra("á");
    }
    public void letra3a(View view)
    {
        ponLetra("ǎ");
    }
    public void letra4a(View view)
    {
        ponLetra("à");
    }


    public void letra1e(View view)
    {
        ponLetra("ē");
    }
    public void letra2e(View view)
    {
        ponLetra("é");
    }
    public void letra3e(View view)
    {
        ponLetra("ĕ");
    }
    public void letra4e(View view)
    {
        ponLetra("è");
    }

    public void letra1i(View view)
    {
        ponLetra("ī");
    }
    public void letra2i(View view)
    {
        ponLetra("í");
    }
    public void letra3i(View view)
    {
        ponLetra("ĭ");
    }
    public void letra4i(View view)
    {
        ponLetra("ì");
    }


    public void letra1o(View view)
    {
        ponLetra("ō");
    }
    public void letra2o(View view)
    {
        ponLetra("ó");
    }
    public void letra3o(View view)
    {
        ponLetra("ŏ");
    }
    public void letra4o(View view)
    {
        ponLetra("ò");
    }

    public void letra1u(View view)
    {
        ponLetra("ū");
    }
    public void letra2u(View view)
    {
        ponLetra("ú");
    }
    public void letra3u(View view)
    {
        ponLetra("ŭ");
    }
    public void letra4u(View view)
    {
        ponLetra("ù");
    }
}