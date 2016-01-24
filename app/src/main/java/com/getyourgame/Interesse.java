package com.getyourgame;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.getyourgame.util.Util;

import java.net.SocketPermission;

public class Interesse extends TabActivity /*implements InteresseTroca.OnTrocaListener, InteresseCompra.OnCompraListener*/{

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    Integer id_jogo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesse);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_jogo = recebe.getInt("id_jogo");

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TENHO");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("QUERO");

        tab1.setIndicator("TENHO");
        Intent interesseTenho = new Intent(this, InteresseTab.class);
        Bundle param1 = new Bundle();
        param1.putInt("tipo", 1);
        param1.putInt("id_usuario", id_usuario);
        param1.putString("chave_api", chave_api);
        param1.putInt("id_jogo", id_jogo);
        interesseTenho.putExtras(param1);
        tab1.setContent(interesseTenho);

        tab2.setIndicator("QUERO");
        Intent interesseQuero = new Intent(this, InteresseTab.class);
        Bundle param2 = new Bundle();
        param2.putInt("tipo", 2);
        param2.putInt("id_usuario", id_usuario);
        param2.putString("chave_api", chave_api);
        param2.putInt("id_jogo", id_jogo);
        interesseQuero.putExtras(param2);
        tab2.setContent(interesseQuero);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }

}
