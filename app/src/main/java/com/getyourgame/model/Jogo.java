package com.getyourgame.model;

import java.util.List;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class Jogo {
    private Integer id_jogo;
    private String descricao;
    private Integer ano;
    private String edicao;
    private String desenvolvedor;
    private Long soma_avaliacao;
    private Long qtde_avaliacoes;
    private Byte capa;

    private List<Plataforma> plataformas;
    private List<UsuarioJogo> usuarioJogos;

    public Jogo() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(Integer id_jogo) {
        this.id_jogo = id_jogo;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getDesenvolvedor() {
        return desenvolvedor;
    }

    public void setDesenvolvedor(String desenvolvedor) {
        this.desenvolvedor = desenvolvedor;
    }

    public Long getQtde_avaliacoes() {
        return qtde_avaliacoes;
    }

    public void setQtde_avaliacoes(Long qtde_avaliacoes) {
        this.qtde_avaliacoes = qtde_avaliacoes;
    }

    public Long getSoma_avaliacao() {
        return soma_avaliacao;
    }

    public void setSoma_avaliacao(Long soma_avaliacao) {
        this.soma_avaliacao = soma_avaliacao;
    }

    public List<Plataforma> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<Plataforma> plataformas) {
        this.plataformas = plataformas;
    }

    public List<UsuarioJogo> getUsuarioJogos() {
        return usuarioJogos;
    }

    public void setUsuarioJogos(List<UsuarioJogo> usuarioJogos) {
        this.usuarioJogos = usuarioJogos;
    }

    public Byte getCapa() {
        return capa;
    }

    public void setCapa(Byte capa) {
        this.capa = capa;
    }
}
