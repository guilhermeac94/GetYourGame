package com.getyourgame.model;

/**
 * Created by Guilherme on 28/09/2015.
 */
public class PreferenciaTransacao {
    private Integer id_preferencia_transacao;
    private MetodoEnvio metodoEnvio;
    private Number novo_preco;
    private Jogo jogoNovo;

    public PreferenciaTransacao() {
    }

    public Integer getId_preferencia_transacao() {
        return id_preferencia_transacao;
    }

    public void setId_preferencia_transacao(Integer id_preferencia_transacao) {
        this.id_preferencia_transacao = id_preferencia_transacao;
    }

    public MetodoEnvio getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(MetodoEnvio metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public Number getNovo_preco() {
        return novo_preco;
    }

    public void setNovo_preco(Number novo_preco) {
        this.novo_preco = novo_preco;
    }

    public Jogo getJogoNovo() {
        return jogoNovo;
    }

    public void setJogoNovo(Jogo jogoNovo) {
        this.jogoNovo = jogoNovo;
    }
}
