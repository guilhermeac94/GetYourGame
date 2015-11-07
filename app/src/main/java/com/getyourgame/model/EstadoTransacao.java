package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class EstadoTransacao {
    private Integer id_estado_transacao;
    private String descricao;

    public EstadoTransacao() {
    }

    public Integer getId_estado_transacao() {
        return id_estado_transacao;
    }

    public void setId_estado_transacao(Integer id_estado_transacao) {
        this.id_estado_transacao = id_estado_transacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
