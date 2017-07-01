package br.com.devgeek.cartolaparciais.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */

public class ParciaisTimes extends RealmObject {

    public static String FORMATO_PONTUACAO = "#0.00";
    public static String FORMATO_CARTOLETAS = "#0.00";

    @PrimaryKey
    private String slug;
    private String escudo;

    private int posicao;

    private String nomeTime;
    private String nomeCartoleiro;

    private double pontuacao;
    private double variacaoCartoletas;


    public ParciaisTimes(){
        super();
    }


    public ParciaisTimes(String slug, String escudo, int posicao, String nomeTime, String nomeCartoleiro, double variacaoCartoletas, double pontuacao){
        super();
        this.slug = slug;
        this.escudo = escudo;
        this.posicao = posicao;
        this.nomeTime = nomeTime;
        this.nomeCartoleiro = nomeCartoleiro;
        this.variacaoCartoletas = variacaoCartoletas;
        this.pontuacao = pontuacao;
    }


    public int getPosicao(){
        return posicao;
    }
    public void setPosicao(int posicao){
        this.posicao = posicao;
    }
    public String getSlug(){
        return slug;
    }
    public void setSlug(String slug){
        this.slug = slug;
    }
    public String getEscudo(){
        return escudo;
    }
    public void setEscudo(String escudo){
        this.escudo = escudo;
    }
    public String getNomeTime(){
        return nomeTime;
    }
    public void setNomeTime(String nomeTime){
        this.nomeTime = nomeTime;
    }
    public String getNomeCartoleiro(){
        return nomeCartoleiro;
    }
    public void setNomeCartoleiro(String nomeCartoleiro){
        this.nomeCartoleiro = nomeCartoleiro;
    }
    public double getVariacaoCartoletas(){
        return variacaoCartoletas;
    }
    public void setVariacaoCartoletas(double variacaoCartoletas){
        this.variacaoCartoletas = variacaoCartoletas;
    }
    public double getPontuacao(){
        return pontuacao;
    }
    public void setPontuacao(double pontuacao){
        this.pontuacao = pontuacao;
    }
}