package com.getyourgame;

import android.content.Context;
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
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourgame.model.Jogo;
import com.getyourgame.model.Plataforma;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;


public class TelaJogo extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    int id_jogo;
    Bitmap sem_jogo;
    Bitmap sem_usuario;

    ArrayList<Plataforma> listaPlataformas = new ArrayList<Plataforma>();
    ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();

    LadapterPlataformas adapterPlataformas;
    LadapterUsuarios adapterUsuarios;

    ImageView ivJFotoJogo;
    TextView tvJDescricaoJogo;
    TextView tvJAnoJogo;

    ListView lvLTListaPlataformas;
    ListView lvJUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_jogo);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        /*
        Bundle recebe = getIntent().getExtras();
        id_jogo = recebe.getInt("id_jogo");
        */
        id_jogo = 1;

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        sem_usuario = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_user);

        ivJFotoJogo = (ImageView)findViewById(R.id.ivJFotoJogo);
        tvJDescricaoJogo = (TextView)findViewById(R.id.tvJDescricaoJogo);
        tvJAnoJogo = (TextView)findViewById(R.id.tvJAnoJogo);

        lvLTListaPlataformas = (ListView)findViewById(R.id.lvLTListaPlataformas);
        lvJUsuarios = (ListView)findViewById(R.id.lvJUsuarios);

        new HttpCarregaJogo((new Webservice().buscaJogo(id_jogo)),null,Jogo.class,"").execute();
        new HttpBuscaUsuarioTemJogo((new Webservice().buscaUsuarioTemJogo(id_jogo)),null,Object[].class,"").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jogo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpCarregaJogo extends Http {
        public HttpCarregaJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno!=null) {
                Jogo jogo = (Jogo) retorno;

                ivJFotoJogo.setImageBitmap(jogo.getFoto().equals("")?sem_jogo : util.StringToBitMap(jogo.getFoto()));
                tvJDescricaoJogo.setText(jogo.getDescricao());
                tvJAnoJogo.setText(jogo.getAno().toString().equals("") ? "" : "("+jogo.getAno().toString()+")");


                ObjectMapper plataformaMapper = new ObjectMapper();

                List<Plataforma> plataformas = plataformaMapper.convertValue(jogo.getPlataformas(), new TypeReference<List<Plataforma>>() { });
                listaPlataformas.addAll(plataformas);

                adapterPlataformas = new LadapterPlataformas(getApplicationContext());
                lvLTListaPlataformas.setAdapter(adapterPlataformas);
            }
        }
    }

    class LadapterPlataformas extends BaseAdapter {

        Context c;
        myViewHolder holder;

        public LadapterPlataformas(Context context) {
            // TODO Auto-generated constructor stub
            this.c = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaPlataformas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaPlataformas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class myViewHolder {
            TextView descricao;
            TextView marca;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                descricao = (TextView) v.findViewById(R.id.tvJPlataformasDescricao);
                marca = (TextView) v.findViewById(R.id.tvJPlataformasMarca);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_jogo_plataformas, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.descricao.setText(listaPlataformas.get(position).getDescricao());
            holder.marca.setText(listaPlataformas.get(position).getMarca());

            return row;
        }
    }


    private class HttpBuscaUsuarioTemJogo extends Http {
        public HttpBuscaUsuarioTemJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            if(retorno!=null) {
                ObjectMapper usuarioMapper = new ObjectMapper();

                List<Usuario> usuarios = usuarioMapper.convertValue(retorno, new TypeReference<List<Usuario>>() { });
                listaUsuarios.addAll(usuarios);

                adapterUsuarios = new LadapterUsuarios(getApplicationContext());
                lvJUsuarios.setAdapter(adapterUsuarios);
            }
        }
    }

    class LadapterUsuarios extends BaseAdapter {

        Context c;
        myViewHolder holder;

        public LadapterUsuarios(Context context) {
            // TODO Auto-generated constructor stub
            this.c = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaUsuarios.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaUsuarios.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class myViewHolder {
            TextView nome;
            ImageView foto;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                nome = (TextView) v.findViewById(R.id.tvNomeUsuario);
                foto = (ImageView) v.findViewById(R.id.ivFotoUsuario);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_usuario, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.nome.setText(listaUsuarios.get(position).getNome());
            holder.foto.setImageBitmap(listaUsuarios.get(position).getFoto().equals("")?sem_usuario : util.StringToBitMap(listaUsuarios.get(position).getFoto()));

            return row;
        }
    }
}
