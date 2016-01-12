package com.getyourgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getyourgame.model.Endereco;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.SwipeDetector;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public class Principal extends AppCompatActivity{

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    Bitmap sem_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*
        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());
        */
        id_usuario = 5;
        chave_api = "923798d42ec81ca9f07e3cffd7855748";

        sem_usuario = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_user);

        //final TextView tvPrincNomeUsuario = (TextView) findViewById(R.id.tvPrincNomeUsuario);

        View v = (RelativeLayout)this.findViewById(R.id.principal);
        new SwipeDetector(v).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT) {
                    Intent mainIntent = new Intent(Principal.this, ListaUsuarioJogo.class);
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    mainIntent.putExtras(param);
                    startActivity(mainIntent);
                    Principal.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }else if(swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
                    Intent mainIntent = new Intent(Principal.this, Oportunidades.class);
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    mainIntent.putExtras(param);
                    startActivity(mainIntent);
                    Principal.this.finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }
        });

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id_usuario", String.valueOf(id_usuario));
        Usuario usuario = new Usuario();
        Webservice ws = new Webservice();
        new HttpCadastro(ws.buscaUsuario(id_usuario),null,Usuario.class,chave_api).execute();

        Button btCadastrarInteresse = (Button) findViewById(R.id.btCadastrarInteresse);
        btCadastrarInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpBuscaEndereco((new Webservice()).buscaEndereco(id_usuario),null,Endereco.class,"").execute();
            }
        });

        TextView tvPrincPreferencias = (TextView) findViewById(R.id.tvPrincPreferencias);

        tvPrincPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferencia = new Intent(Principal.this, PreferenciaUsuario.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                preferencia.putExtras(param);
                startActivity(preferencia);
            }
        });

        TextView tvPrincContato = (TextView) findViewById(R.id.tvPrincContato);

        tvPrincContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contato = new Intent(Principal.this, Contatos.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                contato.putExtras(param);
                startActivity(contato);
            }
        });

        ImageView ivQuadroTransacoes = (ImageView)findViewById(R.id.ivQuadroTransacoes);

        ivQuadroTransacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listaTransacao = new Intent(Principal.this, ListaTransacao.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                listaTransacao.putExtras(param);
                startActivity(listaTransacao);
            }
        });
    }

    private class HttpCadastro extends Http {
        public HttpCadastro(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            Usuario usuario = (Usuario) retorno;
            if(!usuario.getError()) {
                final TextView tvPrincNomeUsuario = (TextView) findViewById(R.id.tvPrincNomeUsuario);
                final TextView tvPrincEmailUsuario = (TextView) findViewById(R.id.tvPrincEmailUsuario);
                ImageView ivPrincFotoUsuario = (ImageView) findViewById(R.id.ivPrincFotoUsuario);

                tvPrincNomeUsuario.setText(usuario.getNome());
                tvPrincEmailUsuario.setText("("+usuario.getEmail()+")");
                ivPrincFotoUsuario.setImageBitmap(usuario.getFoto().equals("") ? sem_usuario : util.StringToBitMap(usuario.getFoto()));
            }else{
                util.msgDialog(Principal.this, "Alerta", usuario.getMessage());
            }
        }
    }

    private class HttpBuscaEndereco extends Http {
        public HttpBuscaEndereco(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            Endereco endereco = (Endereco) retorno;

            Bundle param = new Bundle();
            param.putInt("id_usuario", id_usuario);
            param.putString("chave_api", chave_api);

            if(!endereco.getError()) {
                Intent interesse = new Intent(Principal.this, Interesse.class);
                interesse.putExtras(param);
                startActivity(interesse);
            }else{
                param.putString("msg_interesse", "msg_interesse");
                Intent contato = new Intent(Principal.this, Contatos.class);
                contato.putExtras(param);
                startActivity(contato);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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
        if (id == R.id.action_meu_perfil) {
            Bundle param = new Bundle();
            param.putInt("id_usuario", id_usuario);
            param.putString("chave_api", chave_api);
            redirecionar(Principal.this, testePicture.class, param);
            return true;
        }

        if (id == R.id.action_multiple_fotos) {
            Bundle param = new Bundle();
            param.putInt("id_usuario", id_usuario);
            param.putString("chave_api", chave_api);
            redirecionar(Principal.this, testeMultipleImages.class, param);
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
