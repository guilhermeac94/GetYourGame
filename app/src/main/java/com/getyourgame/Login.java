package com.getyourgame;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.getyourgame.model.Usuario;
import com.getyourgame.util.Http;
import com.getyourgame.util.Util;
import com.getyourgame.util.Webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URL;


public class Login extends AppCompatActivity {

    Util util = new Util();
    MultiValueMap<String, String> FBmap;
    Boolean mata_sessao = false;
    String urlFoto;

    private CallbackManager mCallbackManager;

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        FBmap = new LinkedMultiValueMap<String, String>();
                        FBmap.add("nome", jsonObject.getString("name"));
                        FBmap.add("email", jsonObject.getString("email"));
                        FBmap.add("senha", jsonObject.getString("id"));
                        urlFoto = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");

                        new HttpBuscaEmailFB((new Webservice()).buscaUsuarioEmail(jsonObject.getString("email")), null, Usuario.class, "").execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle params = new Bundle();
            params.putString("fields", "id,name,email,picture");
            request.setParameters(params);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private class HttpBuscaEmailFB extends Http {
        public HttpBuscaEmailFB(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Login.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    if (usuario.getTem_transacao()) {

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(1, util.createNotification(Login.this, Oportunidades.class, "Transações", "Existem novas transações"));
                    }
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", usuario.getId_usuario());
                    param.putString("chave_api", usuario.getChave_api());
                    redirecionar(Login.this, Principal.class, param);
                    util.toast(getApplicationContext(), "Login efetuado com sucesso!!!");
                } else {
                    new HttpCadastroFB((new Webservice()).cadastro(), FBmap, Usuario.class, "").execute();
                }
            }
        }
    }

    private class HttpCadastroFB extends Http {
        public HttpCadastroFB(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Login.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    new RecuperaFotoFacebook(urlFoto).execute(usuario.getId_usuario());
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", usuario.getId_usuario());
                    param.putString("chave_api", usuario.getChave_api());
                    param.putString("primeiro_cadastro", "1");
                    param.putString("redirecionar", "preferencias_usuario");
                    redirecionar(Login.this, Contatos.class, param);
                    util.toast(getApplicationContext(), "Login efetuado com sucesso!");
                } else {
                    util.msgDialog(Login.this, "Alerta", usuario.getMessage());
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();

        getSupportActionBar().hide();

        if (mata_sessao) {
            LoginManager.getInstance().logOut();
        }

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etSenha = (EditText) findViewById(R.id.etSenha);
        etEmail.requestFocus();

        Button btEntrar = (Button) findViewById(R.id.btEntrar);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("email", String.valueOf(etEmail.getText().toString()));
                map.add("senha", String.valueOf(etSenha.getText().toString()));

                Usuario usuario = new Usuario();
                Webservice ws = new Webservice();
                new HttpLogin(ws.login(), map, Usuario.class, "").execute();
            }
        });


        TextView tvCadastrar = (TextView) findViewById(R.id.tvCadastrar);

        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent tela1 = new Intent(Login.this, Cadastro.class);
                startActivity(tela1);
            }
        });


        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile newProfile) {
            }
        };

        LoginButton FBLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        FBLoginButton.registerCallback(mCallbackManager, mCallBack);


        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request2 = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                        map.add("email", jsonObject.getString("email"));
                        map.add("senha", jsonObject.getString("id"));

                        new HttpLoginFB((new Webservice()).login(), map, Usuario.class, "").execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle params2 = new Bundle();
            params2.putString("fields", "id,name,email");
            request2.setParameters(params2);
            request2.executeAsync();
        }
    }

    private class HttpLoginFB extends Http {
        public HttpLoginFB(Webservice ws, MultiValueMap<String, String> map, Class classe, String apikey) {
            super(ws, map, classe, apikey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Login.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    if (usuario.getTem_transacao()) {

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(1, util.createNotification(Login.this, Oportunidades.class, "Transações", "Existem novas transações"));

                    }
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", usuario.getId_usuario());
                    param.putString("chave_api", usuario.getChave_api());
                    redirecionar(Login.this, Principal.class, param);
                    util.toast(getApplicationContext(), "Login efetuado com sucesso!!!");
                } else {
                    util.msgDialog(Login.this, "Alerta", usuario.getMessage());
                }
            }
        }
    }

    private class HttpLogin extends Http {
        public HttpLogin(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Login.this, "Alerta", "Erro ao conectar com o servidor.");
            }else {
                Usuario usuario = (Usuario) retorno;
                if (!usuario.getError()) {
                    if (usuario.getTem_transacao()) {

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(1, util.createNotification(Login.this, Oportunidades.class, "Transações", "Existem novas transações"));

                    }
                    Bundle param = new Bundle();
                    param.putInt("id_usuario", usuario.getId_usuario());
                    param.putString("chave_api", usuario.getChave_api());
                    redirecionar(Login.this, Principal.class, param);
                    util.toast(getApplicationContext(), "Login efetuado com sucesso!!!");
                } else {
                    util.msgDialog(Login.this, "Alerta", usuario.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void redirecionar(Activity atual, Class destino, Bundle param) {
        Intent intentPrincipal = new Intent(atual, destino);
        intentPrincipal.putExtras(param);
        startActivity(intentPrincipal);
    }


    private class HttpAtualizaUsuarioFB extends Http {
        public HttpAtualizaUsuarioFB(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey) {
            super(ws, map, classe, apiKey);
        }

        @Override
        protected void onPostExecute(Object retorno) {
            if(retorno instanceof Exception){
                util.msgDialog(Login.this, "Alerta", "Erro ao conectar com o servidor.");
            }
        }
    }

    class RecuperaFotoFacebook extends AsyncTask<Integer, Void, Bitmap> {

        private String url;
        private Integer id_usuario;

        public RecuperaFotoFacebook(String imageUrl) {
            this.url = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            id_usuario = params[0];
            Bitmap retVal = null;
            try {
                URL imageUrl = new URL(url);
                return BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            } catch (Exception ex) {
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("foto", util.BitMapToString(result));

            Webservice ws = new Webservice();
            new HttpAtualizaUsuarioFB(ws.atualizarUsuario(id_usuario), map, Usuario.class, "").execute();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
