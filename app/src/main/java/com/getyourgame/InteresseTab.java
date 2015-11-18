package com.getyourgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.getyourgame.model.UsuarioJogo;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.MultiValueMap;

public class InteresseTab extends AppCompatActivity implements InteresseTroca.OnTrocaListener, InteresseCompra.OnCompraListener, InteresseVenda.OnVendaListener {

    FragmentManager manager;
    Util util;
    int tipo;
    int interesse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesse_tab);

        util = new Util();
        manager = getFragmentManager();

        Bundle recebe = getIntent().getExtras();
        tipo = recebe.getInt("tipo");


        RadioButton rbCompraVenda = (RadioButton) findViewById(R.id.rbCompraVenda);

        if(tipo==1){ //Tenho
            rbCompraVenda.setText(R.string.venda);
        }else if(tipo==2){ //Quero
            rbCompraVenda.setText(R.string.compra);
        }

        RadioGroup rgInteresses = (RadioGroup) findViewById(R.id.rgInteresses);

        rgInteresses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbTroca) {

                    finalizaFragment("interesse_compra");
                    finalizaFragment("interesse_venda");

                    if(tipo==1){
                        interesse = 1;
                    }else{
                        interesse = 3;
                    }

                    Bundle param = new Bundle();
                    param.putInt("interesse", interesse);

                    Fragment fr = getFragmentManager().findFragmentByTag("interesse_troca");
                    if (fr == null) {
                        InteresseTroca troca = new InteresseTroca();
                        troca.setArguments(param);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.layoutFragments, troca, "interesse_troca");
                        transaction.commit();
                    }


                } else if (i == R.id.rbCompraVenda) {

                    if(tipo==1) { //Tenho
                        finalizaFragment("interesse_troca");
                        finalizaFragment("interesse_compra");

                        Bundle param = new Bundle();
                        param.putInt("interesse", 2);

                        Fragment fr = getFragmentManager().findFragmentByTag("interesse_venda");
                        if (fr == null) {
                            InteresseVenda venda = new InteresseVenda();
                            venda.setArguments(param);
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.add(R.id.layoutFragments, venda, "interesse_venda");
                            transaction.commit();
                        }

                    }else if(tipo==2) { //Quero

                        finalizaFragment("interesse_troca");
                        finalizaFragment("interesse_venda");

                        Bundle param = new Bundle();
                        param.putInt("interesse", 4);

                        Fragment fr = getFragmentManager().findFragmentByTag("interesse_compra");
                        if (fr == null) {
                            InteresseCompra compra = new InteresseCompra();
                            compra.setArguments(param);
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.add(R.id.layoutFragments, compra, "interesse_compra");
                            transaction.commit();
                        }
                    }
                }
            }
        });
    }

    public void finalizaFragment(String tag){
        Fragment fr = getFragmentManager().findFragmentByTag(tag);
        if (fr != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fr);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interesse_tab, menu);
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

    @Override
    public void onTroca(String teste) {
        util.toast(this, teste);
    }

    @Override
    public void onCompra(MultiValueMap<String, String> map) {
        new HttpUsuarioJogo((new Webservice()).insereUsuarioJogo(),map,UsuarioJogo.class,"").execute();
    }

    @Override
    public void OnVenda(String teste) {
        util.toast(this, teste);
    }

    private class HttpUsuarioJogo extends Http {
        public HttpUsuarioJogo(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            UsuarioJogo usuarioJogo = (UsuarioJogo) retorno;
            util.toast(getApplicationContext(), usuarioJogo.getMessage());
        }
    }
}
