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

    private OnTrocaListener mListener;
    private View fragmentView;

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
        // Inflate the layout for this fragment

        fragmentView = inflater.inflate(R.layout.fragment_interesse_troca, container, false);

        Button btSalvarInteresse = (Button) fragmentView.findViewById(R.id.btSalvarInteresse);

        btSalvarInteresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarInteresse("teste interação TROCA");
            }
        });

        EditText etInteresse1 = (EditText) fragmentView.findViewById(R.id.etInteresse1);
        Bundle param = this.getArguments();
        if(param!=null){
            etInteresse1.setText(param.getString("jogo"));
        }

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void salvarInteresse(String teste) {
        if (mListener != null) {
            mListener.onTroca(teste);
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
        if (context instanceof OnTrocaListener) {
            mListener = (OnTrocaListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implemenet InteresseTroca.OnTrocaListener");
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
    public interface OnTrocaListener {
        // TODO: Update argument type and name
        public void onTroca(String teste);
    }
}