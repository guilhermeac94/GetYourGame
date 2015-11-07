package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class AvaliacaoTransacao {
    private Integer id_avaliacao_transacao;
    private Usuario usuarioAvaliador;
    private Usuario UsuarioAvaliado;
    private EstadoAvalicao estadoAvalicao;
    private String observacao;

    public AvaliacaoTransacao() {
    }

    public Integer getId_avaliacao_transacao() {
        return id_avaliacao_transacao;
    }

    public void setId_avaliacao_transacao(Integer id_avaliacao_transacao) {
        this.id_avaliacao_transacao = id_avaliacao_transacao;
    }

    public Usuario getUsuarioAvaliador() {
        return usuarioAvaliador;
    }

    public void setUsuarioAvaliador(Usuario usuarioAvaliador) {
        this.usuarioAvaliador = usuarioAvaliador;
    }

    public Usuario getUsuarioAvaliado() {
        return UsuarioAvaliado;
    }

    public void setUsuarioAvaliado(Usuario usuarioAvaliado) {
        UsuarioAvaliado = usuarioAvaliado;
    }

    public EstadoAvalicao getEstadoAvalicao() {
        return estadoAvalicao;
    }

    public void setEstadoAvalicao(EstadoAvalicao estadoAvalicao) {
        this.estadoAvalicao = estadoAvalicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
