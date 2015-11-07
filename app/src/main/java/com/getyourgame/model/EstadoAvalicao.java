package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class EstadoAvalicao {
    private Integer id_estado_avaliacao;
    private String descricao;

    public EstadoAvalicao() {
    }

    public Integer getId_estado_avaliacao() {
        return id_estado_avaliacao;
    }

    public void setId_estado_avaliacao(Integer id_estado_avaliacao) {
        this.id_estado_avaliacao = id_estado_avaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
