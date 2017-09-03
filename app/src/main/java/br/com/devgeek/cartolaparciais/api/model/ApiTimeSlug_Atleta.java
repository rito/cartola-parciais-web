package br.com.devgeek.cartolaparciais.api.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by geovannefduarte
 */
public class ApiTimeSlug_Atleta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String apelido;
    private String foto;

    private Integer clube_id;
    private Integer atleta_id;
    private Integer posicao_id;

    private double pontos_num;
    private double preco_num;
    private double variacao_num;

    private HashMap<String, Integer> scout;


    public ApiTimeSlug_Atleta(){
        super();
    }


    public ApiTimeSlug_Atleta(String nome, String apelido, String foto, Integer clube_id,
                              Integer atleta_id, Integer posicao_id, double pontos_num,
                              double preco_num, double variacao_num, HashMap<String, Integer> scout){
        super();
        this.nome = nome;
        this.apelido = apelido;
        this.foto = foto;
        this.clube_id = clube_id;
        this.atleta_id = atleta_id;
        this.posicao_id = posicao_id;
        this.pontos_num = pontos_num;
        this.preco_num = preco_num;
        this.variacao_num = variacao_num;
        this.scout = scout;
    }


    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getApelido(){
        return apelido;
    }
    public void setApelido(String apelido){
        this.apelido = apelido;
    }
    public String getFoto(){
        return foto;
    }
    public void setFoto(String foto){
        this.foto = foto;
    }
    public Integer getClube_id(){
        return clube_id;
    }
    public void setClube_id(Integer clube_id){
        this.clube_id = clube_id;
    }
    public Integer getAtleta_id(){
        return atleta_id;
    }
    public void setAtleta_id(Integer atleta_id){
        this.atleta_id = atleta_id;
    }
    public Integer getPosicao_id(){
        return posicao_id;
    }
    public void setPosicao_id(Integer posicao_id){
        this.posicao_id = posicao_id;
    }
    public double getPontos_num(){
        return pontos_num;
    }
    public void setPontos_num(double pontos_num){
        this.pontos_num = pontos_num;
    }
    public double getPreco_num(){
        return preco_num;
    }
    public void setPreco_num(double preco_num){
        this.preco_num = preco_num;
    }
    public double getVariacao_num(){
        return variacao_num;
    }
    public void setVariacao_num(double variacao_num){
        this.variacao_num = variacao_num;
    }
    public HashMap<String, Integer> getScout(){
        return scout;
    }
    public void setScout(HashMap<String, Integer> scout){
        this.scout = scout;
    }
}