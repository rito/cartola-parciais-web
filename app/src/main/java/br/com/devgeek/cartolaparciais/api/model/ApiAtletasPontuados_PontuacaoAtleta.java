package br.com.devgeek.cartolaparciais.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by geovannefduarte
 */

public class ApiAtletasPontuados_PontuacaoAtleta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String apelido;
    private double pontuacao;
    private Map<String, Integer> scout;
    private String foto;
    private Integer posicao_id;
    private Integer clube_id;


    public ApiAtletasPontuados_PontuacaoAtleta() {
        super();
    }


    public ApiAtletasPontuados_PontuacaoAtleta(String apelido,
                                               double pontuacao, Map<String, Integer> scout, String foto,
                                               Integer posicao_id, Integer clube_id) {
        super();
        this.apelido = apelido;
        this.pontuacao = pontuacao;
        this.scout = scout;
        this.foto = foto;
        this.posicao_id = posicao_id;
        this.clube_id = clube_id;
    }


    public String getApelido() {
        return apelido;
    }
    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
    public double getPontuacao() {
        return pontuacao;
    }
    public void setPontuacao(double pontuacao) {
        this.pontuacao = pontuacao;
    }
    public Map<String, Integer> getScout(){
        return scout;
    }
    public void setScout(Map<String, Integer> scout) {
        this.scout = scout;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public Integer getPosicao_id() {
        return posicao_id;
    }
    public void setPosicao_id(Integer posicao_id) {
        this.posicao_id = posicao_id;
    }
    public Integer getClube_id() {
        return clube_id;
    }
    public void setClube_id(Integer clube_id) {
        this.clube_id = clube_id;
    }
}