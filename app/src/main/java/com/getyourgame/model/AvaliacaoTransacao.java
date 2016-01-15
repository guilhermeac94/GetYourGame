package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class AvaliacaoTransacao {
    private Integer id_avaliacao_transacao;
    private Integer id_transacao;
    private Integer id_usuario_avaliado;
    private Integer id_usuario_avaliador;
    private Integer avalicao;
    private String observacao;

    public AvaliacaoTransacao() {
    }

    public Integer getId_avaliacao_transacao() {
        return id_avaliacao_transacao;
    }

    public void setId_avaliacao_transacao(Integer id_avaliacao_transacao) {
        this.id_avaliacao_transacao = id_avaliacao_transacao;
    }

    public Integer getId_transacao() {
        return id_transacao;
    }

    public void setId_transacao(Integer id_transacao) {
        this.id_transacao = id_transacao;
    }

    public Integer getId_usuario_avaliador() {
        return id_usuario_avaliador;
    }

    public void setId_usuario_avaliador(Integer id_usuario_avaliador) {
        this.id_usuario_avaliador = id_usuario_avaliador;
    }

    public Integer getId_usuario_avaliado() {
        return id_usuario_avaliado;
    }

    public void setId_usuario_avaliado(Integer id_usuario_avaliado) {
        this.id_usuario_avaliado = id_usuario_avaliado;
    }

    public Integer getAvalicao() {
        return avalicao;
    }

    public void setAvalicao(Integer avalicao) {
        this.avalicao = avalicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
