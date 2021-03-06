package com.getyourgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    TextView tvITFotos;
    TextView tvITFotosOfert;

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

        tvITFotos = (TextView) findViewById(R.id.tvITFotos);
        tvITFotosOfert = (TextView) findViewById(R.id.tvITFotosOfert);

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

        tvITFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizaFotos(id_usuario_jogo_solic);
            }
        });

        tvITFotosOfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizaFotos(id_usuario_jogo_ofert);
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
            if(retorno instanceof Exception){
                util.msgDialog(IniciarTransacao.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Map<String, String> map = (Map<String, String>) retorno;

                Object map_id_interesse = map.get("id_interesse");
                id_interesse = Integer.parseInt(map_id_interesse.toString());

                if (id_interesse == 4) {
                    compra = true;
                } else {
                    compra = false;
                }

                tvITNomeUsuarioOfert.setText("Ofertante: " + map.get("nome_ofert"));
                tvITNomeUsuarioSolic.setText("Solicitante: " + map.get("nome"));


                tvITDescricaoJogoOfert.setText(map.get("descricao_jogo_ofert"));
                tvITPlataformaEstadoJogoOfert.setText(map.get("plataforma_jogo_ofert") + (map.get("estado_jogo_ofert") != null ? " - " + map.get("estado_jogo_ofert") : ""));

                if (compra) {
                    tvITDescricaoJogo.setText("R$ " + df.format(Double.parseDouble(String.valueOf(map.get("preco_jogo_ofert")))));
                    tvITMetodoEnvioOfert.setVisibility(View.GONE);
                    spITMetodoEnvioOfert.setVisibility(View.GONE);
                } else {
                    if(map.get("descricao_jogo")!=null) {
                        tvITDescricaoJogo.setText(map.get("descricao_jogo"));
                        tvITPlataformaEstadoJogo.setText(map.get("plataforma_jogo") + (map.get("estado_jogo") != null ? " - " + map.get("estado_jogo") : ""));
                    }else{
                        tvITDescricaoJogo.setText("(Qualquer)");
                        tvITPlataformaEstadoJogo.setText((map.get("estado_jogo") != null ? " - " + map.get("estado_jogo") : ""));
                    }
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

                Object map_qtd_foto = map.get("qtd_foto");
                if (Integer.parseInt(map_qtd_foto.toString()) > 0) {
                    tvITFotos.setVisibility(View.VISIBLE);
                }

                Object map_qtd_foto_ofert = map.get("qtd_foto_ofert");
                if (Integer.parseInt(map_qtd_foto_ofert.toString()) > 0) {
                    tvITFotosOfert.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class HttpInsereTransacao extends Http {
        public HttpInsereTransacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(IniciarTransacao.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
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

    public void visualizaFotos(Integer id_usuario_jogo){
        Intent intent = new Intent(IniciarTransacao.this, VisualizaFotos.class);
        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);
        param.putInt("id_usuario_jogo", id_usuario_jogo);
        intent.putExtras(param);
        startActivity(intent);
    }
}
