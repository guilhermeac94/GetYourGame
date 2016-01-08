package com.getyourgame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.EstadoJogo;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class PreferenciaUsuario extends AppCompatActivity {

    Util util = new Util();
    SQLiteHandler db = new SQLiteHandler(PreferenciaUsuario.this);
    List<EstadoJogo> estados;
    List<MetodoEnvio> metodos;
    Integer id_usuario;
    String chave_api;
    Spinner spEstadoJogo;
    Spinner spMetodoEnvio;
    Switch gps;
    SeekBar distancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencia_usuario);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        spEstadoJogo = (Spinner) findViewById(R.id.spEstadoJogo);
        spMetodoEnvio = (Spinner) findViewById(R.id.spMetodoEnvio);

        new HttpBuscaPreferencias((new Webservice()).buscaPreferencias(id_usuario),null,Object.class,"").execute();

        SeekBar seekBar = (SeekBar) findViewById(R.id.sbDistancia);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tvDistanciaProgresso = (TextView) findViewById(R.id.tvDistanciaProgresso);
                tvDistanciaProgresso.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        gps = (Switch) findViewById(R.id.swGPS);
        distancia = (SeekBar) findViewById(R.id.sbDistancia);

        Button btSalvar = (Button) findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("gps", String.valueOf(gps.isChecked() ? 1 : 0));
                MetodoEnvio metodoEnvio = (MetodoEnvio) spMetodoEnvio.getSelectedItem();
                EstadoJogo estadoJogo = (EstadoJogo) spEstadoJogo.getSelectedItem();

                map.add("id_metodo_envio",String.valueOf(metodoEnvio.getId_metodo_envio()));
                map.add("id_estado_jogo", String.valueOf(estadoJogo.getId_estado_jogo()));
                map.add("distancia",String.valueOf(distancia.getProgress()));

                Webservice ws = new Webservice();
                new HttpAtualizaUsuario(ws.atualizarUsuario(id_usuario),map,Usuario.class,"").execute();
            }
        });
    }

    private class HttpBuscaPreferencias extends Http {
        public HttpBuscaPreferencias(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            Map<String, String> map = (Map<String, String>) retorno;

            try {
                estados = db.selectEstadoJogo();
                metodos = db.selectMetodoEnvio();

            }catch (Exception e){
                e.printStackTrace();
            }

            if(map.get("desc_estado_jogo")!=null){
                util.carregaSpinner(spEstadoJogo, PreferenciaUsuario.this, estados, map.get("desc_estado_jogo"));
            }else{
                util.carregaSpinner(spEstadoJogo, PreferenciaUsuario.this, estados, null);
            }
            if(map.get("desc_metodo_envio")!=null) {
                util.carregaSpinner(spMetodoEnvio, PreferenciaUsuario.this, metodos, map.get("desc_metodo_envio"));
            }else{
                util.carregaSpinner(spMetodoEnvio, PreferenciaUsuario.this, metodos, null);
            }
            if(map.get("gps")!=null) {
                Object gpsInt = map.get("gps");
                gps.setChecked(Integer.parseInt(gpsInt.toString()) == 1 ? true : false);
            }
            if(map.get("distancia")!=null) {
                Object distanciaInt = map.get("distancia");
                distancia.setProgress(Integer.parseInt(distanciaInt.toString()));
            }
        }
    }

    private class HttpAtualizaUsuario extends Http {
        public HttpAtualizaUsuario(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            Usuario usuario = (Usuario) retorno;
            if(!usuario.getError()) {
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                redirecionar(PreferenciaUsuario.this, Principal.class, param);

                util.toast(getApplicationContext(), "Preferências salvas com sucesso!");
                PreferenciaUsuario.this.finish();
            }else{
                util.msgDialog(PreferenciaUsuario.this, "Alerta", usuario.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferencia_usuario, menu);
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

    public void redirecionar(Activity atual, Class destino, Bundle param){
        Intent intentPrincipal = new Intent(atual, destino);
        intentPrincipal.putExtras(param);
        startActivity(intentPrincipal);
    }

}
