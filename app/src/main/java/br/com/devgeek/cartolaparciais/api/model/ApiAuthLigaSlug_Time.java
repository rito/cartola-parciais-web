package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */
public class ApiAuthLigaSlug_Time {

    @SerializedName("time_id")                    private Long timeId;
    @SerializedName("nome")                       private String nomeDoTime;
    @SerializedName("nome_cartola")               private String nomeDoCartoleiro;
    @SerializedName("slug")                       private String slug;
    @SerializedName("facebook_id")                private Long facebookId;
    @SerializedName("url_escudo_png")             private String urlEscudoPng;
    @SerializedName("url_escudo_placeholder_png") private String urlEscudoPlaceholderPng;
    @SerializedName("foto_perfil")                private String fotoPerfil;
    @SerializedName("assinante")                  private Boolean assinante;
    @SerializedName("patrimonio")                 private double patrimonio;
    @SerializedName("pontos")                     private ApiAuthLigaSlug_Time_Pontos pontos;


    public ApiAuthLigaSlug_Time(){
    }


    public ApiAuthLigaSlug_Time(Long timeId, String nomeDoTime, String nomeDoCartoleiro, String slug, Long facebookId, String urlEscudoPng, String urlEscudoPlaceholderPng, String fotoPerfil, Boolean assinante, double patrimonio, ApiAuthLigaSlug_Time_Pontos pontos){
        this.timeId = timeId;
        this.nomeDoTime = nomeDoTime;
        this.nomeDoCartoleiro = nomeDoCartoleiro;
        this.slug = slug;
        this.facebookId = facebookId;
        this.urlEscudoPng = urlEscudoPng;
        this.urlEscudoPlaceholderPng = urlEscudoPlaceholderPng;
        this.fotoPerfil = fotoPerfil;
        this.assinante = assinante;
        this.patrimonio = patrimonio;
        this.pontos = pontos;
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
    public double getPatrimonio(){
        return patrimonio;
    }
    public void setPatrimonio(double patrimonio){
        this.patrimonio = patrimonio;
    }
    public ApiAuthLigaSlug_Time_Pontos getPontos(){
        return pontos;
    }
    public void setPontos(ApiAuthLigaSlug_Time_Pontos pontos){
        this.pontos = pontos;
    }
}