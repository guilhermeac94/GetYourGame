package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class MetodoEnvio {
    private Integer id_metodo_envio;
    private String descricao;

    public MetodoEnvio() {
    }

    public Integer getId_metodo_envio() {
        return id_metodo_envio;
    }

    public void setId_metodo_envio(Integer id_metodo_envio) {
        this.id_metodo_envio = id_metodo_envio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao ;
    }

}
