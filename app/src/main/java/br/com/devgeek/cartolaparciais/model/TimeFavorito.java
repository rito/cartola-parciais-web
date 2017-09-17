package br.com.devgeek.cartolaparciais.model;

import br.com.devgeek.cartolaparciais.api.model.ApiTime;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */

public class TimeFavorito extends RealmObject {

    public static String FORMATO_PONTUACAO = "#0.00";
    public static String FORMATO_CARTOLETAS = "#0.00";

    @PrimaryKey
    private Long timeId;

    private String nomeDoTime;
    private String nomeDoCartoleiro;
    private String slug;
    private Long facebookId;
    private String urlEscudoPng;
    private String urlEscudoPlaceholderPng;
    private String fotoPerfil;
    private Boolean assinante;
    private boolean timeFavorito;
    private boolean timeDoUsuario;

    private Double pontuacao;
    private Double variacaoCartoletas;

    private RealmList<AtletasPontuados> atletas;

    public TimeFavorito(){
    }


    public TimeFavorito(ApiTime time, boolean timeFavorito, boolean timeDoUsuario){
        this.timeId = time.getTimeId();
        this.nomeDoTime = time.getNomeDoTime();
        this.nomeDoCartoleiro = time.getNomeDoCartoleiro();
        this.slug = time.getSlug();
        this.facebookId = time.getFacebookId();
        this.urlEscudoPng = time.getUrlEscudoPng();
        this.urlEscudoPlaceholderPng = time.getUrlEscudoPlaceholderPng();
        this.fotoPerfil = time.getFotoPerfil();
        this.assinante = time.getAssinante();
        this.timeFavorito = timeFavorito;
        this.timeDoUsuario = timeDoUsuario;
    }


    public TimeFavorito(Long timeId, String nomeDoTime, String nomeDoCartoleiro, String slug, Long facebookId, String urlEscudoPng, String urlEscudoPlaceholderPng, String fotoPerfil, Boolean assinante, boolean timeFavorito, boolean timeDoUsuario){
        this.timeId = timeId;
        this.nomeDoTime = nomeDoTime;
        this.nomeDoCartoleiro = nomeDoCartoleiro;
        this.slug = slug;
        this.facebookId = facebookId;
        this.urlEscudoPng = urlEscudoPng;
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
        this.fotoPerfil = fotoPerfil;
        this.assinante = assinante;
        this.timeFavorito = timeFavorito;
        this.timeDoUsuario = timeDoUsuario;
    }


    public Long getTimeId(){
        return timeId;
    }
    public void setTimeId(Long timeId){
        this.timeId = timeId;
    }
    public String getNomeDoTime(){
        return nomeDoTime;
    }
    public void setNomeDoTime(String nomeDoTime){
        this.nomeDoTime = nomeDoTime;
    }
    public String getNomeDoCartoleiro(){
        return nomeDoCartoleiro;
    }
    public void setNomeDoCartoleiro(String nomeDoCartoleiro){
        this.nomeDoCartoleiro = nomeDoCartoleiro;
    }
    public String getSlug(){
        return slug;
    }
    public void setSlug(String slug){
        this.slug = slug;
    }
    public Long getFacebookId(){
        return facebookId;
    }
    public void setFacebookId(Long facebookId){
        this.facebookId = facebookId;
    }
    public String getUrlEscudoPng(){
        return urlEscudoPng;
    }
    public void setUrlEscudoPng(String urlEscudoPng){
        this.urlEscudoPng = urlEscudoPng;
    }
    public String getUrlEscudoPlaceholderPng(){
        return urlEscudoPlaceholderPng;
    }
    public void setUrlEscudoPlaceholderPng(String urlEscudoPlaceholderPng){
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
    }
    public String getFotoPerfil(){
        return fotoPerfil;
    }
    public void setFotoPerfil(String fotoPerfil){
        this.fotoPerfil = fotoPerfil;
    }
    public Boolean getAssinante(){
        return assinante;
    }
    public void setAssinante(Boolean assinante){
        this.assinante = assinante;
    }
    public boolean isTimeFavorito(){
        return timeFavorito;
    }
    public void setTimeFavorito(boolean timeFavorito){
        this.timeFavorito = timeFavorito;
    }
    public boolean isTimeDoUsuario(){
        return timeDoUsuario;
    }
    public void setTimeDoUsuario(boolean timeDoUsuario){
        this.timeDoUsuario = timeDoUsuario;
    }
    public Double getPontuacao(){
        return pontuacao;
    }
    public void setPontuacao(Double pontuacao){
        this.pontuacao = pontuacao;
    }
    public Double getVariacaoCartoletas(){
        return variacaoCartoletas;
    }
    public void setVariacaoCartoletas(Double variacaoCartoletas){
        this.variacaoCartoletas = variacaoCartoletas;
    }
    public RealmList<AtletasPontuados> getAtletas(){
        return atletas;
    }
    public void setAtletas(RealmList<AtletasPontuados> atletas){
        this.atletas = atletas;
    }
}