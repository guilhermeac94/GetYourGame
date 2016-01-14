package com.getyourgame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class CarregaFotoUsuario extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    Util util = new Util();
    Integer id_usuario;
    String chave_api;
    String foto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrega_foto_usuario);
        id_usuario = util.recebeIdUsuario(getIntent());
        chave_api = util.recebeChaveApi(getIntent());
        foto = util.recebeFoto(getIntent());


        ImageView ivLoadFoto = (ImageView) findViewById(R.id.ivLoadFoto);
        ivLoadFoto.setImageBitmap(util.StringToBitMap(foto));

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
                ImageView ivLoadFoto = (ImageView) findViewById(R.id.ivLoadFoto);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("foto", util.BitMapToString(((BitmapDrawable)ivLoadFoto.getDrawable()).getBitmap()));

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
                redirecionar(CarregaFotoUsuario.this, Principal.class, param, true);

                util.toast(getApplicationContext(), "Foto salva com sucesso!");
                CarregaFotoUsuario.this.finish();
            }else{
                util.msgDialog(CarregaFotoUsuario.this, "Alerta", usuario.getMessage());
            }
        }
    }


    public void redirecionar(Activity atual, Class destino, Bundle param, Boolean mata){
        Intent intentPrincipal = new Intent(atual, destino);
        intentPrincipal.putExtras(param);
        if (mata){
            intentPrincipal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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

            ImageView ivLoadFoto = (ImageView) findViewById(R.id.ivLoadFoto);
            ivLoadFoto.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 256, 300, false));
        }


    }
}