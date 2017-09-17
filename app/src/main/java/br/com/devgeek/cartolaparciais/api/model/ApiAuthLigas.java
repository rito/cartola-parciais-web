package br.com.devgeek.cartolaparciais.api.model;

import java.util.List;

/**
 * Created by geovannefduarte
 */
public class ApiAuthLigas {

    private List<ApiAuthLigas_liga> ligas;


    public ApiAuthLigas(){
    }


    public ApiAuthLigas(List<ApiAuthLigas_liga> ligas){
        this.ligas = ligas;
    }


    public List<ApiAuthLigas_liga> getLigas(){
        return ligas;
    }
    public void setLigas(List<ApiAuthLigas_liga> ligas){
        this.ligas = ligas;
    }
}