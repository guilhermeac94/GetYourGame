package com.getyourgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    ImageView ivPrincFotoUsuario;
    Button btAvaliacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

/*
        id_usuario = 5;
        chave_api = "923798d42ec81ca9f07e3cffd7855748";
        id_usuario = 8;
        chave_api = "d5f01d506ef7f209c66726ea52080435";

*/
        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());
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
                param.putString("redirecionar", "principal");
                contato.putExtras(param);
                startActivity(contato);
            }
        });

        ImageView ivQuadroJogosUsuarios = (ImageView)findViewById(R.id.ivQuadroJogosUsuarios);

        ivQuadroJogosUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listaUsuarioJogo = new Intent(Principal.this, ListaUsuarioJogo.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                listaUsuarioJogo.putExtras(param);
                startActivity(listaUsuarioJogo);
            }
        });


        ImageView ivQuadroOportunidades = (ImageView)findViewById(R.id.ivQuadroOportunidades);

        ivQuadroOportunidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oportunidades = new Intent(Principal.this, Oportunidades.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                oportunidades.putExtras(param);
                startActivity(oportunidades);
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

        ImageView ivQuadroInteresses = (ImageView)findViewById(R.id.ivQuadroInteresses);

        ivQuadroInteresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listaInteresse = new Intent(Principal.this, ListaInteresse.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                listaInteresse.putExtras(param);
                startActivity(listaInteresse);
            }
        });


        ivPrincFotoUsuario = (ImageView) findViewById(R.id.ivPrincFotoUsuario);
        ivPrincFotoUsuario.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                param.putString("foto", util.BitMapToString(((BitmapDrawable) ivPrincFotoUsuario.getDrawable()).getBitmap()));
                redirecionar(Principal.this, CarregaFotoUsuario.class, param);
                return false;
            }
        });

        btAvaliacoes = (Button)findViewById(R.id.btAvaliacoes);
        btAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                param.putInt("id_usuario_aval", id_usuario);
                redirecionar(Principal.this, Avaliacao.class, param);
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
                util.msgDialog(Principal.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    final TextView tvPrincNomeUsuario = (TextView) findViewById(R.id.tvPrincNomeUsuario);
                    final TextView tvPrincEmailUsuario = (TextView) findViewById(R.id.tvPrincEmailUsuario);
                    ivPrincFotoUsuario = (ImageView) findViewById(R.id.ivPrincFotoUsuario);

                    tvPrincNomeUsuario.setText(usuario.getNome());
                    tvPrincEmailUsuario.setText("(" + usuario.getEmail() + ")");
                    ivPrincFotoUsuario.setImageBitmap(usuario.getFoto().equals("") ? sem_usuario : util.StringToBitMap(usuario.getFoto()));
                } else {
                    util.msgDialog(Principal.this, "Alerta", usuario.getMessage());
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
