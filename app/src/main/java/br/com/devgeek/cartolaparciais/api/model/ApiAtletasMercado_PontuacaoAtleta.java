package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by geovannefduarte on 12/09/17.
 */
public class ApiAtletasMercado_PontuacaoAtleta implements Serializable {

    private String apelido;

    @SerializedName("pontos_num")
    private double pontuacao;

    @SerializedName("variacao_num")
    private Double cartoletas;

    private Map<String, Integer> scout;

    private String foto;

    private Integer atleta_id;
    private Integer rodada_id;
    private Integer posicao_id;
    private Integer clube_id;


    public ApiAtletasMercado_PontuacaoAtleta(){
    }


    public ApiAtletasMercado_PontuacaoAtleta(String apelido, double pontuacao, Double cartoletas, Map<String, Integer> scout, String foto, Integer atleta_id, Integer rodada_id, Integer posicao_id, Integer clube_id){
        this.apelido = apelido;
        this.pontuacao = pontuacao;
        this.cartoletas = cartoletas;
        this.scout = scout;
        this.foto = foto;
        this.atleta_id = atleta_id;
        this.rodada_id = rodada_id;
        this.posicao_id = posicao_id;
        this.clube_id = clube_id;
    }


    public String getApelido(){
        return apelido;
    }
    public void setApelido(String apelido){
        this.apelido = apelido;
    }
    public double getPontuacao(){
        return pontuacao;
    }
    public void setPontuacao(double pontuacao){
        this.pontuacao = pontuacao;
    }
    public Double getCartoletas(){
        return cartoletas;
    }
    public void setCartoletas(Double cartoletas){
        this.cartoletas = cartoletas;
    }
    public Map<String, Integer> getScout(){
        return scout;
    }
    public void setScout(Map<String, Integer> scout){
        this.scout = scout;
    }
    public String getFoto(){
        return foto;
    }
    public void setFoto(String foto){
        this.foto = foto;
    }
    public Integer getAtleta_id(){
        return atleta_id;
    }
    public void setAtleta_id(Integer atleta_id){
        this.atleta_id = atleta_id;
    }
    public Integer getRodada_id(){
        return rodada_id;
    }
    public void setRodada_id(Integer rodada_id){
        this.rodada_id = rodada_id;
    }
    public Integer getPosicao_id(){
        return posicao_id;
    }
    public void setPosicao_id(Integer posicao_id){
        this.posicao_id = posicao_id;
    }
    public Integer getClube_id(){
        return clube_id;
    }
    public void setClube_id(Integer clube_id){
        this.clube_id = clube_id;
    }
}