package com.getyourgame;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.getyourgame.util.Util;

public class ListaTransacao extends TabActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_transacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("PENDENTES");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("ANDAMENTO");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("CONCLUÍDAS");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("CANCELADAS");

        tab1.setIndicator("PENDENTES");
        Intent intentPendentes = new Intent(this, ListaTransacaoTab.class);
        Bundle paramPendentes = new Bundle();
        paramPendentes.putInt("status", 1);
        paramPendentes.putInt("id_usuario", id_usuario);
        paramPendentes.putString("chave_api", chave_api);
        intentPendentes.putExtras(paramPendentes);
        tab1.setContent(intentPendentes);

        tab2.setIndicator("ANDAMENTO");
        Intent intentAndamento = new Intent(this, ListaTransacaoTab.class);
        Bundle paramAndamento = new Bundle();
        paramAndamento.putInt("status", 2);
        paramAndamento.putInt("id_usuario", id_usuario);
        paramAndamento.putString("chave_api", chave_api);
        intentAndamento.putExtras(paramAndamento);
        tab2.setContent(intentAndamento);

        tab3.setIndicator("CONCLUÍDAS");
        Intent intentConcluidas = new Intent(this, ListaTransacaoTab.class);
        Bundle paramConcluidas = new Bundle();
        paramConcluidas.putInt("status", 3);
        paramConcluidas.putInt("id_usuario", id_usuario);
        paramConcluidas.putString("chave_api", chave_api);
        intentConcluidas.putExtras(paramConcluidas);
        tab3.setContent(intentConcluidas);

        tab4.setIndicator("CANCELADAS");
        Intent intentCanceladas = new Intent(this, ListaTransacaoTab.class);
        Bundle paramCanceladas = new Bundle();
        paramCanceladas.putInt("status", 4);
        paramCanceladas.putInt("id_usuario", id_usuario);
        paramCanceladas.putString("chave_api", chave_api);
        intentCanceladas.putExtras(paramCanceladas);
        tab4.setContent(intentCanceladas);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

        final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i)
        {
            final View tabView = tw.getChildTabViewAt(i);
            tabView.setPadding(5,0,5,0);
            final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            tv.setTextSize(12);
            tv.setPadding(0,0,0,0);
        }
    }

}
