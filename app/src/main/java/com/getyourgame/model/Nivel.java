package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class Nivel {
    private Integer id_nivel;
    private String descricao;

    public Nivel() {
    }

    public Integer getId_nivel() {
        return id_nivel;
    }

    public void setId_nivel(Integer id_nivel) {
        this.id_nivel = id_nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
