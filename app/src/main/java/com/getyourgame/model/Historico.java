package com.getyourgame.model;

import java.util.Date;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class Historico {
    private Integer id_historico;
    private Date data_hora;
    private String observacao;
    private EstadoTransacao estadoTransacao;

    public Historico() {
    }

    public Integer getId_historico() {
        return id_historico;
    }

    public void setId_historico(Integer id_historico) {
        this.id_historico = id_historico;
    }

    public Date getData_hora() {
        return data_hora;
    }

    public void setData_hora(Date data_hora) {
        this.data_hora = data_hora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public EstadoTransacao getEstadoTransacao() {
        return estadoTransacao;
    }

    public void setEstadoTransacao(EstadoTransacao estadoTransacao) {
        this.estadoTransacao = estadoTransacao;
    }
}
