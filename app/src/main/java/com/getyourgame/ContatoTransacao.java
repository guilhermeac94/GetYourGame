package com.getyourgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.Map;

public class ContatoTransacao extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    Bitmap sem_usuario;
    Integer id_usuario_selec;

    ImageView ivCTFotoUsuario;
    TextView tvCTNomeUsuario;
    TextView tvCTEmailUsuario;
    EditText etCTLogradouro;
    EditText etCTCep;
    EditText etCTBairro;
    EditText etCTUF;
    EditText etCTCidade;
    EditText etCTNumero;
    EditText etCTComplemento;
    EditText etCTDDD;
    EditText etCTTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_transacao);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());
        id_usuario_selec = getIntent().getExtras().getInt("id_usuario_selec");

        sem_usuario = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_user);

        ivCTFotoUsuario = (ImageView) findViewById(R.id.ivCTFotoUsuario);
        tvCTNomeUsuario = (TextView) findViewById(R.id.tvCTNomeUsuario);
        tvCTEmailUsuario = (TextView) findViewById(R.id.tvCTEmailUsuario);
        etCTLogradouro = (EditText) findViewById(R.id.etCTLogradouro);
        etCTCep = (EditText) findViewById(R.id.etCTCep);
        etCTBairro = (EditText) findViewById(R.id.etCTBairro);
        etCTUF = (EditText) findViewById(R.id.etCTUF);
        etCTCidade = (EditText) findViewById(R.id.etCTCidade);
        etCTNumero = (EditText) findViewById(R.id.etCTNumero);
        etCTComplemento = (EditText) findViewById(R.id.etCTComplemento);
        etCTDDD = (EditText) findViewById(R.id.etCTDDD);
        etCTTelefone = (EditText) findViewById(R.id.etCTTelefone);

        new HttpBuscaContatoTransacao((new Webservice()).buscaContatoTransacao(id_usuario_selec),null,Object.class,"").execute();
    }


    private class HttpBuscaContatoTransacao extends Http {
        public HttpBuscaContatoTransacao(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(ContatoTransacao.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                if (retorno != null) {
                    Map<String, String> map = (Map<String, String>) retorno;

                    tvCTNomeUsuario.setText(map.get("nome"));
                    tvCTEmailUsuario.setText("(" + map.get("email") + ")");
                    ivCTFotoUsuario.setImageBitmap(map.get("foto").equals("") ? sem_usuario : util.StringToBitMap(map.get("foto")));

                    if (map.get("logradouro") != null) {
                        etCTLogradouro.setText(map.get("logradouro"));
                    }
                    if (map.get("cep") != null) {
                        etCTCep.setText(String.valueOf(map.get("cep")));
                    }
                    if (map.get("bairro") != null) {
                        etCTBairro.setText(map.get("bairro"));
                    }
                    if (map.get("cidade") != null) {
                        etCTCidade.setText(map.get("cidade"));
                    }
                    if (map.get("uf") != null) {
                        etCTUF.setText(map.get("uf"));
                    }
                    if (map.get("numero") != null) {
                        etCTNumero.setText(String.valueOf(map.get("numero")));
                    }
                    if (map.get("complemento") != null) {
                        etCTComplemento.setText(map.get("complemento"));
                    }
                    if (map.get("ddd") != null) {
                        etCTDDD.setText(String.valueOf(map.get("ddd")));
                    }
                    if (map.get("telefone") != null) {
                        etCTTelefone.setText(String.valueOf(map.get("telefone")));
                    }
                }
            }
        }
    }
}
