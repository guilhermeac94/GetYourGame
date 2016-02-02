package com.getyourgame.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getyourgame.R;

import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guilherme on 14/09/2015.
 */
public class Util extends Activity{

    public Boolean testaConexaoInternet(Context context){
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }


    public void msgDialog(Activity act, String titulo, String msg){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(act);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(titulo);
        dlgAlert.setNeutralButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void retornaMap(final Object objeto){
        ReflectionUtils.doWithFields(objeto.getClass(), new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(final Field field)
                    throws IllegalArgumentException, IllegalAccessException {

                System.out.println("Field name: " + field.getName());
                field.setAccessible(true);
                System.out.println("Field value: " + field.get(objeto));

            }
        });
    }

    public static Object[] convertToObjectArray(Object array) {
        Class ofArray = array.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                ar.add(Array.get(array, i));
            }
            return ar.toArray();
        }
        else {
            return (Object[]) array;
        }
    }

    public void carregaSpinnerHint(Spinner spinner, Activity act, List objetos ){
        objetos.add("Selecione");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(act, android.R.layout.simple_spinner_item, objetos){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()).toString()); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1;            // you don't display last item. It is used as hint.
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setSelection(spinnerArrayAdapter.getCount());
    }

    public void carregaSpinner(Spinner spinner, Activity act, List objetos, String item){
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(act, android.R.layout.simple_spinner_item, objetos);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        if(item!=null) {
            int spinnerPosition = 0;
            for (int i=0;i<spinner.getCount();i++){
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                    spinnerPosition = i;
                    break;
                }
            }
            spinner.setSelection(spinnerPosition);
        }
    }


    public Integer recebeIdUsuario(Intent intent){
        try{
            Intent dadosRecebidos = intent;

            if(dadosRecebidos != null){
                Bundle recebe = dadosRecebidos.getExtras();
                return recebe.getInt("id_usuario");
            }
        }catch(Exception e){}
        return null;
    }

    public String recebeChaveApi(Intent intent){
        try{
            Intent dadosRecebidos = intent;

            if(dadosRecebidos != null){
                Bundle recebe = dadosRecebidos.getExtras();
                return recebe.getString("chave_api");
            }
        }catch(Exception e){}
        return null;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Notification createNotification(Activity act, Class classe, String titulo, String msg, Bundle param){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(act)
                        .setSmallIcon(R.drawable.ic_seta)
                        .setContentTitle(titulo)
                        .setContentText(msg);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(act, classe);
        resultIntent.putExtras(param);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(act);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(classe);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder.build();
    }

    public String recebeFoto(Intent intent){
        try{
            Intent dadosRecebidos = intent;

            if(dadosRecebidos != null){
                Bundle recebe = dadosRecebidos.getExtras();
                return recebe.getString("foto");
            }
        }catch(Exception e){}
        return null;
    }
}
