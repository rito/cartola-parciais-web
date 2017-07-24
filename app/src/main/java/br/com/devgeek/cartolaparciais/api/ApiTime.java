package br.com.devgeek.cartolaparciais.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */

public class ApiTime {

    @Expose
    @SerializedName("time_id")
    private Long timeId;

    @Expose
    @SerializedName("nome")
    private String nomeDoTime;

    @Expose
    @SerializedName("nome_cartola")
    private String nomeDoCartoleiro;

    @Expose
    @SerializedName("slug")
    private String slug;

    @Expose
    @SerializedName("facebook_id")
    private Long facebookId;

    @Expose
    @SerializedName("url_escudo_png")
    private String urlEscudoPng;

    @Expose
    @SerializedName("url_escudo_placeholder_png")
    private String urlEscudoPlaceholderPng;

    @Expose
    @SerializedName("foto_perfil")
    private String fotoPerfil;

    @Expose
    @SerializedName("assinante")
    private Boolean assinante;


    public ApiTime(){
    }


    public ApiTime(Long timeId, String nomeDoTime, String nomeDoCartoleiro, String slug, Long facebookId, String urlEscudoPng, String urlEscudoPlaceholderPng, String fotoPerfil, Boolean assinante){
        this.timeId = timeId;
        this.nomeDoTime = nomeDoTime;
        this.nomeDoCartoleiro = nomeDoCartoleiro;
        this.slug = slug;
        this.facebookId = facebookId;
        this.urlEscudoPng = urlEscudoPng;
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
        this.fotoPerfil = fotoPerfil;
        this.assinante = assinante;
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
}