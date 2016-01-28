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
    int id_usuario_jogo;
    int id_usuario_jogo_ofert;
    int id_transacao;
    int id_interesse;
    int status;
    Boolean envio_solic;
    Boolean envio_ofert;
    Boolean compra;
    Boolean solicitante;
    List<MetodoEnvio> metodos;
    MetodoEnvio metodo;
    Integer id_outro_usuario;

    Integer id_usuario_ofert;
    Integer id_usuario_solic;

    TextView tvTNomeUsuarioOfert;
    TextView tvTNomeUsuarioSolic;
    TextView tvTDescricaoJogoOfert;
    TextView tvTPlataformaEstadoJogoOfert;
    TextView tvTMetodoEnvio;
    TextView tvTMetodoEnvioOfert;
    Spinner spTMetodoEnvioOfert;
    TextView tvTDescricaoJogo;
    TextView tvTPlataformaEstadoJogo;
    Spinner spTMetodoEnvio;
    TextView tvTAguardando;
    Button btTIniciarTransacao;
    Button btTEnviarTransacao;
    Button btTCancelarTransacao;
    Button btTRecusarTransacao;
    Button btTAvaliacao;

    TextView tvTFotos;
    TextView tvTFotosOfert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_transacao = recebe.getInt("id_transacao");

        tvTNomeUsuarioSolic = (TextView) findViewById(R.id.tvTNomeUsuarioSolic);
        tvTNomeUsuarioOfert = (TextView) findViewById(R.id.tvTNomeUsuarioOfert);
        tvTDescricaoJogoOfert = (TextView) findViewById(R.id.tvTDescricaoJogoOfert);
        tvTPlataformaEstadoJogoOfert = (TextView) findViewById(R.id.tvTPlataformaEstadoJogoOfert);
        tvTMetodoEnvioOfert = (TextView) findViewById(R.id.tvTMetodoEnvioOfert);
        spTMetodoEnvioOfert = (Spinner) findViewById(R.id.spTMetodoEnvioOfert);
        tvTDescricaoJogo = (TextView) findViewById(R.id.tvTDescricaoJogo);
        tvTPlataformaEstadoJogo = (TextView) findViewById(R.id.tvTPlataformaEstadoJogo);
        tvTMetodoEnvio = (TextView) findViewById(R.id.tvTMetodoEnvio);
        spTMetodoEnvio = (Spinner) findViewById(R.id.spTMetodoEnvio);
        tvTAguardando = (TextView) findViewById(R.id.tvTAguardando);
        btTIniciarTransacao = (Button)findViewById(R.id.btTIniciarTransacao);
        btTEnviarTransacao = (Button)findViewById(R.id.btTEnviarTransacao);
        btTCancelarTransacao = (Button)findViewById(R.id.btTCancelarTransacao);
        btTRecusarTransacao = (Button)findViewById(R.id.btTRecusarTransacao);
        btTAvaliacao = (Button)findViewById(R.id.btTAvaliacao);

        tvTFotos = (TextView) findViewById(R.id.tvTFotos);
        tvTFotosOfert = (TextView) findViewById(R.id.tvTFotosOfert);

        btTIniciarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("id_estado_transacao", "2");

                if(!compra) {
                    metodo = (MetodoEnvio) spTMetodoEnvio.getSelectedItem();
                    map.add("id_metodo_envio_ofertante", String.valueOf(metodo.getId_metodo_envio()));
                }
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

        btTRecusarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAtualizaTransacao((new Webservice()).deletaTransacao(id_transacao),null,Object.class,"").execute();
            }
        });

        btTAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Transacao.this,Avaliar.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                param.putInt("id_transacao", id_transacao);
                param.putInt("id_usuario_avaliador", id_usuario);
                param.putInt("id_usuario_avaliado", id_outro_usuario);

                intent.putExtras(param);
                startActivity(intent);
            }
        });

        new HttpBuscaDadosTransacao((new Webservice()).buscaDadosTransacao(id_transacao),null,Object.class,"").execute();
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

            Object map_id_usuario_jogo = map.get("id_usuario_jogo");
            id_usuario_jogo = Integer.parseInt(map_id_usuario_jogo.toString());

            Object map_id_usuario_jogo_ofert = map.get("id_usuario_jogo_ofert");
            id_usuario_jogo_ofert = Integer.parseInt(map_id_usuario_jogo_ofert.toString());

            tvTFotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    visualizaFotos(id_usuario_jogo);
                }
            });

            tvTFotosOfert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    visualizaFotos(id_usuario_jogo_ofert);
                }
            });

            Object map_qtd_foto = map.get("qtd_foto");
            if(Integer.parseInt(map_qtd_foto.toString()) > 0){
                tvTFotos.setVisibility(View.VISIBLE);
            }

            Object map_qtd_foto_ofert = map.get("qtd_foto_ofert");
            if(Integer.parseInt(map_qtd_foto_ofert.toString()) > 0){
                tvTFotosOfert.setVisibility(View.VISIBLE);
            }


            Object map_id_usuario_solic = map.get("id_usuario");
            id_usuario_solic = Integer.parseInt(map_id_usuario_solic.toString());

            Object map_id_usuario_ofert = map.get("id_usuario_ofert");
            id_usuario_ofert = Integer.parseInt(map_id_usuario_ofert.toString());

            if(id_usuario == id_usuario_solic){
                id_outro_usuario = id_usuario_ofert;
                solicitante = true;
            }else{
                id_outro_usuario = id_usuario_solic;
                solicitante = false;
            }

            tvTNomeUsuarioOfert.setText("Ofertante: "+map.get("nome_ofert"));
            tvTNomeUsuarioOfert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    param.putInt("id_usuario_selec", id_usuario_ofert);
                    Intent intent = new Intent(Transacao.this, ContatoTransacao.class);
                    intent.putExtras(param);
                    startActivity(intent);
                }
            });

            tvTNomeUsuarioSolic.setText("Solicitante: " + map.get("nome"));
            tvTNomeUsuarioSolic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    param.putInt("id_usuario_selec", id_usuario_solic);
                    Intent intent = new Intent(Transacao.this, ContatoTransacao.class);
                    intent.putExtras(param);
                    startActivity(intent);
                }
            });

            tvTDescricaoJogoOfert.setText(map.get("descricao_jogo_ofert"));
            tvTPlataformaEstadoJogoOfert.setText(map.get("plataforma_jogo_ofert") + (map.get("estado_jogo_ofert")!=null ? " - "+map.get("estado_jogo_ofert") : ""));

            if(compra){
                tvTDescricaoJogo.setText("R$ "+df.format(Double.parseDouble(String.valueOf(map.get("preco_jogo_ofert")))));
                tvTMetodoEnvioOfert.setVisibility(View.GONE);
                spTMetodoEnvioOfert.setVisibility(View.GONE);
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
                if(String.valueOf(map.get("metodo_envio_ofert")) != null) {
                    util.carregaSpinner(spTMetodoEnvioOfert, Transacao.this, metodos, String.valueOf(map.get("metodo_envio_ofert")));
                }else{
                    util.carregaSpinner(spTMetodoEnvioOfert, Transacao.this, metodos, null);
                }
            }

            if (String.valueOf(map.get("metodo_envio")) != null) {
                util.carregaSpinner(spTMetodoEnvio, Transacao.this, metodos, String.valueOf(map.get("metodo_envio")));
            }else{
                util.carregaSpinner(spTMetodoEnvio, Transacao.this, metodos, null);
            }

            if(status==1){
                if(solicitante){
                    tvTAguardando.setVisibility(View.VISIBLE);
                }else{
                    btTRecusarTransacao.setVisibility(View.VISIBLE);
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
            }else if(status==3){
                btTAvaliacao.setVisibility(View.VISIBLE);
            }

            if(solicitante){
                spTMetodoEnvioOfert.setEnabled(false);
            }else{
                spTMetodoEnvio.setEnabled(false);
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

    public void visualizaFotos(Integer id_usuario_jogo){
        Intent intent = new Intent(Transacao.this, VisualizaFotos.class);
        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);
        param.putInt("id_usuario_jogo", id_usuario_jogo);
        intent.putExtras(param);
        startActivity(intent);
    }
}
