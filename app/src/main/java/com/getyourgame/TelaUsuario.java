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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourgame.model.Jogo;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class TelaUsuario extends AppCompatActivity {

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    Bitmap sem_jogo;
    Bitmap sem_usuario;
    Integer id_usuario_selec;

    ArrayList<Jogo> listaJogos = new ArrayList<Jogo>();

    LadapterJogos adapterJogos;

    ImageView ivUFotoUsuario;
    TextView tvUNomeUsuario;
    TextView tvUEmailUsuario;

    ProgressBar pbUCarregando;
    TextView tvUNenhumResultado;
    ListView lvUJogos;

    Button btUAvaliacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_usuario);

        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        id_usuario_selec = recebe.getInt("id_usuario_selec");

        sem_jogo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jogo_default);
        sem_usuario = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_user);

        ivUFotoUsuario = (ImageView) findViewById(R.id.ivUFotoUsuario);
        tvUNomeUsuario = (TextView) findViewById(R.id.tvUNomeUsuario);
        tvUEmailUsuario = (TextView) findViewById(R.id.tvUEmailUsuario);

        pbUCarregando = (ProgressBar) findViewById(R.id.pbUCarregando);
        tvUNenhumResultado = (TextView) findViewById(R.id.tvUNenhumResultado);
        lvUJogos = (ListView) findViewById(R.id.lvUJogos);

        btUAvaliacoes = (Button)findViewById(R.id.btUAvaliacoes);
        btUAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAvaliacao = new Intent(TelaUsuario.this, Avaliacao.class);
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                param.putInt("id_usuario_aval", id_usuario_selec);
                intentAvaliacao.putExtras(param);
                startActivity(intentAvaliacao);
            }
        });

        new HttpCarregaUsuario(new Webservice().buscaUsuario(id_usuario_selec),null,Usuario.class,chave_api).execute();
        new HttpBuscaJogosDoUsuario(new Webservice().buscaJogosDoUsuario(id_usuario_selec),null,Object[].class,"").execute();
    }

    private class HttpCarregaUsuario extends Http {
        public HttpCarregaUsuario(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(TelaUsuario.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    tvUNomeUsuario.setText(usuario.getNome());
                    tvUEmailUsuario.setText("(" + usuario.getEmail() + ")");
                    ivUFotoUsuario.setImageBitmap(usuario.getFoto().equals("") ? sem_usuario : util.StringToBitMap(usuario.getFoto()));
                } else {
                    util.msgDialog(TelaUsuario.this, "Alerta", usuario.getMessage());
                }
            }
        }
    }

    private class HttpBuscaJogosDoUsuario extends Http {
        public HttpBuscaJogosDoUsuario(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);
            if(retorno instanceof Exception){
                util.msgDialog(TelaUsuario.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                if (retorno != null) {
                    ObjectMapper usuarioMapper = new ObjectMapper();

                    List<Jogo> jogos = usuarioMapper.convertValue(retorno, new TypeReference<List<Jogo>>() {
                    });
                    listaJogos.addAll(jogos);

                    adapterJogos = new LadapterJogos(getApplicationContext());
                    lvUJogos.setAdapter(adapterJogos);

                    pbUCarregando.setVisibility(View.GONE);
                    lvUJogos.setVisibility(View.VISIBLE);

                    lvUJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Jogo jogo = listaJogos.get(i);

                            Bundle param = new Bundle();
                            param.putInt("id_usuario", id_usuario);
                            param.putString("chave_api", chave_api);
                            param.putInt("id_jogo", jogo.getId_jogo());
                            Intent intent = new Intent(TelaUsuario.this, TelaJogo.class);
                            intent.putExtras(param);
                            startActivity(intent);
                        }
                    });
                } else {
                    pbUCarregando.setVisibility(View.GONE);
                    tvUNenhumResultado.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class LadapterJogos extends BaseAdapter {

        Context c;
        myViewHolder holder;

        public LadapterJogos(Context context) {
            // TODO Auto-generated constructor stub
            this.c = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaJogos.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaJogos.get(position);
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
                row = inflater.inflate(R.layout.layout_jogos, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.name.setText(listaJogos.get(position).getDescricao());
            holder.foto.setImageBitmap(listaJogos.get(position).getFoto().equals("")?sem_jogo : util.StringToBitMap(listaJogos.get(position).getFoto()));

            if (holder.name.getText().toString().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setVisibility(View.VISIBLE);
            }

            return row;
        }
    }
}
