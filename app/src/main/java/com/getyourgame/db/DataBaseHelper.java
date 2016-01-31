package com.getyourgame.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Guilherme on 05/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static Context context;

    public DataBaseHelper(Context context){
        super(context, "getyourgame", null, DATABASE_VERSION);
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
}
