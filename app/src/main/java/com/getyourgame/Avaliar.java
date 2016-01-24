package com.getyourgame;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getyourgame.model.AvaliacaoTransacao;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class Avaliar extends AppCompatActivity {

    Util util = new Util();

    Integer id_usuario;
    String chave_api;

    Integer id_avaliacao_transacao = 0;
    Integer id_transacao = 0;
    Integer id_usuario_avaliador = 0;
    Integer id_usuario_avaliado = 0;

    Integer avaliacao = -1;
    String observacao;

    TextView tvAVAjuda;
    ImageView ivAVPositivo;
    ImageView ivAVNegativo;
    EditText etAVObservacao;
    RelativeLayout layoutPositivo;
    RelativeLayout layoutNegativo;
    Button btAVSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();

        id_avaliacao_transacao = recebe.getInt("id_avaliacao_transacao");
        id_transacao = recebe.getInt("id_transacao");
        id_usuario_avaliador = recebe.getInt("id_usuario_avaliador");
        id_usuario_avaliado = recebe.getInt("id_usuario_avaliado");


        tvAVAjuda = (TextView)findViewById(R.id.tvAVAjuda);
        ivAVPositivo = (ImageView)findViewById(R.id.ivAVPositivo);
        ivAVNegativo = (ImageView)findViewById(R.id.ivAVNegativo);
        etAVObservacao = (EditText)findViewById(R.id.etAVObservacao);
        layoutPositivo = (RelativeLayout)findViewById(R.id.layoutPositivo);
        layoutNegativo = (RelativeLayout)findViewById(R.id.layoutNegativo);
        btAVSalvar = (Button)findViewById(R.id.btAVSalvar);

        btAVSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(avaliacao==-1){
                    util.msgDialog(Avaliar.this,"Alerta","Toque em uma opção de avaliação!");
                }else{
                    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

                    if(id_avaliacao_transacao!=0) {
                        map.add("id_avaliacao_transacao", String.valueOf(id_avaliacao_transacao));
                    }
                    map.add("id_transacao", String.valueOf(id_transacao));
                    map.add("id_usuario_avaliador", String.valueOf(id_usuario_avaliador));
                    map.add("id_usuario_avaliado", String.valueOf(id_usuario_avaliado));
                    map.add("avaliacao", String.valueOf(avaliacao));
                    map.add("observacao", etAVObservacao.getText().toString());

                    new HttpAvaliar((new Webservice()).salvaAvaliacao(),map,Object.class,"").execute();
                }
            }
        });

        if(id_avaliacao_transacao==0) {

            ivAVPositivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avaliacao = 1;
                    layoutPositivo.setBackgroundDrawable(getResources().getDrawable(R.drawable.borda));
                    layoutNegativo.setBackgroundResource(0);
                }
            });

            ivAVNegativo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avaliacao = 0;
                    layoutPositivo.setBackgroundResource(0);
                    layoutNegativo.setBackgroundDrawable(getResources().getDrawable(R.drawable.borda));
                }
            });

            btAVSalvar.setVisibility(View.VISIBLE);
        }else{
            tvAVAjuda.setVisibility(View.GONE);
            etAVObservacao.setEnabled(false);
        }

        new HttpBuscaAvaliacao(new Webservice().buscaAvaliacao(id_transacao,id_usuario_avaliador),null,Object.class,"").execute();
    }

    private class HttpAvaliar extends Http {
        public HttpAvaliar(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            Map<String, String> map = (Map<String, String>) retorno;

            Object map_error = map.get("error");
            if(!Boolean.parseBoolean(map_error.toString())){
                util.toast(getApplicationContext(), map.get("message"));
                Avaliar.this.finish();
            }else{
                util.msgDialog(Avaliar.this, "Alerta", map.get("message"));
            }
        }
    }

    private class HttpBuscaAvaliacao extends Http {
        public HttpBuscaAvaliacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno!=null) {

                Map<String, String> map = (Map<String, String>) retorno;

                Object map_id_avaliacao_transacao = map.get("id_avaliacao_transacao");
                id_avaliacao_transacao = Integer.parseInt(map_id_avaliacao_transacao.toString());

                Object map_id_transacao = map.get("id_transacao");
                id_transacao = Integer.parseInt(map_id_transacao.toString());

                Object map_id_usuario_avaliado = map.get("id_usuario_avaliado");
                id_usuario_avaliado = Integer.parseInt(map_id_usuario_avaliado.toString());

                Object map_id_usuario_avaliador = map.get("id_usuario_avaliador");
                id_usuario_avaliador = Integer.parseInt(map_id_usuario_avaliador.toString());

                Object map_avaliacao = map.get("avaliacao");
                avaliacao = Integer.parseInt(map_avaliacao.toString());

                observacao = map.get("observacao");

                if(avaliacao==1){
                    layoutPositivo.setBackgroundDrawable(getResources().getDrawable(R.drawable.borda));
                    layoutNegativo.setBackgroundResource(0);
                }else{
                    layoutPositivo.setBackgroundResource(0);
                    layoutNegativo.setBackgroundDrawable(getResources().getDrawable(R.drawable.borda));
                }
                etAVObservacao.setText(observacao);
            }
        }
    }

}
