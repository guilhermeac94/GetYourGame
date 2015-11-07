package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class EstadoJogo {
    private Integer id_estado_jogo;
    private String descricao;

    public EstadoJogo() {
    }

    public Integer getId_estado_jogo() {
        return id_estado_jogo;
    }

    public void setId_estado_jogo(Integer id_estado_jogo) {
        this.id_estado_jogo = id_estado_jogo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
