package br.com.devgeek.cartolaparciais.api.model;

import java.util.List;

/**
 * Created by geovannefduarte
 */
public class ApiAuthLigaSlug {

    private ApiAuthLigas_liga liga;
    private List<ApiAuthLigaSlug_Time> times;


    public ApiAuthLigaSlug(){
    }


    public ApiAuthLigaSlug(ApiAuthLigas_liga liga, List<ApiAuthLigaSlug_Time> times){
        this.liga = liga;
        this.times = times;
    }


    public ApiAuthLigas_liga getLiga(){
        return liga;
    }
    public void setLiga(ApiAuthLigas_liga liga){
        this.liga = liga;
    }
    public List<ApiAuthLigaSlug_Time> getTimes(){
        return times;
    }
    public void setTimes(List<ApiAuthLigaSlug_Time> times){
        this.times = times;
    }
}