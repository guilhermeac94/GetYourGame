package com.getyourgame.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.getyourgame.model.EstadoJogo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Guilherme on 05/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static Context context;

    public DataBaseHelper(Context context){
        super(context, "getyourgame", null, DATABASE_VERSION);
        //super(context, Environment.getExternalStorageDirectory().getAbsolutePath()
        //        + "/getyourgame/database/getyourgame", null, DATABASE_VERSION);
        setContext(context);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DataBaseHelper.context = DataBaseHelper.context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createTables(db);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTables(SQLiteDatabase db) throws IOException {
        AssetManager manager = context.getAssets();

        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = manager.open("banco.sql");

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String[] sqls = stringBuilder.toString().split(";");

            for (String sql : sqls) {
                db.execSQL(sql);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public List<EstadoJogo> select() throws Exception {
        List<EstadoJogo> estados = new ArrayList<>();
        EstadoJogo estado = null;
        Cursor cursor = null;

        SQLiteDatabase sqlLite = new DataBaseHelper(context).getReadableDatabase();

        String[] colunas = new String[] { "id_estado_jogo","descricao"};

        cursor = sqlLite.query("estado_jogo", colunas, null, null, null, null, null);

        while (cursor.moveToNext()) {
            estado = new EstadoJogo();
            estado.setId_estado_jogo(cursor.getInt(cursor.getColumnIndex("id_estado_jogo")));
            estado.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            estados.add(estado);
        }

        if (cursor != null)
            cursor.close();

        return estados;
    }

    public long insertEstadoJogo(EstadoJogo estadoJogo) throws Exception {
        SQLiteDatabase sqlLite = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put("id_estado_jogo", estadoJogo.getId_estado_jogo());
        content.put("descricao", estadoJogo.getDescricao());

        return sqlLite.insert("estado_jogo", null, content);
    }
}
