package com.getyourgame.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.getyourgame.model.EstadoJogo;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.model.Plataforma;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guilherme on 08/10/2015.
 */
public class SQLiteHandler {

    public DBUser dbUser;

    public SQLiteHandler(Context context) {
        dbUser = new DBUser(context);
    }

    public List<EstadoJogo> selectEstadoJogo() throws Exception {
        List<EstadoJogo> estados = new ArrayList<>();
        EstadoJogo estado = null;
        Cursor cursor = null;

        SQLiteDatabase db = dbUser.getReadableDatabase();

        String[] colunas = new String[] {"id_estado_jogo","descricao"};

        cursor = db.query("estado_jogo", colunas, null, null, null, null, null);

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

    public List<MetodoEnvio> selectMetodoEnvio() throws Exception {
        List<MetodoEnvio> metodos = new ArrayList<>();
        MetodoEnvio metodoEnvio = null;
        Cursor cursor = null;

        SQLiteDatabase db = dbUser.getReadableDatabase();

        String[] colunas = new String[] {"id_metodo_envio","descricao"};

        cursor = db.query("metodo_envio", colunas, null, null, null, null, null);

        while (cursor.moveToNext()) {
            metodoEnvio = new MetodoEnvio();
            metodoEnvio.setId_metodo_envio(cursor.getInt(cursor.getColumnIndex("id_metodo_envio")));
            metodoEnvio.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            metodos.add(metodoEnvio);
        }

        if (cursor != null)
            cursor.close();

        return metodos;
    }

    public List<Plataforma> selectPlataforma() throws Exception {
        List<Plataforma> plataformas = new ArrayList<>();
        Plataforma plataforma = null;
        Cursor cursor = null;

        SQLiteDatabase db = dbUser.getReadableDatabase();

        String[] colunas = new String[] {"id_plataforma","descricao"};

        cursor = db.query("plataforma", colunas, null, null, null, null, null);

        while (cursor.moveToNext()) {
            plataforma = new Plataforma();
            plataforma.setId_plataforma(cursor.getInt(cursor.getColumnIndex("id_plataforma")));
            plataforma.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            plataformas.add(plataforma);
        }

        if (cursor != null)
            cursor.close();

        return plataformas;
    }

    public void insert(String tabela, ContentValues content){
        SQLiteDatabase sqlLite = dbUser.getWritableDatabase();
        sqlLite.insert(tabela, null, content);
        sqlLite.close();
    }

    public void delete(String tabela) {
        SQLiteDatabase sqlLite = dbUser.getWritableDatabase();
        sqlLite.delete(tabela, null, null);
        sqlLite.close();
    }

    public class DBUser extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Gu5";
        private static final int DATABASE_VERSION = 1;
        private Context context;

        public DBUser(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
            setContext(context);
        }

        /*@Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }*/


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
            // TODO Auto-generated method stub
            //db.execSQL(DROP_TABLE);
            onCreate(db);
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}
