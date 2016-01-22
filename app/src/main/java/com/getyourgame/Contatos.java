package com.getyourgame;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class Contatos extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;

    EditText etLogradouro;
    EditText etCep;
    EditText etBairro;
    EditText etCidade;
    EditText etUF;
    EditText etNumero;
    EditText etComplemento;
    EditText etDDD;
    EditText etTelefone;

    Integer id_jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());
        id_jogo = getIntent().getExtras().getInt("id_jogo");

        if(getIntent().getExtras().getString("msg_interesse")!=null){
            util.msgDialog(Contatos.this, "Alerta", "Você precisa cadastrar um endereço antes de cadastrar interesses!");
        }


        etLogradouro = (EditText) findViewById(R.id.etLogradouro);
        etCep = (EditText) findViewById(R.id.etCep);
        etBairro = (EditText) findViewById(R.id.etBairro);
        etCidade = (EditText) findViewById(R.id.etCidade);
        etUF = (EditText) findViewById(R.id.etUF);
        etNumero = (EditText) findViewById(R.id.etNumero);
        etComplemento = (EditText) findViewById(R.id.etComplemento);
        etDDD = (EditText) findViewById(R.id.etDDD);
        etTelefone = (EditText) findViewById(R.id.etTelefone);

        new HttpBuscaContatos((new Webservice()).buscaContatos(id_usuario),null,Object.class,"").execute();

        Button btContato = (Button) findViewById(R.id.btContato);

        btContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

                if(!etLogradouro.getText().toString().equals("")){
                    map.add("logradouro",etLogradouro.getText().toString());
                }
                if(!etCep.getText().toString().equals("")){
                    map.add("cep",etCep.getText().toString());
                }
                if(!etBairro.getText().toString().equals("")){
                    map.add("bairro",etBairro.getText().toString());
                }
                if(!etCidade.getText().toString().equals("")){
                    map.add("cidade",etCidade.getText().toString());
                }
                if(!etUF.getText().toString().equals("")){
                    map.add("uf",etUF.getText().toString());
                }
                if(!etNumero.getText().toString().equals("")){
                    map.add("numero",etNumero.getText().toString());
                }
                if(!etComplemento.getText().toString().equals("")){
                    map.add("complemento", etComplemento.getText().toString());
                }
                if(!etDDD.getText().toString().equals("")){
                    map.add("ddd", etDDD.getText().toString());
                }
                if(!etTelefone.getText().toString().equals("")){
                    map.add("telefone", etTelefone.getText().toString());
                }

                new HttpSalvaContatos((new Webservice()).salvaContatos(id_usuario),map,Object.class,"").execute();
            }
        });
    }


    private class HttpBuscaContatos extends Http {
        public HttpBuscaContatos(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno!=null) {
                Map<String, String> map = (Map<String, String>) retorno;

                if (map.get("logradouro") != null) {
                    etLogradouro.setText(map.get("logradouro"));
                }
                if (map.get("cep") != null) {
                    etCep.setText(String.valueOf(map.get("cep")));
                }
                if (map.get("bairro") != null) {
                    etBairro.setText(map.get("bairro"));
                }
                if (map.get("cidade") != null) {
                    etCidade.setText(map.get("cidade"));
                }
                if (map.get("uf") != null) {
                    etUF.setText(map.get("uf"));
                }
                if (map.get("numero") != null) {
                    etNumero.setText(String.valueOf(map.get("numero")));
                }
                if (map.get("complemento") != null) {
                    etComplemento.setText(map.get("complemento"));
                }
                if (map.get("ddd") != null) {
                    etDDD.setText(String.valueOf(map.get("ddd")));
                }
                if (map.get("telefone") != null) {
                    etTelefone.setText(String.valueOf(map.get("telefone")));
                }
            }
        }
    }

    private class HttpSalvaContatos extends Http {
        public HttpSalvaContatos(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            Map<String, String> map = (Map<String, String>) retorno;

            if(Boolean.parseBoolean(((Object)map.get("error")).toString())){
                util.msgDialog(Contatos.this, "Alerta", map.get("message"));
            }else{
                util.toast(getApplicationContext(), map.get("message"));

                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);

                String redirecionar = getIntent().getExtras().getString("redirecionar");

                switch (redirecionar) {
                    case "preferencias_usuario":{
                        Intent intent = new Intent(Contatos.this, PreferenciaUsuario.class);
                        intent.putExtras(param);
                        startActivity(intent);
                        break;
                    }
                    case "principal":{
                        Intent intent = new Intent(Contatos.this, Principal.class);
                        intent.putExtras(param);
                        startActivity(intent);
                        break;
                    }
                    case "tela_jogo":{
                        Intent intent = new Intent(Contatos.this, TelaJogo.class);
                        param.putInt("id_jogo", id_jogo);
                        intent.putExtras(param);
                        startActivity(intent);
                        break;
                    }
                    case "lista_interesse":{
                        Intent intent = new Intent(Contatos.this, ListaInteresse.class);
                        intent.putExtras(param);
                        startActivity(intent);
                        break;
                    }
                }
                Contatos.this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
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
