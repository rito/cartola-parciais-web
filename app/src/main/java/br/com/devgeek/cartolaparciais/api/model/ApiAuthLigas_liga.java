package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */
public class ApiAuthLigas_liga {

    @SerializedName("liga_id")
    private long ligaId;

    @SerializedName("clube_id")
    private Long clubeId;

    @SerializedName("time_dono_id")
    private Long timeDonoId;

    @SerializedName("nome")
    private String nomeDaLiga;

    @SerializedName("descricao")
    private String descricaoDaLiga;

    @SerializedName("slug")
    private String slug;

    @SerializedName("url_flamula_png")
    private String urlFlamulaPng;

    @SerializedName("url_trofeu_png")
    private String urlTrofeuPng;

    @SerializedName("total_times_liga")
    private long totalTimesLiga;

    @SerializedName("camp_ranking_num")
    private long ranking;

    @SerializedName("editorial")
    private boolean editorial;

    @SerializedName("patrocinador")
    private boolean patrocinador;

    @SerializedName("mata_mata")
    private boolean mata_mata;


    public ApiAuthLigas_liga(){
    }


    public ApiAuthLigas_liga(long ligaId, Long clubeId, Long timeDonoId, String nomeDaLiga, String descricaoDaLiga, String slug, String urlFlamulaPng, String urlTrofeuPng, long totalTimesLiga, long ranking, boolean editorial, boolean patrocinador, boolean mata_mata){
        this.ligaId = ligaId;
        this.clubeId = clubeId;
        this.timeDonoId = timeDonoId;
        this.nomeDaLiga = nomeDaLiga;
        this.descricaoDaLiga = descricaoDaLiga;
        this.slug = slug;
        this.urlFlamulaPng = urlFlamulaPng;
        this.urlTrofeuPng = urlTrofeuPng;
        this.totalTimesLiga = totalTimesLiga;
        this.ranking = ranking;
        this.editorial = editorial;
        this.patrocinador = patrocinador;
        this.mata_mata = mata_mata;
    }


    public long getLigaId(){
        return ligaId;
    }
    public void setLigaId(long ligaId){
        this.ligaId = ligaId;
    }
    public Long getClubeId(){
        return clubeId;
    }
    public void setClubeId(Long clubeId){
        this.clubeId = clubeId;
    }
    public Long getTimeDonoId(){
        return timeDonoId;
    }
    public void setTimeDonoId(Long timeDonoId){
        this.timeDonoId = timeDonoId;
    }
    public String getNomeDaLiga(){
        return nomeDaLiga;
    }
    public void setNomeDaLiga(String nomeDaLiga){
        this.nomeDaLiga = nomeDaLiga;
    }
    public String getDescricaoDaLiga(){
        return descricaoDaLiga;
    }
    public void setDescricaoDaLiga(String descricaoDaLiga){
        this.descricaoDaLiga = descricaoDaLiga;
    }
    public String getSlug(){
        return slug;
    }
    public void setSlug(String slug){
        this.slug = slug;
    }
    public String getUrlFlamulaPng(){
        return urlFlamulaPng;
    }
    public void setUrlFlamulaPng(String urlFlamulaSvg){
        this.urlFlamulaPng = urlFlamulaSvg;
    }
    public String getUrlTrofeuPng(){
        return urlTrofeuPng;
    }
    public void setUrlTrofeuPng(String urlTrofeuPng){
        this.urlTrofeuPng = urlTrofeuPng;
    }
    public long getTotalTimesLiga(){
        return totalTimesLiga;
    }
    public void setTotalTimesLiga(long totalTimesLiga){
        this.totalTimesLiga = totalTimesLiga;
    }
    public long getRanking(){
        return ranking;
    }
    public void setRanking(long ranking){
        this.ranking = ranking;
    }
    public boolean isEditorial(){
        return editorial;
    }
    public void setEditorial(boolean editorial){
        this.editorial = editorial;
    }
    public boolean isPatrocinador(){
        return patrocinador;
    }
    public void setPatrocinador(boolean patrocinador){
        this.patrocinador = patrocinador;
    }
    public boolean isMata_mata(){
        return mata_mata;
    }
    public void setMata_mata(boolean mata_mata){
        this.mata_mata = mata_mata;
    }
}