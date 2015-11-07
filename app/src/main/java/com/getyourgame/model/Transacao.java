package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class Transacao {
    private Integer id_transacao;
    private UsuarioJogo usuarioSolicitante;
    private UsuarioJogo usuarioOfertante;
    private EstadoTransacao estadoTransacao;
    private PreferenciaTransacao preferenciaSolicitante;
    private PreferenciaTransacao preferenciaOfertante;

    public Transacao() {
    }

    public Integer getId_transacao() {
        return id_transacao;
    }

    public void setId_transacao(Integer id_transacao) {
        this.id_transacao = id_transacao;
    }

    public UsuarioJogo getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(UsuarioJogo usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public UsuarioJogo getUsuarioOfertante() {
        return usuarioOfertante;
    }

    public void setUsuarioOfertante(UsuarioJogo usuarioOfertante) {
        this.usuarioOfertante = usuarioOfertante;
    }

    public EstadoTransacao getEstadoTransacao() {
        return estadoTransacao;
    }

    public void setEstadoTransacao(EstadoTransacao estadoTransacao) {
        this.estadoTransacao = estadoTransacao;
    }

    public PreferenciaTransacao getPreferenciaSolicitante() {
        return preferenciaSolicitante;
    }

    public void setPreferenciaSolicitante(PreferenciaTransacao preferenciaSolicitante) {
        this.preferenciaSolicitante = preferenciaSolicitante;
    }

    public PreferenciaTransacao getPreferenciaOfertante() {
        return preferenciaOfertante;
    }

    public void setPreferenciaOfertante(PreferenciaTransacao preferenciaOfertante) {
        this.preferenciaOfertante = preferenciaOfertante;
    }
}
