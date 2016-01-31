package com.getyourgame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.getyourgame.model.Usuario;
import com.getyourgame.util.Util;

public class ListaUsuario extends AppCompatActivity implements ListaUsuarios.OnSelecionaUsuarioListener{

    FragmentManager manager;
    Util util = new Util();
    Integer id_usuario;
    String chave_api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);

        getSupportActionBar().hide();

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        manager = getFragmentManager();
        ListaUsuarios listaUsuarios= new ListaUsuarios();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layoutListaUsuario, listaUsuarios, "lista_usuarios");
        transaction.commit();
    }

    @Override
    public void OnSelecionaUsuario(Usuario usuario) {
        Intent intentTelaJogo = new Intent(ListaUsuario.this,TelaUsuario.class);
        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);
        param.putInt("id_usuario_selec", usuario.getId_usuario());
        intentTelaJogo.putExtras(param);
        startActivity(intentTelaJogo);
    }
}
