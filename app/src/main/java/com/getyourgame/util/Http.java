package com.getyourgame.util;

import android.os.AsyncTask;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by Guilherme on 14/09/2015.
 */
public class Http extends AsyncTask<String, Void, Object> {
    
    private Webservice ws;
    private MultiValueMap<String, String> map;
    private Class classe;
    private String apiKey;
    
    public Http(Webservice ws, MultiValueMap<String, String> map, Class classe, String apiKey){
        this.ws = ws;
        this.map = map;
        this.classe = classe;
        this.apiKey = apiKey;
    }
    
    @Override
    protected Object doInBackground(String... params) {
        try {
            String url = "http://10.0.2.2/getYourGameWS/getyourgame/"+ws.getServico();
            //String url = "http://192.168.25.38/getYourGameWS/getyourgame/"+ws.getServico();
            //String url = "http://192.168.1.112/getYourGameWS/getyourgame/"+ws.getServico();
            //String url = "http://192.168.0.193/getYourGameWS/getyourgame/"+ws.getServico();

           /* SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            simpleClientHttpRequestFactory.setConnectTimeout(5000);
            simpleClientHttpRequestFactory.setReadTimeout(1000);*/

            RestTemplate restTemplate = new RestTemplate();//simpleClientHttpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            if(ws.getAuth()) {
                headers.setAuthorization(new HttpAuthentication() {
                    @Override
                    public String getHeaderValue() {
                        return apiKey;
                    }
                });
            }
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(this.map, headers);

            ResponseEntity<Object> retorno = restTemplate.exchange(url, ws.getMetodo(), request, this.classe);

            return retorno.getBody();

        } catch (Exception e) {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object retorno) {

    }

}
