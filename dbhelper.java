package com.example.learn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
public class dbhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =2;
    private static final String DATABASE_NOMBRE = "libreta.db";
    public static final String TABLE_PALABRAS = "t_palabras";
    public static final String TABLE_USUARIO = "t_usuario";
    public dbhelper(@Nullable Context context)
    {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PALABRAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "palabraChino TEXT NOT NULL,"+
                "palabraEspañol TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_usu INT NOT NULL,"+
                "nombre TEXT NOT NULL,"+
                "contra TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
