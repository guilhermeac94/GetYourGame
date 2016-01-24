package com.getyourgame;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Map;

public class Avaliacao extends AppCompatActivity {

    Util util = new Util();

    Integer id_usuario;
    String chave_api;

    Integer id_usuario_aval;

    ListView lvAAvaliacoes;
    ProgressBar pbACarregando;
    TextView tvANenhumResultado;

    Ladapter adapter;
    ArrayList<Item> lista = new ArrayList();
    Bitmap sem_usuario;
    Bitmap positivo;
    Bitmap negativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_usuario_aval = recebe.getInt("id_usuario_aval");

        sem_usuario = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_user);
        positivo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.thumbs_up);
        negativo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.thumbs_down);

        lvAAvaliacoes = (ListView)findViewById(R.id.lvAAvaliacoes);
        pbACarregando = (ProgressBar)findViewById(R.id.pbACarregando);
        tvANenhumResultado = (TextView)findViewById(R.id.tvANenhumResultado);

        new HttpBuscaAvaliacoes((new Webservice().buscaAvaliacoesUsuario(id_usuario_aval)), null, Object[].class, "").execute();
    }


    private class HttpBuscaAvaliacoes extends Http {
        public HttpBuscaAvaliacoes(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            if(retorno!=null) {
                Object[] l = Util.convertToObjectArray(retorno);

                for (Object obj : l) {
                    Map<String, String> map = (Map<String, String>) obj;

                    lista.add(new Item(Integer.parseInt(String.valueOf(map.get("id_avaliacao_transacao"))),
                            Integer.parseInt(String.valueOf(map.get("id_transacao"))),
                            Integer.parseInt(String.valueOf(map.get("id_usuario_avaliador"))),
                            Integer.parseInt(String.valueOf(map.get("id_usuario_avaliado"))),
                            Integer.parseInt(String.valueOf(map.get("avaliacao"))),
                            map.get("observacao"),
                            map.get("nome"),
                            map.get("foto").equals("") ? sem_usuario : util.StringToBitMap(map.get("foto"))));
                }

                adapter = new Ladapter(getApplicationContext());
                lvAAvaliacoes.setAdapter(adapter);

                pbACarregando.setVisibility(View.GONE);
                lvAAvaliacoes.setVisibility(View.VISIBLE);

                lvAAvaliacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Item item = lista.get(i);

                        Bundle param = new Bundle();
                        param.putInt("id_usuario", id_usuario);
                        param.putString("chave_api", chave_api);
                        param.putInt("id_avaliacao_transacao", item.id_avaliacao_transacao);
                        param.putInt("id_transacao", item.id_transacao);
                        param.putInt("id_usuario_avaliador", item.id_usuario_avaliador);
                        param.putInt("id_usuario_avaliado", item.id_usuario_avaliado);

                        Intent intent = new Intent(Avaliacao.this, Avaliar.class);
                        intent.putExtras(param);
                        startActivity(intent);
                    }
                });
            }else{
                pbACarregando.setVisibility(View.GONE);
                tvANenhumResultado.setVisibility(View.VISIBLE);
            }
        }
    }

    class Item {
        Integer id_avaliacao_transacao;
        Integer id_transacao;
        Integer id_usuario_avaliador;
        Integer id_usuario_avaliado;
        Integer avaliacao;
        String observacao;
        String nome;
        Bitmap foto;

        public Item(Integer id_avaliacao_transacao, Integer id_transacao, Integer id_usuario_avaliador, Integer id_usuario_avaliado, Integer avaliacao, String observacao, String nome, Bitmap foto) {
            this.id_avaliacao_transacao = id_avaliacao_transacao;
            this.id_transacao = id_transacao;
            this.id_usuario_avaliador = id_usuario_avaliador;
            this.id_usuario_avaliado = id_usuario_avaliado;
            this.avaliacao = avaliacao;
            this.observacao = observacao;
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
            ImageView ivAFotoUsuario;
            TextView tvANomeUsuario;
            ImageView ivAAvaliacao;
            TextView tvAObservacao;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                ivAFotoUsuario = (ImageView)v.findViewById(R.id.ivAFotoUsuario);
                tvANomeUsuario = (TextView)v.findViewById(R.id.tvANomeUsuario);
                ivAAvaliacao = (ImageView)v.findViewById(R.id.ivAAvaliacao);
                tvAObservacao = (TextView)v.findViewById(R.id.tvAObservacao);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_avaliacao, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.ivAFotoUsuario.setImageBitmap(lista.get(position).foto);
            holder.tvANomeUsuario.setText(lista.get(position).nome);

            String obs = lista.get(position).observacao.length() >= 25 ? lista.get(position).observacao.substring(0,24)+"..." : lista.get(position).observacao;

            holder.tvAObservacao.setText("\""+obs+"\"");

            if(lista.get(position).avaliacao==1){
                holder.ivAAvaliacao.setImageBitmap(positivo);
            }else{
                holder.ivAAvaliacao.setImageBitmap(negativo);
            }
            return row;
        }
    }
}
