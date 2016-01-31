package com.getyourgame;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.getyourgame.util.SwipeDetector;
import com.getyourgame.util.Util;

public class ListaUsuarioJogo extends TabActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario_jogo);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Usuário");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Jogo");

        tab1.setIndicator("Usuário");
        Intent intentListaUsuario = new Intent(ListaUsuarioJogo.this,ListaUsuario.class);
        intentListaUsuario.putExtras(param);
        tab1.setContent(intentListaUsuario);

        tab2.setIndicator("Jogo");
        Intent intentListaJogo = new Intent(ListaUsuarioJogo.this,ListaJogo.class);
        intentListaJogo.putExtras(param);
        tab2.setContent(intentListaJogo);

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        View v = (RelativeLayout)this.findViewById(R.id.lista_usuario_jogo);
        new SwipeDetector(v).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
                    Intent mainIntent = new Intent(ListaUsuarioJogo.this,Principal.class);
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    mainIntent.putExtras(param);
                    startActivity(mainIntent);
                    ListaUsuarioJogo.this.finish();
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                }
            }
        });
    }

}
