package com.getyourgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaUsuarios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaUsuarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaUsuarios extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Util util = new Util();
    Context context;

    Integer id_usuario;

    ListView lvUsuarios;
    EditText etBuscarUsuario;

    Ladapter adapter;
    ArrayList<Usuario> lista;
    String filtro;
    Bitmap sem_usuario;

    private View fragmentView;

    private OnSelecionaUsuarioListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaUsuarios.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaUsuarios newInstance(String param1, String param2) {
        ListaUsuarios fragment = new ListaUsuarios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListaUsuarios() {
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

        fragmentView = inflater.inflate(R.layout.fragment_lista_usuarios, container, false);

        lvUsuarios  = (ListView) fragmentView.findViewById(R.id.lvUsuarios);

        Button btBuscarUsuario = (Button) fragmentView.findViewById(R.id.btBuscarUsuario);
        sem_usuario = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user);

        etBuscarUsuario = (EditText) fragmentView.findViewById(R.id.etBuscarUsuario);

        btBuscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro = String.valueOf(etBuscarUsuario.getText().toString());
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

    public void seleciona(Usuario usuario) {
        if (mListener != null) {
            mListener.OnSelecionaUsuario(usuario);
        }
    }

    public void carregaLista(String filtro){
        lista = new ArrayList();

        Webservice ws = new Webservice();

        if(filtro.equals("")) {
            new HttpBuscaUsuarios(ws.buscaUsuarios(), null, Object[].class, "").execute();

        }else {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("filtro", filtro);
            new HttpBuscaUsuarios(ws.buscaUsuarios(), map, Object[].class, "").execute();
        }
    }


    private class HttpBuscaUsuarios extends Http {
        public HttpBuscaUsuarios(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            super.onPostExecute(retorno);
            if(retorno instanceof Exception){
                util.msgDialog(getActivity(), "Alerta", "Erro ao conectar com o servidor.");
            }else {
                ObjectMapper jogoMapper = new ObjectMapper();

                List<Usuario> usuarios = jogoMapper.convertValue(retorno, new TypeReference<List<Usuario>>() {
                });
                lista.addAll(usuarios);

                adapter = new Ladapter(context);
                lvUsuarios.setAdapter(adapter);

                lvUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Item item = lista.get(i);
                        Usuario usuario = lista.get(i);

                        seleciona(usuario);
                    }
                });
            }
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
        if (context instanceof OnSelecionaUsuarioListener) {
            mListener = (OnSelecionaUsuarioListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implemenet ListaUsuarios.OnSelecionaUsuarioListener");
        }
    }

    public interface OnSelecionaUsuarioListener {
        // TODO: Update argument type and name
        public void OnSelecionaUsuario(Usuario usuario);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        class myViewHolder {
            TextView name;
            ImageView foto;

            public myViewHolder(View v) {
                // TODO Auto-generated constructor stub
                name = (TextView) v.findViewById(R.id.tvNomeUsuario);
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

            holder.name.setText(lista.get(position).getNome());
            holder.foto.setImageBitmap(lista.get(position).getFoto().equals("")?sem_usuario : util.StringToBitMap(lista.get(position).getFoto()));

            if (holder.name.getText().toString().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setVisibility(View.VISIBLE);
            }

            return row;
        }

    }

}
