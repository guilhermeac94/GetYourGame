package com.getyourgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class Transacao extends AppCompatActivity {

    Util util = new Util();
    SQLiteHandler db = new SQLiteHandler(Transacao.this);
    DecimalFormat df = new DecimalFormat("0.00");
    Integer id_usuario;
    String chave_api;
    int id_transacao;
    int id_interesse;
    int status;
    Boolean envio_solic;
    Boolean envio_ofert;
    Boolean compra;
    Boolean solicitante;
    List<MetodoEnvio> metodos;
    MetodoEnvio metodo;

    TextView tvTDescricaoJogoOfert;
    TextView tvTPlataformaEstadoJogoOfert;
    TextView tvTMetodoEnvio;
    Spinner spTMetodoEnvioOfert;
    TextView tvTDescricaoJogo;
    TextView tvTPlataformaEstadoJogo;
    Spinner spTMetodoEnvio;
    TextView tvTAguardando;
    Button btTIniciarTransacao;
    Button btTEnviarTransacao;
    Button btTCancelarTransacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_transacao = recebe.getInt("id_transacao");

        tvTDescricaoJogoOfert = (TextView) findViewById(R.id.tvTDescricaoJogoOfert);
        tvTPlataformaEstadoJogoOfert = (TextView) findViewById(R.id.tvTPlataformaEstadoJogoOfert);
        spTMetodoEnvioOfert = (Spinner) findViewById(R.id.spTMetodoEnvioOfert);
        tvTDescricaoJogo = (TextView) findViewById(R.id.tvTDescricaoJogo);
        tvTPlataformaEstadoJogo = (TextView) findViewById(R.id.tvTPlataformaEstadoJogo);
        tvTMetodoEnvio = (TextView) findViewById(R.id.tvTMetodoEnvio);
        spTMetodoEnvio = (Spinner) findViewById(R.id.spTMetodoEnvio);
        tvTAguardando = (TextView) findViewById(R.id.tvTAguardando);
        btTIniciarTransacao = (Button)findViewById(R.id.btTIniciarTransacao);
        btTEnviarTransacao = (Button)findViewById(R.id.btTEnviarTransacao);
        btTCancelarTransacao = (Button)findViewById(R.id.btTCancelarTransacao);

        btTIniciarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("id_estado_transacao", "2");

                metodo = (MetodoEnvio)spTMetodoEnvio.getSelectedItem();
                map.add("id_metodo_envio_ofertante", String.valueOf(metodo.getId_metodo_envio()));

                map.add("tipo_atualizacao", "iniciar");

                new HttpAtualizaTransacao((new Webservice()).atualizaTransacao(id_transacao),map,Object.class,"").execute();
            }
        });

        btTEnviarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

                if(solicitante) {
                    map.add("envio_solicitante", "1");
                }else{
                    map.add("envio_ofertante", "1");
                }

                if((solicitante && envio_ofert) || (!solicitante && envio_solic)){
                    map.add("tipo_atualizacao", "concluir");
                    map.add("id_estado_transacao", "3");
                }else{
                    map.add("tipo_atualizacao", "enviar");
                }

                new HttpAtualizaTransacao((new Webservice()).atualizaTransacao(id_transacao),map,Object.class,"").execute();
            }
        });

        btTCancelarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("id_estado_transacao", "4");
                map.add("tipo_atualizacao", "cancelar");

                new HttpAtualizaTransacao((new Webservice()).atualizaTransacao(id_transacao),map,Object.class,"").execute();
            }
        });


        new HttpBuscaDadosTransacao((new Webservice()).buscaDadosTransacao(id_transacao),null,Object.class,"").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transacao, menu);
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

    private class HttpBuscaDadosTransacao extends Http {
        public HttpBuscaDadosTransacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Map<String, String> map = (Map<String, String>) retorno;

            Object map_status = map.get("id_estado_transacao");
            status = Integer.parseInt(map_status.toString());


            Object map_envio_solic = map.get("envio_solicitante");
            if(Integer.parseInt(map_envio_solic.toString())==1) {
                envio_solic = true;
            }else{
                envio_solic = false;
            }
            Object map_envio_ofert = map.get("envio_ofertante");
            if(Integer.parseInt(map_envio_ofert.toString())==1) {
                envio_ofert = true;
            }else{
                envio_ofert = false;
            }


            Object map_id_interesse = map.get("id_interesse");
            id_interesse = Integer.parseInt(map_id_interesse.toString());

            if(id_interesse==4) {
                compra = true;
            }else{
                compra = false;
            }

            Object map_id_usuario_solic = map.get("id_usuario");
            int id_usuario_solic = Integer.parseInt(map_id_usuario_solic.toString());

            if(id_usuario == id_usuario_solic){
                solicitante = true;
            }else{
                solicitante = false;
            }

            tvTDescricaoJogoOfert.setText(map.get("descricao_jogo_ofert"));
            tvTPlataformaEstadoJogoOfert.setText(map.get("plataforma_jogo_ofert") + (map.get("estado_jogo_ofert")!=null ? " - "+map.get("estado_jogo_ofert") : ""));

            if(compra){
                tvTDescricaoJogo.setText("R$ "+df.format(Double.parseDouble(String.valueOf(map.get("preco_jogo_ofert")))));
                tvTMetodoEnvio.setVisibility(View.GONE);
                spTMetodoEnvio.setVisibility(View.GONE);
            }else{
                tvTDescricaoJogo.setText(map.get("descricao_jogo"));
                tvTPlataformaEstadoJogo.setText(map.get("plataforma_jogo") + (map.get("estado_jogo")!=null ? " - "+map.get("estado_jogo") : ""));
            }

            try {
                metodos = db.selectMetodoEnvio();
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!compra) {
                if (String.valueOf(map.get("metodo_envio")) != null) {
                    util.carregaSpinner(spTMetodoEnvio, Transacao.this, metodos, String.valueOf(map.get("metodo_envio")));
                }else{
                    util.carregaSpinner(spTMetodoEnvio, Transacao.this, metodos, null);
                }
            }

            if(String.valueOf(map.get("metodo_envio_ofert")) != null) {
                util.carregaSpinner(spTMetodoEnvioOfert, Transacao.this, metodos, String.valueOf(map.get("metodo_envio_ofert")));
            }else{
                util.carregaSpinner(spTMetodoEnvioOfert, Transacao.this, metodos, null);
            }

            if(status==1){
                if(solicitante){
                    tvTAguardando.setVisibility(View.VISIBLE);
                }else{
                    btTIniciarTransacao.setVisibility(View.VISIBLE);
                }
            }else if(status==2){

                if((envio_solic && solicitante) || (envio_ofert && !solicitante)){
                    tvTAguardando.setVisibility(View.VISIBLE);
                }else{
                    btTEnviarTransacao.setVisibility(View.VISIBLE);
                }

                if(!envio_solic && !envio_ofert){
                    btTCancelarTransacao.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class HttpAtualizaTransacao extends Http {
        public HttpAtualizaTransacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Map<String, String> map = (Map<String, String>) retorno;

            Object map_error = map.get("error");
            if(!Boolean.parseBoolean(map_error.toString())){
                Intent intent = new Intent(Transacao.this,Principal.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                intent.putExtras(param);
                startActivity(intent);
                util.toast(getApplicationContext(), map.get("message"));
                Transacao.this.finish();
            }
        }
    }
}
