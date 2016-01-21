package com.getyourgame;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.getyourgame.util.Util;

public class ListaInteresse extends TabActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_interesse);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Dou em Troca");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tenho e Vendo");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Quero por Troca");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Quero e Compro");

        tab1.setIndicator("Dou em Troca");
        Intent intentPendentes = new Intent(this, ListaInteresseTab.class);
        Bundle paramPendentes = new Bundle();
        paramPendentes.putInt("id_interesse", 1);
        paramPendentes.putInt("id_usuario", id_usuario);
        paramPendentes.putString("chave_api", chave_api);
        intentPendentes.putExtras(paramPendentes);
        tab1.setContent(intentPendentes);

        tab2.setIndicator("Tenho e Vendo");
        Intent intentAndamento = new Intent(this, ListaInteresseTab.class);
        Bundle paramAndamento = new Bundle();
        paramAndamento.putInt("id_interesse", 2);
        paramAndamento.putInt("id_usuario", id_usuario);
        paramAndamento.putString("chave_api", chave_api);
        intentAndamento.putExtras(paramAndamento);
        tab2.setContent(intentAndamento);

        tab3.setIndicator("Quero por Troca");
        Intent intentConcluidas = new Intent(this, ListaInteresseTab.class);
        Bundle paramConcluidas = new Bundle();
        paramConcluidas.putInt("id_interesse", 3);
        paramConcluidas.putInt("id_usuario", id_usuario);
        paramConcluidas.putString("chave_api", chave_api);
        intentConcluidas.putExtras(paramConcluidas);
        tab3.setContent(intentConcluidas);

        tab4.setIndicator("Quero e Compro");
        Intent intentCanceladas = new Intent(this, ListaInteresseTab.class);
        Bundle paramCanceladas = new Bundle();
        paramCanceladas.putInt("id_interesse", 4);
        paramCanceladas.putInt("id_usuario", id_usuario);
        paramCanceladas.putString("chave_api", chave_api);
        intentCanceladas.putExtras(paramCanceladas);
        tab4.setContent(intentCanceladas);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
    }
}