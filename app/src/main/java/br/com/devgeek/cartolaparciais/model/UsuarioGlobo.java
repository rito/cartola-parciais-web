package br.com.devgeek.cartolaparciais.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by geovannefduarte
 */
public class UsuarioGlobo extends RealmObject {

    @PrimaryKey
    private String glbId;


    public UsuarioGlobo(){
    }


    public UsuarioGlobo(String glbId){
        this.glbId = glbId;
    }


    public String getGlbId(){
        return glbId;
    }
    public void setGlbId(String glbId){
        this.glbId = glbId;
    }
}