package com.getyourgame;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class IniciarTransacao extends AppCompatActivity {

    Util util = new Util();
    SQLiteHandler db = new SQLiteHandler(IniciarTransacao.this);
    DecimalFormat df = new DecimalFormat("0.00");
    Integer id_usuario;
    String chave_api;
    int id_usuario_jogo_solic;
    int id_usuario_jogo_ofert;
    int id_interesse;
    Boolean solicitante;
    Boolean compra;
    List<MetodoEnvio> metodos;

    TextView tvTDescricaoJogoOfert;
    TextView tvTPlataformaEstadoJogoOfert;
    TextView tvTMetodoEnvio;
    Spinner spTMetodoEnvioOfert;
    TextView tvTDescricaoJogo;
    TextView tvTPlataformaEstadoJogo;
    Spinner spTMetodoEnvio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_transacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_usuario_jogo_solic = recebe.getInt("id_usuario_jogo_solic");
        id_usuario_jogo_ofert = recebe.getInt("id_usuario_jogo_ofert");

        tvTDescricaoJogoOfert = (TextView) findViewById(R.id.tvTDescricaoJogoOfert);
        tvTPlataformaEstadoJogoOfert = (TextView) findViewById(R.id.tvTPlataformaEstadoJogoOfert);
        spTMetodoEnvioOfert = (Spinner) findViewById(R.id.spTMetodoEnvioOfert);
        tvTDescricaoJogo = (TextView) findViewById(R.id.tvTDescricaoJogo);
        tvTPlataformaEstadoJogo = (TextView) findViewById(R.id.tvTPlataformaEstadoJogo);
        tvTMetodoEnvio = (TextView) findViewById(R.id.tvTMetodoEnvio);
        spTMetodoEnvio = (Spinner) findViewById(R.id.spTMetodoEnvio);

        new HttpBuscaDadosOportunidade((new Webservice()).buscaDadosOportunidade(id_usuario_jogo_solic,id_usuario_jogo_ofert),null,Object.class,"").execute();
    }

    private class HttpBuscaDadosOportunidade extends Http {
        public HttpBuscaDadosOportunidade(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Map<String, String> map = (Map<String, String>) retorno;

            Object map_id_usuario = map.get("id_usuario");
            if(id_usuario == Integer.parseInt(map_id_usuario.toString())){
                solicitante = true;
            }else{
                solicitante = false;
            }

            Object map_id_interesse = map.get("id_interesse");
            id_interesse = Integer.parseInt(map_id_interesse.toString());

            if(id_interesse==4) {
                compra = true;
            }else{
                compra = false;
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

            if(solicitante){
                if(!compra) {
                    if (String.valueOf(map.get("prefer_metodo_envio")) != null) {
                        util.carregaSpinner(spTMetodoEnvio, IniciarTransacao.this, metodos, String.valueOf(map.get("prefer_metodo_envio")));
                    }
                }
                spTMetodoEnvioOfert.setEnabled(false);
            }else{
                if(String.valueOf(map.get("prefer_metodo_envio_ofert")) != null) {
                    util.carregaSpinner(spTMetodoEnvioOfert, IniciarTransacao.this, metodos, String.valueOf(map.get("prefer_metodo_envio_ofert")));
                }
                spTMetodoEnvio.setEnabled(false);
            }
        }
    }
}
