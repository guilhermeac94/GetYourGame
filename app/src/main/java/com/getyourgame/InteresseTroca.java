package com.getyourgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.EstadoJogo;
import com.getyourgame.model.Jogo;
import com.getyourgame.model.Plataforma;
import com.getyourgame.model.UsuarioJogo;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTrocaListener} interface
 * to handle interaction events.
 * Use the {@link InteresseTroca#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteresseTroca extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Util util = new Util();

    private OnTrocaListener mListener;
    private OnAbreSelecionaJogoListener mListenerJogo;

    int interesse;
    int tipo_jogo; /* 1-Jogo / 2-Jogo Desejado */

    int id_jogo = 0;
    int id_jogo_desejado = 0;

    Integer id_usuario;
    String chave_api;

    private View fragmentView;
    SQLiteHandler db;

    List<Plataforma> plataformas;
    Plataforma plataforma;
    Plataforma plataformaDesejada;

    List<EstadoJogo> estados_jogo;
    EstadoJogo estado_jogo;

    MultiValueMap<String, String> map;

    TextView tvSelecionaJogo;
    TextView tvSelecionaJogoDesejado;
    TextView tvPlataforma;
    TextView tvPlataformaDesejada;

    UsuarioJogo usuarioJogo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteresseTroca.
     */
    // TODO: Rename and change types and number of parameters
    public static InteresseTroca newInstance(String param1, String param2) {
        InteresseTroca fragment = new InteresseTroca();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InteresseTroca() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_interesse_troca, container, false);

        Bundle param = this.getArguments();
        if(param!=null){
            interesse = (param.getInt("interesse"));
            id_usuario = param.getInt("id_usuario");
            chave_api = param.getString("chave_api");
            id_jogo = param.getInt("id_jogo");
        }

        tvPlataforma = (TextView) fragmentView.findViewById(R.id.tvPlataforma);
        tvPlataformaDesejada = (TextView) fragmentView.findViewById(R.id.tvPlataformaDesejada);
        if(interesse == 3) {
            tvPlataformaDesejada.setText(R.string.plataformaOfertada);
            TextView tvJogoOfertado = (TextView) fragmentView.findViewById(R.id.tvJogoDesejado);
            tvJogoOfertado.setText(R.string.jogoOfertado);
        }

        tvSelecionaJogo = (TextView) fragmentView.findViewById(R.id.tvSelecionaJogo);

        if(id_jogo==0) {
            tvSelecionaJogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipo_jogo = 1;
                    abreSelecionaJogo();
                }
            });
        }else{
            tipo_jogo = 1;
            new HttpCarregaJogo((new Webservice().buscaJogo(id_jogo)),null,Jogo.class,"").execute();
        }

        tvSelecionaJogoDesejado = (TextView) fragmentView.findViewById(R.id.tvSelecionaJogoDesejado);
        tvSelecionaJogoDesejado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipo_jogo = 2;
                abreSelecionaJogo();
            }
        });

        final Spinner spPlataforma = (Spinner) fragmentView.findViewById(R.id.spPlataforma);
        final Spinner spPlataformaDesejada = (Spinner) fragmentView.findViewById(R.id.spPlataformaDesejada);

        Button btSalvarInteresse = (Button) fragmentView.findViewById(R.id.btSalvarInteresse);

        btSalvarInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_jogo!=0){
                    //plataforma = (Plataforma) spPlataforma.getSelectedItem();
                    if(spPlataforma.getSelectedItem() instanceof Plataforma){
                        plataforma = (Plataforma) spPlataforma.getSelectedItem();
                    }else{
                        plataforma = null;
                    }

                    //plataformaDesejada = (Plataforma) spPlataformaDesejada.getSelectedItem();
                    if(spPlataformaDesejada.getSelectedItem() instanceof Plataforma){
                        plataformaDesejada = (Plataforma) spPlataformaDesejada.getSelectedItem();
                    }else{
                        plataformaDesejada = null;
                    }


                    if(id_jogo_desejado==id_jogo && plataforma!=null && plataformaDesejada!=null && plataforma.getId_plataforma().equals(plataformaDesejada.getId_plataforma())) {
                        util.msgDialog(getActivity(), "Alerta", "Os jogos e plataformas não podem ser iguais!");

                    }else {
                        final Spinner spEstadoJogo = (Spinner) fragmentView.findViewById(R.id.spEstadoJogo);
                        if (spEstadoJogo.getSelectedItem() instanceof EstadoJogo) {
                            estado_jogo = (EstadoJogo) spEstadoJogo.getSelectedItem();
                        } else {
                            estado_jogo = null;
                        }

                        map = new LinkedMultiValueMap<String, String>();
                        map.add("id_jogo", String.valueOf(id_jogo));
                        map.add("id_usuario", String.valueOf(id_usuario));
                        map.add("id_interesse", String.valueOf(interesse));
                        map.add("id_estado_jogo", (estado_jogo != null) ? String.valueOf(estado_jogo.getId_estado_jogo()) : "");
                        map.add("id_nivel", "1");
                        map.add("distancia", "");
                        map.add("id_plataforma", (plataforma != null) ? String.valueOf(plataforma.getId_plataforma()) : "");
                        map.add("preco", "");
                        map.add("id_jogo_troca", (id_jogo_desejado != 0) ? String.valueOf(id_jogo_desejado) : "");
                        map.add("id_plataforma_troca", (plataformaDesejada != null) ? String.valueOf(plataformaDesejada.getId_plataforma()) : "");
                        map.add("preco_inicial", "");
                        map.add("preco_final", "");
                        salvarInteresse(map);
                    }
                }else{
                    util.msgDialog(getActivity(), "Alerta", "Selecione o jogo!");
                }
            }
        });

        try {
            //plataformas = db.selectPlataforma();
            estados_jogo = db.selectEstadoJogo();
        }catch (Exception e){
            e.printStackTrace();
        }

        spPlataforma.setVisibility(View.GONE);
        tvPlataforma.setVisibility(View.GONE);

        spPlataformaDesejada.setVisibility(View.GONE);
        tvPlataformaDesejada.setVisibility(View.GONE);

        final Spinner spEstadoJogo = (Spinner) fragmentView.findViewById(R.id.spEstadoJogo);
        util.carregaSpinnerHint(spEstadoJogo, getActivity(), estados_jogo);

        return fragmentView;
    }

    private class HttpCarregaJogo extends Http {
        public HttpCarregaJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            Jogo jogo = (Jogo) retorno;
            if(jogo!=null){
                carregaJogo(jogo);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void salvarInteresse(MultiValueMap<String, String> map) {
        if (mListener != null) {
            mListener.onTroca(map);
        }
    }

    public void abreSelecionaJogo(){
        if (mListenerJogo != null) {
            mListenerJogo.OnAbreSelecionaJogo();
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
        if (context instanceof OnTrocaListener && context instanceof OnAbreSelecionaJogoListener) {
            mListener = (OnTrocaListener) context;
            mListenerJogo = (OnAbreSelecionaJogoListener) context;
            db = new SQLiteHandler(context);
        } else {
            throw new ClassCastException(context.toString() + " must implemenet InteresseTroca.OnTrocaListener and InteresseTroca.OnAbreSelecionaJogoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mListenerJogo = null;
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

    public interface OnAbreSelecionaJogoListener {
        // TODO: Update argument type and name
        public void OnAbreSelecionaJogo();
    }

    public interface OnTrocaListener {
        // TODO: Update argument type and name
        public void onTroca(MultiValueMap<String, String> map);
    }

    public void carregaJogo(Jogo jogo){

        if(tipo_jogo==1){
            id_jogo = jogo.getId_jogo();
            tvSelecionaJogo.setText(jogo.getDescricao());
            plataformas = jogo.getPlataformas();

            final Spinner spPlataforma = (Spinner) fragmentView.findViewById(R.id.spPlataforma);
            util.carregaSpinner(spPlataforma, getActivity(), plataformas, null);
            spPlataforma.setVisibility(View.VISIBLE);
            tvPlataforma.setVisibility(View.VISIBLE);

        }else if(tipo_jogo==2){
            id_jogo_desejado = jogo.getId_jogo();
            tvSelecionaJogoDesejado.setText(jogo.getDescricao());
            plataformas = jogo.getPlataformas();

            final Spinner spPlataformaDesejada = (Spinner) fragmentView.findViewById(R.id.spPlataformaDesejada);
            util.carregaSpinner(spPlataformaDesejada, getActivity(), plataformas, null);
            spPlataformaDesejada.setVisibility(View.VISIBLE);
            tvPlataformaDesejada.setVisibility(View.VISIBLE);
        }
    }
}