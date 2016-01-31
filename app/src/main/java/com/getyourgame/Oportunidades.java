package com.getyourgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getyourgame.util.Http;
import com.getyourgame.util.SwipeDetector;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class Oportunidades extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    DecimalFormat df = new DecimalFormat("0.00");
    ListView lvOportunidades;
    Ladapter adapter;
    ArrayList<Item> lista;
    String filtro;
    Bitmap sem_jogo;
    Bitmap moeda;
    ProgressBar pbOportunidadesCarregando;
    TextView tvOportunidadesNenhum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidades);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        //id_usuario = 5;
        //chave_api = "923798d42ec81ca9f07e3cffd7855748";

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        moeda = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_moeda);

        lista = new ArrayList();
        lvOportunidades = (ListView) findViewById(R.id.lvOportunidades);

        pbOportunidadesCarregando = (ProgressBar)findViewById(R.id.pbOportunidades);
        tvOportunidadesNenhum = (TextView)findViewById(R.id.tvOportunidadesNenhum);

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
            if(retorno instanceof Exception){
                util.msgDialog(Oportunidades.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                if (retorno != null) {
                    Object[] l = Util.convertToObjectArray(retorno);

                    for (Object obj : l) {
                        Map<String, String> map = (Map<String, String>) obj;

                        int interesse = Integer.parseInt(String.valueOf(map.get("id_interesse")));

                        lista.add(new Item(Integer.parseInt(String.valueOf(map.get("existe_transacao"))),
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
                    lvOportunidades.setAdapter(adapter);

                    pbOportunidadesCarregando.setVisibility(View.GONE);
                    lvOportunidades.setVisibility(View.VISIBLE);

                    lvOportunidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Item item = lista.get(i);

                            if (item.existe_transacao == 1) {
                                util.msgDialog(Oportunidades.this, "Alerta", "Já existe uma transação pendente para esta oportunidade!");
                            } else {
                                Bundle param = new Bundle();
                                param.putInt("id_usuario", id_usuario);
                                param.putString("chave_api", chave_api);
                                param.putInt("id_usuario_jogo_solic", item.id_usuario_jogo);
                                param.putInt("id_usuario_jogo_ofert", item.id_usuario_jogo_ofert);
                                Intent intent = new Intent(Oportunidades.this, IniciarTransacao.class);
                                intent.putExtras(param);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    pbOportunidadesCarregando.setVisibility(View.GONE);
                    tvOportunidadesNenhum.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class Item {
        int existe_transacao;
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

        public Item(int existe_transacao, int id_interesse, String descricao_jogo, String plataforma_jogo, Bitmap foto_jogo, int id_usuario_jogo, int id_usuario_ofert, String nome_ofert, int id_jogo_ofert, String descricao_jogo_ofert, String plataforma_jogo_ofert, Bitmap foto_jogo_ofert, String preco_jogo_ofert, int id_usuario_jogo_ofert) {
            this.existe_transacao = existe_transacao;
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
            TextView tvDescricaoJogo;
            TextView tvPlataformaJogo;
            ImageView ivFotoJogo;
            TextView tvPrecoOfert;
            TextView tvDescricaoJogoOfert;
            TextView tvPlataformaJogoOfert;
            ImageView ivFotoJogoOfert;
            TextView tvNomeUsuarioOfert;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                tvDescricaoJogo = (TextView) v.findViewById(R.id.tvDescricaoJogo);
                tvPlataformaJogo = (TextView) v.findViewById(R.id.tvPlataformaJogo);
                ivFotoJogo = (ImageView) v.findViewById(R.id.ivFotoJogo);
                tvPrecoOfert = (TextView) v.findViewById(R.id.tvPrecoOfert);
                tvDescricaoJogoOfert = (TextView) v.findViewById(R.id.tvDescricaoJogoOfert);
                tvPlataformaJogoOfert = (TextView) v.findViewById(R.id.tvPlataformaJogoOfert);
                ivFotoJogoOfert = (ImageView) v.findViewById(R.id.ivFotoJogoOfert);
                tvNomeUsuarioOfert = (TextView) v.findViewById(R.id.tvNomeUsuarioOfert);
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


            if(lista.get(position).id_interesse == 4){
                row.setBackgroundColor(getResources().getColor(R.color.compra));
                holder.tvPrecoOfert.setText(lista.get(position).preco_jogo_ofert);
                holder.ivFotoJogo.setImageBitmap(lista.get(position).foto_jogo);
            }else{
                row.setBackgroundColor(getResources().getColor(R.color.troca));
                holder.tvPrecoOfert.setText("");
                holder.tvPrecoOfert.setVisibility(View.GONE);

                if(lista.get(position).descricao_jogo!=null){
                    holder.tvDescricaoJogo.setText(lista.get(position).descricao_jogo);
                    holder.tvPlataformaJogo.setText(lista.get(position).plataforma_jogo);
                    holder.ivFotoJogo.setImageBitmap(lista.get(position).foto_jogo);
                }else{
                    holder.tvDescricaoJogo.setText("(Qualquer)");
                    holder.tvPlataformaJogo.setText("");
                    holder.ivFotoJogo.setImageBitmap(sem_jogo);
                }
            }

            holder.tvDescricaoJogoOfert.setText(lista.get(position).descricao_jogo_ofert);
            holder.tvPlataformaJogoOfert.setText(lista.get(position).plataforma_jogo_ofert);
            holder.ivFotoJogoOfert.setImageBitmap(lista.get(position).foto_jogo_ofert);
            holder.tvNomeUsuarioOfert.setText(lista.get(position).nome_ofert);

            return row;
        }
    }

}
