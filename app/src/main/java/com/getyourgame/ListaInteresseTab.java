package com.getyourgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.getyourgame.R;
import com.getyourgame.Transacao;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class ListaInteresseTab extends AppCompatActivity {
    Integer id_usuario;
    String chave_api;
    int id_interesse;
    Util util = new Util();
    DecimalFormat df = new DecimalFormat("0.00");
    ListView lvListaInteresseTab;
    TextView tvListaInteresseTabNenhum;
    Ladapter adapter;
    ArrayList<Item> lista;
    String filtro;
    Bitmap sem_jogo;
    Bitmap moeda;
    ProgressBar pbListaInteresseTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_interesse_tab);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_interesse = recebe.getInt("id_interesse");

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        moeda = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_moeda);

        lvListaInteresseTab = (ListView) findViewById(R.id.lvListaInteresseTab);
        tvListaInteresseTabNenhum = (TextView) findViewById(R.id.tvListaInteresseTabNenhum);
        pbListaInteresseTab = (ProgressBar) findViewById(R.id.pbListaInteresseTab);

        buscaInteresses();
    }

    public void buscaInteresses(){
        lista = new ArrayList();
        lvListaInteresseTab.setVisibility(View.GONE);
        pbListaInteresseTab.setVisibility(View.VISIBLE);
        new HttpBuscaInteresses((new Webservice().buscaInteresses(id_usuario, id_interesse)), null, Object[].class, "").execute();
    }

    private class HttpBuscaInteresses extends Http {
        public HttpBuscaInteresses(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            if (retorno != null) {
                Object[] l = Util.convertToObjectArray(retorno);

                for (Object obj : l) {
                    Map<String, String> map = (Map<String, String>) obj;

                    int interesse = Integer.parseInt(String.valueOf(map.get("id_interesse")));

                    lista.add(new Item(Integer.parseInt(String.valueOf(map.get("id_usuario_jogo"))),
                            interesse,
                            Integer.parseInt(String.valueOf(map.get("id_usuario"))),
                            map.get("nome"),
                            map.get("descricao_jogo"),
                            map.get("plataforma_jogo"),
                            map.get("foto_jogo").equals("") ? sem_jogo : util.StringToBitMap(map.get("foto_jogo")),
                            map.get("descricao_jogo_troca"),
                            map.get("plataforma_jogo_troca"),
                            map.get("foto_jogo_troca").equals("") ? sem_jogo : util.StringToBitMap(map.get("foto_jogo_troca")),
                            map.get("preco") != null ? df.format(Double.parseDouble(String.valueOf(map.get("preco")))) : "",
                            map.get("preco_inicial") != null ? df.format(Double.parseDouble(String.valueOf(map.get("preco_inicial")))) : "",
                            map.get("preco_final") != null ? df.format(Double.parseDouble(String.valueOf(map.get("preco_final")))) : ""));
                }

                adapter = new Ladapter(getApplicationContext());
                lvListaInteresseTab.setAdapter(adapter);

                pbListaInteresseTab.setVisibility(View.GONE);
                lvListaInteresseTab.setVisibility(View.VISIBLE);

                lvListaInteresseTab.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        Item item = lista.get(pos);
                        AlertDialog diaBox = AskOption(item.id_usuario_jogo);
                        diaBox.show();
                        return true;
                    }
                });
            } else {
                pbListaInteresseTab.setVisibility(View.GONE);
                tvListaInteresseTabNenhum.setVisibility(View.VISIBLE);
            }
        }
    }

    private class HttpDeletaInteresses extends Http {
        public HttpDeletaInteresses(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Map<String, String> map = (Map<String, String>) retorno;

            Object map_error = map.get("error");

            if(!Boolean.parseBoolean(map_error.toString())){
                util.toast(getApplicationContext(), map.get("message"));
                buscaInteresses();
            }else{
                util.msgDialog(ListaInteresseTab.this, "Alerta", map.get("message"));
            }
        }
    }

    public AlertDialog AskOption(final int id_usuario_jogo) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Remover")
                .setMessage("Deseja remover o interesse?")
                .setIcon(R.drawable.ic_remove)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new HttpDeletaInteresses(new Webservice().deletaInteresse(id_usuario_jogo), null, Object.class, "").execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
   }

    class Item {
        int id_usuario_jogo;
        int id_interesse;
        int id_usuario;
        String nome;
        String descricaoJogo;
        String plataformaJogo;
        Bitmap fotoJogo;
        String descricaoJogoTroca;
        String plataformaJogoTroca;
        Bitmap fotoJogoTroca;
        String preco;
        String precoInicial;
        String precoFinal;

        public Item(int id_usuario_jogo, int id_interesse, int id_usuario, String nome, String descricaoJogo, String plataformaJogo, Bitmap fotoJogo, String descricaoJogoTroca, String plataformaJogoTroca, Bitmap fotoJogoTroca, String preco, String precoInicial, String precoFinal) {
            this.id_usuario_jogo = id_usuario_jogo;
            this.id_interesse = id_interesse;
            this.id_usuario = id_usuario;
            this.nome = nome;
            this.descricaoJogo = descricaoJogo;
            this.plataformaJogo = plataformaJogo;
            this.fotoJogo = fotoJogo;
            this.descricaoJogoTroca = descricaoJogoTroca;
            this.plataformaJogoTroca = plataformaJogoTroca;
            this.fotoJogoTroca = fotoJogoTroca;
            this.preco = preco;
            this.precoInicial = precoInicial;
            this.precoFinal = precoFinal;
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
            TextView tvInteresseTabDescricaoJogo1;
            TextView tvInteresseTabPlataformaJogo1;
            ImageView ivInteresseTabFotoJogo1;
            TextView tvInteresseTabPreco1;
            TextView tvInteresseTabDescricaoJogo2;
            TextView tvInteresseTabPlataformaJogo2;
            ImageView ivInteresseTabFotoJogo2;
            TextView tvInteresseTabPreco2;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                tvInteresseTabDescricaoJogo1 = (TextView) v.findViewById(R.id.tvInteresseTabDescricaoJogo1);
                tvInteresseTabPlataformaJogo1 = (TextView) v.findViewById(R.id.tvInteresseTabPlataformaJogo1);
                ivInteresseTabFotoJogo1 = (ImageView) v.findViewById(R.id.ivInteresseTabFotoJogo1);
                tvInteresseTabPreco1 = (TextView) v.findViewById(R.id.tvInteresseTabPreco1);
                tvInteresseTabDescricaoJogo2 = (TextView) v.findViewById(R.id.tvInteresseTabDescricaoJogo2);
                tvInteresseTabPlataformaJogo2 = (TextView) v.findViewById(R.id.tvInteresseTabPlataformaJogo2);
                ivInteresseTabFotoJogo2 = (ImageView) v.findViewById(R.id.ivInteresseTabFotoJogo2);
                tvInteresseTabPreco2 = (TextView) v.findViewById(R.id.tvInteresseTabPreco2);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.layout_interesse_tab, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }


            if(lista.get(position).id_interesse == 1){
                holder.tvInteresseTabDescricaoJogo1.setText(lista.get(position).descricaoJogo);
                holder.tvInteresseTabPlataformaJogo1.setText(lista.get(position).plataformaJogo);
                holder.ivInteresseTabFotoJogo1.setImageBitmap(lista.get(position).fotoJogo);

                if(lista.get(position).descricaoJogoTroca!=null){
                    holder.tvInteresseTabDescricaoJogo2.setText(lista.get(position).descricaoJogoTroca);
                    holder.tvInteresseTabPlataformaJogo2.setText(lista.get(position).plataformaJogo);
                    holder.ivInteresseTabFotoJogo2.setImageBitmap(lista.get(position).fotoJogo);
                }else{
                    holder.tvInteresseTabDescricaoJogo2.setText("(Qualquer)");
                    holder.tvInteresseTabPlataformaJogo2.setText("");
                    holder.ivInteresseTabFotoJogo2.setImageBitmap(sem_jogo);
                }
            }else if(lista.get(position).id_interesse == 2) {
                holder.tvInteresseTabDescricaoJogo1.setText(lista.get(position).descricaoJogo);
                holder.tvInteresseTabPlataformaJogo1.setText(lista.get(position).plataformaJogo);
                holder.ivInteresseTabFotoJogo1.setImageBitmap(lista.get(position).fotoJogo);

                holder.tvInteresseTabPreco2.setText(lista.get(position).preco);
                holder.ivInteresseTabFotoJogo2.setImageBitmap(moeda);

            }else if(lista.get(position).id_interesse == 3) {
                holder.tvInteresseTabDescricaoJogo2.setText(lista.get(position).descricaoJogo);
                holder.tvInteresseTabPlataformaJogo2.setText(lista.get(position).plataformaJogo);
                holder.ivInteresseTabFotoJogo2.setImageBitmap(lista.get(position).fotoJogo);

                if(lista.get(position).descricaoJogoTroca!=null){
                    holder.tvInteresseTabDescricaoJogo1.setText(lista.get(position).descricaoJogoTroca);
                    holder.tvInteresseTabPlataformaJogo1.setText(lista.get(position).plataformaJogo);
                    holder.ivInteresseTabFotoJogo1.setImageBitmap(lista.get(position).fotoJogo);
                }else{
                    holder.tvInteresseTabDescricaoJogo1.setText("(Qualquer)");
                    holder.tvInteresseTabPlataformaJogo1.setText("");
                    holder.ivInteresseTabFotoJogo1.setImageBitmap(sem_jogo);
                }
            }else if(lista.get(position).id_interesse == 4) {
                holder.tvInteresseTabDescricaoJogo2.setText(lista.get(position).descricaoJogo);
                holder.tvInteresseTabPlataformaJogo2.setText(lista.get(position).plataformaJogo);
                holder.ivInteresseTabFotoJogo2.setImageBitmap(lista.get(position).fotoJogo);

                holder.tvInteresseTabPreco1.setText(lista.get(position).precoInicial + " até " + lista.get(position).precoFinal);
                holder.ivInteresseTabFotoJogo1.setImageBitmap(moeda);

            }

            return row;
        }
    }
}

