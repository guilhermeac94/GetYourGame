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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class ListaTransacaoTab extends AppCompatActivity {

    Integer id_usuario;
    String chave_api;
    int status;
    Util util = new Util();
    DecimalFormat df = new DecimalFormat("0.00");
    ListView lvLTListaTransacoes;
    TextView tvLTNenhum;
    Ladapter adapter;
    ArrayList<Item> lista;
    String filtro;
    Bitmap sem_jogo;
    Bitmap moeda;
    ProgressBar pbLTCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_transacao_tab);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        status = recebe.getInt("status");

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        moeda = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_moeda);

        lista = new ArrayList();
        lvLTListaTransacoes = (ListView) findViewById(R.id.lvLTListaTransacoes);
        tvLTNenhum = (TextView)findViewById(R.id.tvLTNenhum);
        pbLTCarregando = (ProgressBar)findViewById(R.id.pbLTCarregando);

        new HttpBuscaTransacoes((new Webservice().buscaTransacoes(id_usuario, status)), null, Object[].class, "").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_transacao_tab, menu);
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

    private class HttpBuscaTransacoes extends Http {
        public HttpBuscaTransacoes(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            if(retorno!=null){
                Object[] l = Util.convertToObjectArray(retorno);

                for(Object obj : l) {
                    Map<String, String> map = (Map<String, String>) obj;

                    int interesse = Integer.parseInt(String.valueOf(map.get("id_interesse")));
                    int id_transacao = Integer.parseInt(String.valueOf(map.get("id_transacao")));

                    lista.add(new Item(id_transacao,
                            interesse,
                            map.get("descricao_jogo"),
                            map.get("plataforma_jogo"),
                            interesse == 4 ? moeda : (map.get("foto_jogo").equals("") ? sem_jogo : util.StringToBitMap(map.get("foto_jogo"))),
                            Integer.parseInt(String.valueOf(map.get("id_usuario_jogo"))),
                            Integer.parseInt(String.valueOf(map.get("id_usuario_ofert"))),
                            map.get("nome_ofert"),
                            Integer.parseInt(String.valueOf(map.get("id_jogo_ofert"))),
                            map.get("descricao_jogo_ofert"),
                            map.get("plataforma_jogo_ofert"),
                            map.get("foto_jogo_ofert").equals("") ? sem_jogo : util.StringToBitMap(map.get("foto_jogo_ofert")),
                            map.get("preco_jogo_ofert") != null ? df.format(Double.parseDouble(String.valueOf(map.get("preco_jogo_ofert")))) : "",
                            Integer.parseInt(String.valueOf(map.get("id_usuario_jogo_ofert")))));
                }

                adapter = new Ladapter(getApplicationContext());
                lvLTListaTransacoes.setAdapter(adapter);

                pbLTCarregando.setVisibility(View.GONE);
                lvLTListaTransacoes.setVisibility(View.VISIBLE);

                lvLTListaTransacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Item item = lista.get(i);
                        Bundle param = new Bundle();
                        param.putInt("id_usuario", id_usuario);
                        param.putString("chave_api", chave_api);
                        param.putInt("id_transacao", item.id_transacao);
                        Intent intent = new Intent(ListaTransacaoTab.this, Transacao.class);
                        intent.putExtras(param);
                        startActivity(intent);
                    }
                });
            }else{
                pbLTCarregando.setVisibility(View.GONE);
                tvLTNenhum.setVisibility(View.VISIBLE);
            }
        }
    }

    class Item {
        int id_transacao;
        int id_interesse;
        String descricao_jogo;
        String plataforma_jogo;
        Bitmap foto_jogo;
        int id_usuario_jogo;
        int id_usuario_ofert;
        String nome_ofert;
        int id_jogo_ofert;
        String descricao_jogo_ofert;
        String plataforma_jogo_ofert;
        Bitmap foto_jogo_ofert;
        String preco_jogo_ofert;
        int id_usuario_jogo_ofert;

        public Item(int id_transacao, int id_interesse, String descricao_jogo, String plataforma_jogo, Bitmap foto_jogo, int id_usuario_jogo, int id_usuario_ofert, String nome_ofert, int id_jogo_ofert, String descricao_jogo_ofert, String plataforma_jogo_ofert, Bitmap foto_jogo_ofert, String preco_jogo_ofert, int id_usuario_jogo_ofert) {
            this.id_transacao = id_transacao;
            this.id_interesse = id_interesse;
            this.descricao_jogo = descricao_jogo;
            this.plataforma_jogo = plataforma_jogo;
            this.foto_jogo = foto_jogo;
            this.id_usuario_jogo = id_usuario_jogo;
            this.id_usuario_ofert = id_usuario_ofert;
            this.nome_ofert = nome_ofert;
            this.id_jogo_ofert = id_jogo_ofert;
            this.descricao_jogo_ofert = descricao_jogo_ofert;
            this.plataforma_jogo_ofert = plataforma_jogo_ofert;
            this.foto_jogo_ofert = foto_jogo_ofert;
            this.preco_jogo_ofert = preco_jogo_ofert;
            this.id_usuario_jogo_ofert = id_usuario_jogo_ofert;
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
            TextView tvLTDescricaoJogo;
            TextView tvLTPlataformaJogo;
            ImageView ivLTFotoJogo;
            TextView tvLTPrecoOfert;
            TextView tvLTDescricaoJogoOfert;
            TextView tvLTPlataformaJogoOfert;
            ImageView ivLTFotoJogoOfert;
            TextView tvLTNomeUsuarioOfert;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                tvLTDescricaoJogo = (TextView) v.findViewById(R.id.tvLTDescricaoJogo);
                tvLTPlataformaJogo = (TextView) v.findViewById(R.id.tvLTPlataformaJogo);
                ivLTFotoJogo = (ImageView) v.findViewById(R.id.ivLTFotoJogo);
                tvLTPrecoOfert = (TextView) v.findViewById(R.id.tvLTPrecoOfert);
                tvLTDescricaoJogoOfert = (TextView) v.findViewById(R.id.tvLTDescricaoJogoOfert);
                tvLTPlataformaJogoOfert = (TextView) v.findViewById(R.id.tvLTPlataformaJogoOfert);
                ivLTFotoJogoOfert = (ImageView) v.findViewById(R.id.ivLTFotoJogoOfert);
                tvLTNomeUsuarioOfert = (TextView) v.findViewById(R.id.tvLTNomeUsuarioOfert);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_transacao_tab, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }


            if(lista.get(position).id_interesse == 4){
                //row.setBackgroundColor(getResources().getColor(R.color.compra));
                holder.tvLTPrecoOfert.setText(lista.get(position).preco_jogo_ofert);
            }else{
                //row.setBackgroundColor(getResources().getColor(R.color.troca));
                holder.tvLTDescricaoJogo.setText(lista.get(position).descricao_jogo);
                holder.tvLTPlataformaJogo.setText(lista.get(position).plataforma_jogo);
                holder.tvLTPrecoOfert.setText("");
                holder.tvLTPrecoOfert.setVisibility(View.GONE);
            }

            holder.ivLTFotoJogo.setImageBitmap(lista.get(position).foto_jogo);
            holder.tvLTDescricaoJogoOfert.setText(lista.get(position).descricao_jogo_ofert);
            holder.tvLTPlataformaJogoOfert.setText(lista.get(position).plataforma_jogo_ofert);
            holder.ivLTFotoJogoOfert.setImageBitmap(lista.get(position).foto_jogo_ofert);
            holder.tvLTNomeUsuarioOfert.setText(lista.get(position).nome_ofert);

            return row;
        }
    }
}
