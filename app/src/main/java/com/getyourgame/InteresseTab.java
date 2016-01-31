package com.getyourgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.getyourgame.model.Jogo;
import com.getyourgame.model.UsuarioJogo;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

public class InteresseTab extends AppCompatActivity implements InteresseTroca.OnTrocaListener, InteresseTroca.OnAbreSelecionaJogoListener, InteresseCompra.OnCompraListener, InteresseCompra.OnAbreSelecionaJogoListener, InteresseVenda.OnVendaListener, InteresseVenda.OnAbreSelecionaJogoListener, ListaJogos.OnSelecionaJogoListener {

    FragmentManager manager;
    Util util = new Util();
    int tipo;
    int interesse;
    String fragment_atual;

    Integer id_usuario;
    String chave_api;

    Integer id_jogo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesse_tab);

        getSupportActionBar().hide();

        manager = getFragmentManager();
        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());

        Bundle recebe = getIntent().getExtras();
        tipo = recebe.getInt("tipo");
        id_jogo = recebe.getInt("id_jogo");

        RadioButton rbCompraVenda = (RadioButton) findViewById(R.id.rbCompraVenda);

        if (tipo == 1) { //Tenho
            rbCompraVenda.setText(R.string.venda);
        } else if (tipo == 2) { //Quero
            rbCompraVenda.setText(R.string.compra);
        }

        RadioGroup rgInteresses = (RadioGroup) findViewById(R.id.rgInteresses);

        rgInteresses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbTroca) {

                    finalizaFragment("interesse_compra");
                    finalizaFragment("interesse_venda");
                    finalizaFragment("lista_jogos");

                    if (tipo == 1) {
                        interesse = 1;
                    } else {
                        interesse = 3;
                    }

                    Bundle param = new Bundle();
                    param.putInt("interesse", interesse);
                    param.putInt("id_usuario", id_usuario);
                    param.putString("chave_api", chave_api);
                    param.putInt("id_jogo", id_jogo);

                    Fragment fr = manager.findFragmentByTag("interesse_troca");
                    if (fr == null) {
                        InteresseTroca troca = new InteresseTroca();
                        troca.setArguments(param);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.layoutFragments, troca, "interesse_troca");
                        transaction.commit();
                        fragment_atual = "interesse_troca";
                    }

                } else if (i == R.id.rbCompraVenda) {

                    if (tipo == 1) { //Tenho
                        finalizaFragment("interesse_troca");
                        finalizaFragment("interesse_compra");
                        finalizaFragment("lista_jogos");

                        Bundle param = new Bundle();

                        interesse = 2;
                        param.putInt("interesse", interesse);
                        param.putInt("id_usuario", id_usuario);
                        param.putString("chave_api", chave_api);
                        param.putInt("id_jogo", id_jogo);

                        Fragment fr = manager.findFragmentByTag("interesse_venda");
                        if (fr == null) {
                            InteresseVenda venda = new InteresseVenda();
                            venda.setArguments(param);
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.add(R.id.layoutFragments, venda, "interesse_venda");
                            transaction.commit();
                            fragment_atual = "interesse_venda";
                        }

                    } else if (tipo == 2) { //Quero

                        finalizaFragment("interesse_troca");
                        finalizaFragment("interesse_venda");
                        finalizaFragment("lista_jogos");

                        Bundle param = new Bundle();

                        interesse = 4;
                        param.putInt("interesse", interesse);
                        param.putInt("id_usuario", id_usuario);
                        param.putString("chave_api", chave_api);
                        param.putInt("id_jogo", id_jogo);

                        Fragment fr = manager.findFragmentByTag("interesse_compra");
                        if (fr == null) {
                            InteresseCompra compra = new InteresseCompra();
                            compra.setArguments(param);
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.add(R.id.layoutFragments, compra, "interesse_compra");
                            transaction.commit();
                            fragment_atual = "interesse_compra";
                        }
                    }
                }
            }
        });
    }

    public void finalizaFragment(String tag) {
        Fragment fr = manager.findFragmentByTag(tag);
        if (fr != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fr);
            transaction.commit();
        }
    }

    @Override
    public void onTroca(MultiValueMap<String, String> map) {
        new HttpUsuarioJogo((new Webservice()).insereUsuarioJogo(), map, UsuarioJogo.class, "").execute();
    }

    @Override
    public void onCompra(MultiValueMap<String, String> map) {
        new HttpUsuarioJogo((new Webservice()).insereUsuarioJogo(), map, UsuarioJogo.class, "").execute();
    }

    @Override
    public void OnVenda(MultiValueMap<String, String> map) {
        new HttpUsuarioJogo((new Webservice()).insereUsuarioJogo(), map, UsuarioJogo.class, "").execute();
    }

    private class HttpUsuarioJogo extends Http {
        public HttpUsuarioJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(InteresseTab.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                UsuarioJogo usuarioJogo = (UsuarioJogo) retorno;
                util.toast(getApplicationContext(), usuarioJogo.getMessage());
                if (!usuarioJogo.getError()) {
                    InteresseTab.this.finish();
                }
            }
        }
    }

    @Override
    public void OnAbreSelecionaJogo(Integer interesse) {
        Fragment fr = manager.findFragmentByTag(fragment_atual);
        ListaJogos listaJogos = new ListaJogos();

        Bundle param = new Bundle();
        param.putInt("id_usuario", id_usuario);
        param.putString("chave_api", chave_api);

        if (interesse != null) {
            param.putInt("interesse", interesse);
        }
        listaJogos.setArguments(param);

        manager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fr)
                .add(R.id.layoutFragments, listaJogos, "lista_jogos")
                .commit();
    }

    @Override
    public void OnSelecionaJogo(Jogo jogo) {
        finalizaFragment("lista_jogos");

        if (interesse == 4) {
            InteresseCompra fr = (InteresseCompra) manager.findFragmentByTag("interesse_compra");
            fr.carregaJogo(jogo);

        } else if (interesse == 2) {
            InteresseVenda fr = (InteresseVenda) manager.findFragmentByTag("interesse_venda");
            fr.carregaJogo(jogo);

        } else if (interesse == 1 || interesse == 3) {
            InteresseTroca fr = (InteresseTroca) manager.findFragmentByTag("interesse_troca");
            fr.carregaJogo(jogo);
        }

        manager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(manager.findFragmentByTag(fragment_atual))
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (interesse == 2) {
            InteresseVenda fr = (InteresseVenda) manager.findFragmentByTag("interesse_venda");
            fr.carregaFoto(requestCode, resultCode, data);

        } else if (interesse == 1) {
            InteresseTroca fr = (InteresseTroca) manager.findFragmentByTag("interesse_troca");
            fr.carregaFoto(requestCode, resultCode, data);
        }
    }
}
