package com.getyourgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourgame.model.Jogo;
import com.getyourgame.model.Plataforma;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSelecionaJogoListener} interface
 * to handle interaction events.
 * Use the {@link ListaJogos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaJogos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Util util = new Util();
    Integer id_usuario;
    ListView lvJogos;
    Ladapter adapter;

    //ArrayList<Item> lista;
    ArrayList<Jogo> lista;

    String filtro;
    Bitmap sem_jogo;
    Context context;
    EditText etBuscarJogo;

    private View fragmentView;

    private OnSelecionaJogoListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaJogos.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaJogos newInstance(String param1, String param2) {
        ListaJogos fragment = new ListaJogos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListaJogos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentView = inflater.inflate(R.layout.fragment_lista_jogos, container, false);


        sem_jogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_jogo_default);

        lvJogos  = (ListView) fragmentView.findViewById(R.id.lvJogos);
        etBuscarJogo = (EditText) fragmentView.findViewById(R.id.etBuscarJogo);

        Button btBuscarJogo = (Button) fragmentView.findViewById(R.id.btBuscarJogo);
        btBuscarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro = String.valueOf(etBuscarJogo.getText().toString());
                carregaLista(filtro);
            }
        });
        return fragmentView;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        carregaLista("");
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void seleciona(Jogo jogo) {
        if (mListener != null) {
            mListener.OnSelecionaJogo(jogo);
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        this.context = context;
        if (context instanceof OnSelecionaJogoListener) {
            mListener = (OnSelecionaJogoListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implemenet ListaJogos.OnSelecionaJogoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSelecionaJogoListener {
        // TODO: Update argument type and name
        public void OnSelecionaJogo(Jogo jogo);
    }


    public void carregaLista(String filtro){
        lista = new ArrayList();

        Webservice ws = new Webservice();

        if(filtro.equals("")) {
           // new HttpBuscaJogos(ws.buscaJogos(), null, Object[].class, "").execute();
            new HttpBuscaJogos(ws.buscaJogos(), null, Object[].class, "").execute();

        }else {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("filtro", filtro);

            //new HttpBuscaJogos(ws.buscaJogos(), map, Object[].class, "").execute();
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

            ObjectMapper jogoMapper = new ObjectMapper();

            List<Jogo> jogos = jogoMapper.convertValue(retorno, new TypeReference<List<Jogo>>() { });
            lista.addAll(jogos);

            /*
            Convertendo um a um

            Object[] j = Util.convertToObjectArray(retorno);

            for(Object o : j){

                Jogo obj = jogoMapper.convertValue(o, Jogo.class);

                Jogo jogo = new Jogo();
                jogo.setId_jogo(obj.getId_jogo());
                jogo.setDescricao(obj.getDescricao());
                jogo.setFoto(obj.getFoto());
                jogo.setPlataformas(obj.getPlataformas());

                lista.add(jogo);
            }*/
            adapter = new Ladapter(context);
            lvJogos.setAdapter(adapter);

            lvJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Item item = lista.get(i);
                    Jogo jogo = lista.get(i);

                    seleciona(jogo);
                }
            });
        }
    }
/*
    private class HttpBuscaJogos extends Http {
        public HttpBuscaJogos(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);

            Object[] l = Util.convertToObjectArray(retorno);

            for(Object obj : l){
                Map<String, Object> map = (Map<String, Object>) obj;

                Jogo jogo = new Jogo();
                jogo.setId_jogo(Integer.parseInt(String.valueOf(map.get("id_jogo"))));
                jogo.setDescricao(map.get("nome").toString());
                jogo.setFoto(map.get("foto").toString());
                jogo.setPlataformas((ArrayList<Plataforma>)map.get("plataformas"));

                lista.add(jogo);
                //lista.add(new Item(Integer.parseInt(String.valueOf(map.get("id_jogo"))),  map.get("nome"), map.get("foto").equals("")?sem_jogo : util.StringToBitMap(map.get("foto"))));
            }
            adapter = new Ladapter(context);
            lvJogos.setAdapter(adapter);

            lvJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Item item = lista.get(i);
                    Jogo jogo = lista.get(i);

                    seleciona(jogo);
                }
            });
        }
    }*/
    /*
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
    */

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
                row = inflater.inflate(R.layout.layout_jogos, parent,
                        false);
                holder = new myViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (myViewHolder) row.getTag();
            }

            holder.name.setText(lista.get(position).getDescricao());
            holder.foto.setImageBitmap(lista.get(position).getFoto().equals("")?sem_jogo : util.StringToBitMap(lista.get(position).getFoto()));

            if (holder.name.getText().toString().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setVisibility(View.VISIBLE);
            }

            return row;
        }
    }

}
