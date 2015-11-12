package com.getyourgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.getyourgame.model.EstadoJogo;
import com.getyourgame.model.MetodoEnvio;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.transform.Result;

public class testePicture extends AppCompatActivity {


    private static int RESULT_LOAD_IMAGE = 1;

        Util util = new Util();
        Integer id_usuario;
        String chave_api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_picture);
        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());


        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Button btSalvar = (Button) findViewById(R.id.btnSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("foto", util.BitMapToString(((BitmapDrawable)imageView.getDrawable()).getBitmap()));

                Webservice ws = new Webservice();
                new HttpAtualizaUsuario(ws.atualizarUsuario(id_usuario),map,Usuario.class,"").execute();
            }
        });
    }

    private class HttpAtualizaUsuario extends Http {
        public HttpAtualizaUsuario(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            Usuario usuario = (Usuario) retorno;
            if(!usuario.getError()) {
                Bundle param = new Bundle();
                param.putInt("id_usuario", id_usuario);
                param.putString("chave_api", chave_api);
                redirecionar(testePicture.this, Principal.class, param);

                util.toast(testePicture.this, "Foto salva com sucesso!");
                testePicture.this.finish();
            }else{
                util.msgDialog(testePicture.this, "Alerta", usuario.getMessage());
            }
        }
    }


    public void redirecionar(Activity atual, Class destino, Bundle param){
        Intent intentPrincipal = new Intent(atual, destino);
        intentPrincipal.putExtras(param);
        startActivity(intentPrincipal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
}
