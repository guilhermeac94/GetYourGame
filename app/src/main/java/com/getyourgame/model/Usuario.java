package com.getyourgame.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * Created by Guilherme on 17/08/2015.
 */
public class Usuario {

    private Integer id_usuario;
    private String nome;
    private String email;
    private String senha;
    private String chave_api;
    private Boolean gps;
    private MetodoEnvio metodoEnvio;
    private EstadoJogo estadoJogo;
    private Integer distancia;
    private Boolean error;
    private String message;
    private Boolean tem_transacao;

    private List<Telefone> telefones;
    private List<Endereco> enderecos;

    public Usuario() {
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getChave_api() {
        return chave_api;
    }

    public void setChave_api(String chave_api) {
        this.chave_api = chave_api;
    }

    public Boolean getGps() {
        return gps;
    }

    public void setGps(Boolean gps) {
        this.gps = gps;
    }

    public MetodoEnvio getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(MetodoEnvio metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public EstadoJogo getEstadoJogo() {
        return estadoJogo;
    }

    public void setEstadoJogo(EstadoJogo estadoJogo) {
        this.estadoJogo = estadoJogo;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getTem_transacao() {
        return tem_transacao;
    }

    public void setTem_transacao(Boolean tem_transacao) {
        this.tem_transacao = tem_transacao;
    }
}
