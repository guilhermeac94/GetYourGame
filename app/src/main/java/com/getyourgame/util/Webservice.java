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

    public Webservice buscaJogosDoUsuario(int id_usuario){
        return new Webservice("jogos_do_usuario/"+id_usuario, HttpMethod.GET, false);
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

    public Webservice buscaContatoTransacao(int id_usuario){
        return new Webservice("contato_transacao/"+id_usuario, HttpMethod.GET, false);
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

    public Webservice deletaTransacao(int id_transacao){
        return new Webservice("transacao/"+id_transacao, HttpMethod.DELETE, false);
    }

    public Webservice salvaAvaliacao(){
        return new Webservice("avaliacao_transacao", HttpMethod.POST, false);
    }

    public Webservice buscaAvaliacao(int id_transacao, int id_usuario_avaliador){
        return new Webservice("avaliacao_transacao/"+id_transacao+"/"+id_usuario_avaliador, HttpMethod.GET, false);
    }

    public Webservice buscaAvaliacoesUsuario(int id_usuario){
        return new Webservice("avaliacoes_usuario/"+id_usuario, HttpMethod.GET, false);
    }

    public Webservice buscaInteresses(int id_usuario, int id_interesse){
        return new Webservice("interesse/"+id_usuario+"/"+id_interesse, HttpMethod.GET, false);
    }

    public Webservice deletaInteresse(int id_usuario_jogo){
        return new Webservice("interesse/"+id_usuario_jogo, HttpMethod.DELETE, false);
    }

    public Webservice buscaFotos(int id_usuario_jogo){
        return new Webservice("fotos/"+id_usuario_jogo, HttpMethod.GET, false);
    }
}
