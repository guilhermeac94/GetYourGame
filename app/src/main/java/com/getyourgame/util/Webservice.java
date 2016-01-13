package com.getyourgame.util;

import org.springframework.http.HttpMethod;

/**
 * Created by Guilherme on 14/09/2015.
 */
public class Webservice {
    private String servico;
    private HttpMethod metodo;
    private Boolean auth;

    public Webservice(){
    }

    public Webservice(String servico, HttpMethod metodo, Boolean auth){
        this.servico = servico;
        this.metodo = metodo;
        this.auth = auth;
    }

    public String getServico() {
        return servico;
    }

    public Boolean getAuth() {
        return auth;
    }

    public HttpMethod getMetodo() {
        return metodo;
    }

    public Webservice cadastro(){
        return new Webservice("cadastro", HttpMethod.POST, false);
    }

    public Webservice login(){
        return new Webservice("login", HttpMethod.POST, false);
    }

    public Webservice buscaUsuario(int id_usuario){
        return new Webservice("usuario/"+id_usuario, HttpMethod.GET, true);
    }
    public Webservice buscaUsuarioEmail(String email){
        return new Webservice("usuario_email/"+email, HttpMethod.GET, false);
    }

    public Webservice atualizarUsuario(int id_usuario){
        return new Webservice("usuario/"+id_usuario, HttpMethod.PUT, false);
    }

    public Webservice estadoJogo(){
        return new Webservice("estado_jogo", HttpMethod.GET, false);
    }

    public Webservice buscaCadastros(){
        return new Webservice("cadastros", HttpMethod.GET, false);
    }
    public Webservice buscaUsuarios(){
        return new Webservice("usuarios", HttpMethod.POST, false);
    }
    public Webservice buscaJogos(){
        return new Webservice("jogo", HttpMethod.POST, false);
    }

    public Webservice buscaUsuarioTemJogo(int id_jogo){
        return new Webservice("usuario_tem_jogo/"+id_jogo, HttpMethod.GET, false);
    }

    public Webservice buscaJogo(int id_jogo){
        return new Webservice("jogo/"+id_jogo, HttpMethod.GET, false);
    }

    public Webservice insereUsuarioJogo(){
        return new Webservice("usuario_jogo", HttpMethod.POST, false);
    }

    public Webservice buscaOportunidades(int id_usuario){
        return new Webservice("oportunidades/"+id_usuario, HttpMethod.GET, false);
    }

    public Webservice buscaDadosOportunidade(int id_usuario_jogo_solic, int id_usuario_jogo_ofert){
        return new Webservice("dados_oportunidade/"+id_usuario_jogo_solic+"/"+id_usuario_jogo_ofert, HttpMethod.GET, false);
    }

    public Webservice buscaPreferencias(int id_usuario){
        return new Webservice("preferencias/"+id_usuario, HttpMethod.GET, false);
    }

    public Webservice buscaContatos(int id_usuario){
        return new Webservice("contato/"+id_usuario, HttpMethod.GET, false);
    }

    public Webservice salvaContatos(int id_usuario){
        return new Webservice("contato/"+id_usuario, HttpMethod.PUT, false);
    }

    public Webservice buscaEndereco(int id_usuario){
        return new Webservice("endereco/"+id_usuario, HttpMethod.GET, false);
    }

    public Webservice insereTransacao(){
        return new Webservice("transacao", HttpMethod.POST, false);
    }

    public Webservice buscaTransacoes(int id_usuario, int status){
        return new Webservice("transacao/"+id_usuario+"/"+status, HttpMethod.GET, false);
    }

    public Webservice buscaDadosTransacao(int id_transacao){
        return new Webservice("dados_transacao/"+id_transacao, HttpMethod.GET, false);
    }

    public Webservice atualizaTransacao(int id_transacao){
        return new Webservice("transacao/"+id_transacao, HttpMethod.PUT, false);
    }
}
