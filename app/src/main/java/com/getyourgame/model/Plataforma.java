package com.getyourgame.model;

import java.util.List;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class Plataforma {
    private Integer id_plataforma;
    private String descricao;
    private String marca;

    public Plataforma() {
    }

    public Integer getId_plataforma() {
        return id_plataforma;
    }

    public void setId_plataforma(Integer id_plataforma) {
        this.id_plataforma = id_plataforma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
