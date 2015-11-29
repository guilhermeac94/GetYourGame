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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Map;

public class ListaJogo extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    ListView lvJogos;
    Ladapter adapter;
    ArrayList<Item> lista;
    String filtro;
    Bitmap sem_jogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jogo);

        Button btBuscarJogo = (Button) findViewById(R.id.btBuscarJogo);
        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);

        btBuscarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregaLista();
            }
        });
        carregaLista();
    }

    public void carregaLista(){

        EditText etBuscarJogo = (EditText) findViewById(R.id.etBuscarJogo);
        filtro = String.valueOf(etBuscarJogo.getText().toString());

        lista = new ArrayList();
        lvJogos  = (ListView) findViewById(R.id.lvJogos);

        Webservice ws = new Webservice();

        if(filtro.equals("")) {
            new HttpBuscaJogos(ws.buscaJogos(), null, Object[].class, "").execute();

        }else {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("filtro", filtro);
            new HttpBuscaJogos(ws.buscaJogos(), map, Object[].class, "").execute();
        }
    }


    private class HttpBuscaJogos extends Http {
        public HttpBuscaJogos(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Object[] l = Util.convertToObjectArray(retorno);

            for(Object obj : l){
                Map<String, String> map = (Map<String, String>) obj;

                lista.add(new Item(Integer.parseInt(String.valueOf(map.get("id_jogo"))),  map.get("descricao"), map.get("foto").equals("")?sem_jogo : util.StringToBitMap(map.get("foto"))));
            }
            adapter = new Ladapter(getApplicationContext());
            lvJogos.setAdapter(adapter);

            lvJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Item item = lista.get(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_jogo, menu);
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

    class Item {
        int id_jogo;
        String nome;
        Bitmap foto;

        Item(int id_jogo, String name, Bitmap foto) {
            this.id_jogo = id_jogo;
            this.nome = name;
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
            TextView name;
            ImageView foto;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                name = (TextView) v.findViewById(R.id.tvNomeJogo);
                foto = (ImageView) v.findViewById(R.id.ivFotoJogo);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_jogo, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.name.setText(lista.get(position).nome);
            holder.foto.setImageBitmap(lista.get(position).foto);

            if (holder.name.getText().toString().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setVisibility(View.VISIBLE);
            }

            return row;
        }
    }
}
