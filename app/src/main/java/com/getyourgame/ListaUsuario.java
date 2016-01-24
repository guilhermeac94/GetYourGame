package com.getyourgame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Map;

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
