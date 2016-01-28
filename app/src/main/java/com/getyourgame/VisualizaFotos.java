package com.getyourgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.Map;

public class VisualizaFotos extends AppCompatActivity {

    ProgressBar pbVFCarregando;
    Util util = new Util();
    Bitmap sem_jogo;
    Integer id_usuario;
    String chave_api;
    Integer id_usuario_jogo;

    LinearLayout llVisualizaFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_fotos);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_usuario_jogo = recebe.getInt("id_usuario_jogo");

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);

        pbVFCarregando = (ProgressBar)findViewById(R.id.pbVFCarregando);

        llVisualizaFotos = (LinearLayout) findViewById(R.id.llVisualizaFotos);

        new HttpVisualizaFotos((new Webservice().buscaFotos(id_usuario_jogo)), null, Object[].class, "").execute();
    }

    private class HttpVisualizaFotos extends Http {
        public HttpVisualizaFotos(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            pbVFCarregando.setVisibility(View.GONE);

            if(retorno!=null) {
                Object[] l = Util.convertToObjectArray(retorno);

                for (Object obj : l) {
                    Map<String, String> map = (Map<String, String>) obj;

                    ImageView iv = new ImageView(VisualizaFotos.this);
                    iv.setImageBitmap(map.get("foto").equals("") ? sem_jogo : util.StringToBitMap(map.get("foto")));

                    llVisualizaFotos.addView(iv);
                }
            }
        }
    }
}
