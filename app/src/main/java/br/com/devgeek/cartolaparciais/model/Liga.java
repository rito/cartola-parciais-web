package br.com.devgeek.cartolaparciais.model;

import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigas_liga;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */
public class Liga extends RealmObject {

    @PrimaryKey
    private long ligaId;

    private Long timeDonoId;

    private String nomeDaLiga;
    private String descricaoDaLiga;
    private String slug;

    private String urlFlamulaPng;

    private Long totalTimesLiga;
    private Long ranking;

    private String tipoLiga;


    public Liga(){
    }


    public Liga(long ligaId, String nomeDaLiga, String descricaoDaLiga, String tipoLiga){
        this.ligaId = ligaId;
        this.nomeDaLiga = nomeDaLiga;
        this.descricaoDaLiga = descricaoDaLiga;
        this.tipoLiga = tipoLiga;
    }


    public Liga(ApiAuthLigas_liga liga, String tipoLiga){
        this.ligaId = liga.getLigaId();
        this.timeDonoId = liga.getTimeDonoId();
        this.nomeDaLiga = liga.getNomeDaLiga();
        this.descricaoDaLiga = liga.getDescricaoDaLiga();
        this.slug = liga.getSlug();

        if (liga.getUrlFlamulaPng() != null)
            this.urlFlamulaPng = liga.getUrlFlamulaPng();
        else if (liga.getUrlTrofeuPng() != null)
            this.urlFlamulaPng = liga.getUrlTrofeuPng();

        this.totalTimesLiga = liga.getTotalTimesLiga();
        this.ranking = liga.getRanking();
        this.tipoLiga = tipoLiga;
    }


    public Liga(long ligaId, Long timeDonoId, String nomeDaLiga, String descricaoDaLiga, String slug, String urlFlamulaPng, Long totalTimesLiga, Long ranking, String tipoLiga){
        this.ligaId = ligaId;
        this.timeDonoId = timeDonoId;
        this.nomeDaLiga = nomeDaLiga;
        this.descricaoDaLiga = descricaoDaLiga;
        this.slug = slug;
        this.urlFlamulaPng = urlFlamulaPng;
        this.totalTimesLiga = totalTimesLiga;
        this.ranking = ranking;
        this.tipoLiga = tipoLiga;
    }


    public static void mergeLigas(Liga master, Liga slave){
        master.setTimeDonoId(slave.getTimeDonoId());
        master.setNomeDaLiga(slave.getNomeDaLiga());
        master.setDescricaoDaLiga(slave.getDescricaoDaLiga());
        master.setSlug(slave.getSlug());
        master.setUrlFlamulaPng(slave.getUrlFlamulaPng());
        master.setTotalTimesLiga(slave.getTotalTimesLiga());
        master.setRanking(slave.getRanking());
        master.setTipoLiga(slave.getTipoLiga());
    }


    public long getLigaId(){
        return ligaId;
    }
    public void setLigaId(long ligaId){
        this.ligaId = ligaId;
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
    public void setUrlFlamulaPng(String urlFlamulaPng){
        this.urlFlamulaPng = urlFlamulaPng;
    }
    public Long getTotalTimesLiga(){
        return totalTimesLiga;
    }
    public void setTotalTimesLiga(Long totalTimesLiga){
        this.totalTimesLiga = totalTimesLiga;
    }
    public Long getRanking(){
        return ranking;
    }
    public void setRanking(Long ranking){
        this.ranking = ranking;
    }
    public String getTipoLiga(){
        return tipoLiga;
    }
    public void setTipoLiga(String tipoLiga){
        this.tipoLiga = tipoLiga;
    }
}