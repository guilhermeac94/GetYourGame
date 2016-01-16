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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.model.Transacao;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
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
    Boolean compra;
    List<MetodoEnvio> metodos;
    MetodoEnvio metodo;

    TextView tvITNomeUsuarioOfert;
    TextView tvITNomeUsuarioSolic;

    TextView tvITDescricaoJogoOfert;
    TextView tvITPlataformaEstadoJogoOfert;
    TextView tvITMetodoEnvio;
    TextView tvITDescricaoJogo;
    TextView tvITPlataformaEstadoJogo;
    Spinner spITMetodoEnvio;
    TextView tvITMetodoEnvioOfert;
    Spinner spITMetodoEnvioOfert;

    Button btITIniciarTransacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_transacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_usuario_jogo_solic = recebe.getInt("id_usuario_jogo_solic");
        id_usuario_jogo_ofert = recebe.getInt("id_usuario_jogo_ofert");

        tvITNomeUsuarioSolic = (TextView) findViewById(R.id.tvITNomeUsuarioSolic);
        tvITNomeUsuarioOfert = (TextView) findViewById(R.id.tvITNomeUsuarioOfert);
        tvITDescricaoJogoOfert = (TextView) findViewById(R.id.tvITDescricaoJogoOfert);
        tvITPlataformaEstadoJogoOfert = (TextView) findViewById(R.id.tvITPlataformaEstadoJogoOfert);
        tvITMetodoEnvioOfert = (TextView) findViewById(R.id.tvITMetodoEnvioOfert);
        spITMetodoEnvioOfert = (Spinner) findViewById(R.id.spITMetodoEnvioOfert);
        tvITDescricaoJogo = (TextView) findViewById(R.id.tvITDescricaoJogo);
        tvITPlataformaEstadoJogo = (TextView) findViewById(R.id.tvITPlataformaEstadoJogo);
        tvITMetodoEnvio = (TextView) findViewById(R.id.tvITMetodoEnvio);
        spITMetodoEnvio = (Spinner) findViewById(R.id.spITMetodoEnvio);

        btITIniciarTransacao = (Button) findViewById(R.id.btITIniciarTransacao);

        spITMetodoEnvioOfert.setEnabled(false);

        btITIniciarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("id_usuario_jogo_solic", String.valueOf(id_usuario_jogo_solic));
                map.add("id_usuario_jogo_ofert", String.valueOf(id_usuario_jogo_ofert));

                metodo = (MetodoEnvio) spITMetodoEnvio.getSelectedItem();
                map.add("id_metodo_envio_solicitante", String.valueOf(metodo.getId_metodo_envio()));

                new HttpInsereTransacao((new Webservice()).insereTransacao(), map, Object.class, "").execute();
            }
        });


        new HttpBuscaDadosOportunidade((new Webservice()).buscaDadosOportunidade(id_usuario_jogo_solic, id_usuario_jogo_ofert), null, Object.class, "").execute();
    }

    private class HttpBuscaDadosOportunidade extends Http {
        public HttpBuscaDadosOportunidade(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Map<String, String> map = (Map<String, String>) retorno;

            Object map_id_interesse = map.get("id_interesse");
            id_interesse = Integer.parseInt(map_id_interesse.toString());

            if (id_interesse == 4) {
                compra = true;
            } else {
                compra = false;
            }

            tvITNomeUsuarioOfert.setText("Ofertante: "+map.get("nome_ofert"));
            tvITNomeUsuarioSolic.setText("Solicitante: "+map.get("nome"));


            tvITDescricaoJogoOfert.setText(map.get("descricao_jogo_ofert"));
            tvITPlataformaEstadoJogoOfert.setText(map.get("plataforma_jogo_ofert") + (map.get("estado_jogo_ofert") != null ? " - " + map.get("estado_jogo_ofert") : ""));

            if (compra) {
                tvITDescricaoJogo.setText("R$ " + df.format(Double.parseDouble(String.valueOf(map.get("preco_jogo_ofert")))));
                tvITMetodoEnvioOfert.setVisibility(View.GONE);
                spITMetodoEnvioOfert.setVisibility(View.GONE);
            } else {
                tvITDescricaoJogo.setText(map.get("descricao_jogo"));
                tvITPlataformaEstadoJogo.setText(map.get("plataforma_jogo") + (map.get("estado_jogo") != null ? " - " + map.get("estado_jogo") : ""));
            }

            try {
                metodos = db.selectMetodoEnvio();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!compra) {
                if (String.valueOf(map.get("metodo_envio_ofert")) != null) {
                    util.carregaSpinner(spITMetodoEnvioOfert, IniciarTransacao.this, metodos, String.valueOf(map.get("metodo_envio_ofert")));
                } else {
                    util.carregaSpinner(spITMetodoEnvioOfert, IniciarTransacao.this, metodos, null);
                }

            }

            if (String.valueOf(map.get("metodo_envio")) != null) {
                util.carregaSpinner(spITMetodoEnvio, IniciarTransacao.this, metodos, String.valueOf(map.get("metodo_envio")));
            } else {
                util.carregaSpinner(spITMetodoEnvio, IniciarTransacao.this, metodos, null);
            }
        }
    }

    private class HttpInsereTransacao extends Http {
        public HttpInsereTransacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            Intent intent = new Intent(IniciarTransacao.this, Principal.class);
            Bundle param = new Bundle();
            param.putInt("id_usuario", id_usuario);
            param.putString("chave_api", chave_api);
            intent.putExtras(param);
            startActivity(intent);
            util.toast(getApplicationContext(), "Transação iniciada! Aguarde a aceitação do outro usuário.");
            IniciarTransacao.this.finish();
        }
    }
}
