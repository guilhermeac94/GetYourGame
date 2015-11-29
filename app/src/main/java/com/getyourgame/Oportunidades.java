package com.getyourgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getyourgame.util.Http;
import com.getyourgame.util.SwipeDetector;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Map;

public class Oportunidades extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;

    ListView lvOportunidades;
    Ladapter adapter;
    ArrayList<Item> lista;
    String filtro;
    Bitmap sem_jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidades);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        lista = new ArrayList();
        lvOportunidades = (ListView) findViewById(R.id.lvOportunidades);

        View v = (RelativeLayout)this.findViewById(R.id.oportunidades);
        new SwipeDetector(v).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT) {
                    Intent mainIntent = new Intent(Oportunidades.this,Principal.class);
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    mainIntent.putExtras(param);
                    startActivity(mainIntent);
                    Oportunidades.this.finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }
            }
        });

        new HttpBuscaOportunidades((new Webservice().buscaOportunidades(id_usuario)), null, Object[].class, "").execute();
    }

    private class HttpBuscaOportunidades extends Http {
        public HttpBuscaOportunidades(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Object[] l = Util.convertToObjectArray(retorno);

            for(Object obj : l) {
                Map<String, String> map = (Map<String, String>) obj;

                lista.add(new Item(Integer.parseInt(String.valueOf(map.get("id_interesse"))),
                        Integer.parseInt(String.valueOf(map.get("id_usuario_jogo"))),
                        Integer.parseInt(String.valueOf(map.get("id_jogo"))),
                        Integer.parseInt(String.valueOf(map.get("id_usuario"))),
                        map.get("descricao"),
                        map.get("nome"),
                        map.get("foto").equals("")?sem_jogo : util.StringToBitMap(map.get("foto"))));
            }
            adapter = new Ladapter(getApplicationContext());
            lvOportunidades.setAdapter(adapter);

            lvOportunidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Item item = lista.get(i);
                }
            });
        }
    }

    class Item {
        int id_interesse;
        int id_usuario_jogo;
        int id_jogo;
        int id_usuario;
        String descricao;
        String nome;
        Bitmap foto;

        public Item(int id_interesse, int id_usuario_jogo, int id_jogo, int id_usuario, String descricao, String nome, Bitmap foto) {
            this.id_interesse = id_interesse;
            this.id_usuario_jogo = id_usuario_jogo;
            this.id_jogo = id_jogo;
            this.id_usuario = id_usuario;
            this.descricao = descricao;
            this.nome = nome;
            this.foto = foto;
        }
    }

    class Ladapter extends BaseAdapter {

        Context c;
        myViewHolder holder;

        public Ladapter(Context context) {
            // TODO Auto-generated constructor stub
            this.c = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return lista.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class myViewHolder {
            TextView nomeUsuario;
            TextView descricaoJogo;
            ImageView foto;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                descricaoJogo = (TextView) v.findViewById(R.id.tvDescricaoJogoOport);
                nomeUsuario = (TextView) v.findViewById(R.id.tvNomeUsuarioOport);
                foto = (ImageView) v.findViewById(R.id.ivFotoJogoOport);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_oportunidades, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.nomeUsuario.setText(lista.get(position).nome);
            holder.descricaoJogo.setText(lista.get(position).descricao);
            if(lista.get(position).id_interesse==4) {
                row.setBackgroundColor(getResources().getColor(R.color.azul));
            }else{
                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            holder.foto.setImageBitmap(lista.get(position).foto);

            return row;
        }
    }

}
