package com.getyourgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Cadastro extends AppCompatActivity {

    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etNome = (EditText) findViewById(R.id.etNome);
        final EditText etSenha = (EditText) findViewById(R.id.etSenha);
        final EditText etConfirmarSenha = (EditText) findViewById(R.id.etConfirmarSenha);

        etNome.requestFocus();

        Button btCadastrar = (Button) findViewById(R.id.btCadastro);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(String.valueOf(etSenha.getText().toString()).equals(String.valueOf(etConfirmarSenha.getText().toString()))) {

                    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                    map.add("nome", String.valueOf(etNome.getText().toString()));
                    map.add("email", String.valueOf(etEmail.getText().toString()));
                    map.add("senha", String.valueOf(etSenha.getText().toString()));

                    Webservice ws = new Webservice();

                    new HttpCadastro(ws.cadastro(), map, Usuario.class, "").execute();

                }else{
                    util.msgDialog(Cadastro.this, "Alerta", "As senhas são diferentes!");
                }
            }
        });
    }

    private class HttpCadastro extends Http {
        public HttpCadastro(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Cadastro.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", usuario.getId_usuario());
                    param.putString("chave_api", usuario.getChave_api());
                    param.putString("redirecionar", "preferencias_usuario");
                    redirecionar(Cadastro.this, Contatos.class, param);
                    util.toast(getApplicationContext(), "Usuário cadastrado com sucesso!");
                    Cadastro.this.finish();
                } else {
                    util.msgDialog(Cadastro.this, "Alerta", usuario.getMessage());
                }
            }
        }
    }


    public void redirecionar(Activity atual, Class destino, Bundle param){
        Intent intentPrincipal = new Intent(atual, destino);
        intentPrincipal.putExtras(param);
        startActivity(intentPrincipal);
    }

}
