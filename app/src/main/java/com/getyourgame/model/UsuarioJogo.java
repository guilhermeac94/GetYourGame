package com.getyourgame.model;

import java.util.List;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class UsuarioJogo {
    private Integer id_usuario_jogo;
    private Usuario usuario;
    private Interesse interesse;
    private Nivel nivel;
    private Integer distancia;
    private Number preco;
    private Jogo jogo;
    private Plataforma plataforma;
    private Number preco_inicial;
    private Number preco_final;
    private Jogo jogoTroca;
    private Plataforma plataformaTroca;
    private Boolean error;
    private String message;

    private List<Foto> fotos;
    public UsuarioJogo() {
    }

    public Integer getId_usuario_jogo() {
        return id_usuario_jogo;
    }

    public void setId_usuario_jogo(Integer id_usuario_jogo) {
        this.id_usuario_jogo = id_usuario_jogo;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Interesse getInteresse() {
        return interesse;
    }

    public void setInteresse(Interesse interesse) {
        this.interesse = interesse;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public Number getPreco() {
        return preco;
    }

    public void setPreco(Number preco) {
        this.preco = preco;
    }

    public Number getPreco_inicial() {
        return preco_inicial;
    }

    public void setPreco_inicial(Number preco_inicial) {
        this.preco_inicial = preco_inicial;
    }

    public Number getPreco_final() {
        return preco_final;
    }

    public void setPreco_final(Number preco_final) {
        this.preco_final = preco_final;
    }

    public Plataforma getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    public Jogo getJogoTroca() {
        return jogoTroca;
    }

    public void setJogoTroca(Jogo jogoTroca) {
        this.jogoTroca = jogoTroca;
    }

    public Plataforma getPlataformaTroca() {
        return plataformaTroca;
    }

    public void setPlataformaTroca(Plataforma plataformaTroca) {
        this.plataformaTroca = plataformaTroca;
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
}
