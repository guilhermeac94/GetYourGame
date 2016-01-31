package com.getyourgame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.getyourgame.model.Jogo;
import com.getyourgame.util.Util;

public class ListaJogo extends AppCompatActivity implements ListaJogos.OnSelecionaJogoListener{

    FragmentManager manager;
    Util util = new Util();
    Integer id_usuario;
    String chave_api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jogo);

        getSupportActionBar().hide();

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        manager = getFragmentManager();
        ListaJogos listaJogos = new ListaJogos();
        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);
        listaJogos.setArguments(param);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layoutListaJogo, listaJogos, "lista_jogos");
        transaction.commit();
    }


    @Override
    public void OnSelecionaJogo(Jogo jogo) {
        Intent intentTelaJogo = new Intent(ListaJogo.this,TelaJogo.class);
        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);
        param.putInt("id_jogo", jogo.getId_jogo());
        intentTelaJogo.putExtras(param);
        startActivity(intentTelaJogo);
    }
}
