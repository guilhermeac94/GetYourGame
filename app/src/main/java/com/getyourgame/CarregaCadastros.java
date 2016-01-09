package com.getyourgame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class CarregaCadastros extends AppCompatActivity {

    SQLiteHandler db = new SQLiteHandler(CarregaCadastros.this);
    Util util = new Util();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrega_cadastros);

        if(util.testaConexaoInternet(getApplicationContext())){
            carregaCadastros();
        }else{
            util.msgDialog(CarregaCadastros.this, "Alerta", "Você não está conectado à Internet.");
        }
    }

    public void carregaCadastros(){
        db.delete("estado_avaliacao");
        db.delete("estado_jogo");
        db.delete("estado_transacao");
        db.delete("interesse");
        db.delete("metodo_envio");
        db.delete("nivel");
        db.delete("plataforma");
        Webservice ws = new Webservice();
        new HttpBuscaCadastros(ws.buscaCadastros(),null,Object[].class,"").execute();
    }
    private class HttpBuscaCadastros extends Http {
        public HttpBuscaCadastros(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            Object[] lista = Util.convertToObjectArray(retorno);

            for(Object obj : lista){
                Map<String, String> map = (Map<String, String>) obj;
                System.out.println(String.valueOf(map.get("valor_id")));

                ContentValues content = new ContentValues();
                content.put(map.get("campo_id"), String.valueOf(map.get("valor_id")));
                content.put(map.get("campo_des"), map.get("valor_des"));
                if(map.get("campo_marca")!=null){
                    content.put(map.get("campo_marca"), map.get("valor_marca"));
                }

                db.insert(map.get("tabela"), content);
            }

            Intent intentPreferencia = new Intent(CarregaCadastros.this, Login.class);
            startActivity(intentPreferencia);

            CarregaCadastros.this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_carrega_cadastros, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
