package com.getyourgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.getyourgame.db.SQLiteHandler;
import com.getyourgame.model.EstadoJogo;
import com.getyourgame.model.Jogo;
import com.getyourgame.model.Plataforma;
import com.getyourgame.model.UsuarioJogo;
import com.getyourgame.util.Action;
import com.getyourgame.util.CustomGallery;
import com.getyourgame.util.GalleryAdapter;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVendaListener} interface
 * to handle interaction events.
 * Use the {@link InteresseVenda#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteresseVenda extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Util util = new Util();

    private OnVendaListener mListener;
    private OnAbreSelecionaJogoListener mListenerJogo;

    int interesse;
    int id_jogo = 0;

    Integer id_usuario;
    String chave_api;

    private View fragmentView;
    SQLiteHandler db;

    List<Plataforma> plataformas;
    Plataforma plataforma;

    List<EstadoJogo> estados_jogo;
    EstadoJogo estado_jogo;

    MultiValueMap<String, String> map;

    TextView tvSelecionaJogo;
    TextView tvPlataforma;

    UsuarioJogo usuarioJogo;

    ArrayList<CustomGallery> dataT;

    Context contexto;

    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;

    Button btnGalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteresseVenda.
     */
    // TODO: Rename and change types and number of parameters
    public static InteresseVenda newInstance(String param1, String param2) {
        InteresseVenda fragment = new InteresseVenda();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InteresseVenda() {
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
        fragmentView = inflater.inflate(R.layout.fragment_interesse_venda, container, false);
        dataT = new ArrayList<CustomGallery>();

        Bundle param = this.getArguments();
        if(param!=null){
            interesse = (param.getInt("interesse"));
            id_usuario = param.getInt("id_usuario");
            chave_api = param.getString("chave_api");
            id_jogo = param.getInt("id_jogo");
        }

        tvPlataforma = (TextView) fragmentView.findViewById(R.id.tvPlataforma);

        tvSelecionaJogo = (TextView) fragmentView.findViewById(R.id.tvSelecionaJogo);

        if(id_jogo==0) {
            tvSelecionaJogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abreSelecionaJogo(null);
                }
            });
        }else{
            new HttpCarregaJogo((new Webservice().buscaJogo(id_jogo)),null,Jogo.class,"").execute();
        }

        Button btSalvarInteresse = (Button) fragmentView.findViewById(R.id.btSalvarInteresse);

        btSalvarInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_jogo!=0){

                    EditText etPreco = (EditText) fragmentView.findViewById(R.id.etPreco);

                    final Spinner spPlataforma = (Spinner) fragmentView.findViewById(R.id.spPlataforma);
                    plataforma = (Plataforma) spPlataforma.getSelectedItem();
                    if(spPlataforma.getSelectedItem() instanceof Plataforma){
                        plataforma = (Plataforma) spPlataforma.getSelectedItem();
                    }else{
                        plataforma = null;
                    }

                    final Spinner spEstadoJogo = (Spinner) fragmentView.findViewById(R.id.spEstadoJogo);
                    if(spEstadoJogo.getSelectedItem() instanceof EstadoJogo){
                        estado_jogo = (EstadoJogo) spEstadoJogo.getSelectedItem();
                    }else{
                        estado_jogo = null;
                    }

                    map = new LinkedMultiValueMap<String, String>();
                    map.add("id_jogo", String.valueOf(id_jogo));
                    map.add("id_usuario", String.valueOf(id_usuario));
                    map.add("id_interesse", String.valueOf(interesse));
                    map.add("id_estado_jogo", (estado_jogo!=null)?String.valueOf(estado_jogo.getId_estado_jogo()):"");
                    map.add("id_plataforma", (plataforma!=null)?String.valueOf(plataforma.getId_plataforma()):"");
                    map.add("preco", String.valueOf(etPreco.getText()));
                    map.add("id_jogo_troca", "");
                    map.add("id_plataforma_troca", "");
                    map.add("preco_inicial", "");
                    map.add("preco_final", "");

                    int i =0;
                    for(CustomGallery cG : dataT){
                        map.add("foto"+i,util.BitMapToString(BitmapFactory.decodeFile(cG.sdcardPath)));
                        i++;
                    }

                    salvarInteresse(map);

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

        final Spinner spPlataforma = (Spinner) fragmentView.findViewById(R.id.spPlataforma);
        //spPlataforma.setEnabled(false);
        spPlataforma.setVisibility(View.GONE);
        tvPlataforma.setVisibility(View.GONE);

        //util.carregaSpinnerHint(spPlataforma, getActivity(), plataformas);

        final Spinner spEstadoJogo = (Spinner) fragmentView.findViewById(R.id.spEstadoJogo);
        util.carregaSpinnerHint(spEstadoJogo, getActivity(), estados_jogo);

        initImageLoader();
        init();

        return fragmentView;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    private void init() {

        handler = new Handler();
        gridGallery = (GridView) fragmentView.findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(contexto, imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) fragmentView.findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        btnGalleryPickMul = (Button) fragmentView.findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                getActivity().startActivityForResult(i, 200);
            }
        });

    }

    public void carregaFoto(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }

    private class HttpCarregaJogo extends Http {
        public HttpCarregaJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }
        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(getActivity(), "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Jogo jogo = (Jogo) retorno;
                if (jogo != null) {
                    carregaJogo(jogo);
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void salvarInteresse(MultiValueMap<String, String> map) {
        if (mListener != null) {
            mListener.OnVenda(map);
        }
    }

    public void abreSelecionaJogo(Integer interesse){
        if (mListenerJogo != null) {
            mListenerJogo.OnAbreSelecionaJogo(interesse);
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
        if (context instanceof OnVendaListener && context instanceof OnAbreSelecionaJogoListener) {
            contexto = context;
            mListener = (OnVendaListener) context;
            mListenerJogo = (OnAbreSelecionaJogoListener) context;
            db = new SQLiteHandler(context);
        } else {
            throw new ClassCastException(context.toString() + " must implemenet InteresseVenda.OnVendaListener and InteresseVenda.OnAbreSelecionaJogoListener");
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
        public void OnAbreSelecionaJogo(Integer interesse);
    }

    public interface OnVendaListener {
        // TODO: Update argument type and name
        public void OnVenda(MultiValueMap<String, String> map);
    }

    public void carregaJogo(Jogo jogo){

        id_jogo = jogo.getId_jogo();
        tvSelecionaJogo.setText(jogo.getDescricao());
        plataformas = jogo.getPlataformas();

        final Spinner spPlataforma = (Spinner) fragmentView.findViewById(R.id.spPlataforma);
        util.carregaSpinner(spPlataforma, getActivity(), plataformas, null);
        //spPlataforma.setEnabled(true);
        spPlataforma.setVisibility(View.VISIBLE);
        tvPlataforma.setVisibility(View.VISIBLE);
    }
}
