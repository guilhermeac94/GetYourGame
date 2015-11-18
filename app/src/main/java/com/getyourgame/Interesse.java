package com.getyourgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.getyourgame.util.Util;

import java.net.SocketPermission;

public class Interesse extends TabActivity /*implements InteresseTroca.OnTrocaListener, InteresseCompra.OnCompraListener*/{

    Util util;
    //FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesse);

        util = new Util();

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TENHO");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("QUERO");

        tab1.setIndicator("TENHO");
        Intent interesseTenho = new Intent(this, InteresseTab.class);
        Bundle param1 = new Bundle();
        param1.putInt("tipo", 1);
        interesseTenho.putExtras(param1);
        tab1.setContent(interesseTenho);

        tab2.setIndicator("QUERO");
        Intent interesseQuero = new Intent(this, InteresseTab.class);
        Bundle param2 = new Bundle();
        param2.putInt("tipo", 2);
        interesseQuero.putExtras(param2);
        tab2.setContent(interesseQuero);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        /*
        manager = getFragmentManager();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgInteresses);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbInteresse1) {
                    Bundle param = new Bundle();
                    param.putString("jogo", "God of War");

                    Fragment fr = getFragmentManager().findFragmentByTag("interesse_troca");
                    if (fr == null) {
                        InteresseTroca troca = new InteresseTroca();
                        troca.setArguments(param);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.layoutInteresse, troca, "interesse_troca");
                        transaction.commit();
                    }


                } else if (i == R.id.rbInteresse2) {
                    Bundle param = new Bundle();
                    param.putString("jogo", "Rocket League");

                    Fragment fr = getFragmentManager().findFragmentByTag("interesse_compra_venda");
                    if (fr == null) {
                        InteresseCompra compra_venda = new InteresseCompra();
                        compra_venda.setArguments(param);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.layoutInteresse, compra_venda, "interesse_compra_venda");
                        transaction.commit();
                    }

                } else if (i == R.id.rbInteresse3) {
                    finalizaFragment("interesse_troca");
                } else if (i == R.id.rbInteresse4) {
                    finalizaFragment("interesse_compra_venda");
                }
            }
        });
        */
    }

    /*
    @Override
    public void onTroca(String teste){
        util.toast(Interesse.this, ">>>>>>>>>>>>>>>>>> " + teste);
        finalizaFragment("interesse_troca");
    }


    @Override
    public void onCompra(String teste) {
        util.toast(Interesse.this, ">>>>>>>>>>>>>>>>>> " + teste);
        finalizaFragment("interesse_compra_venda");
    }


    public void finalizaFragment(String tag){
        Fragment fr = getFragmentManager().findFragmentByTag(tag);
        if (fr != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fr);
            transaction.commit();
        }
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interesse, menu);
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

}
