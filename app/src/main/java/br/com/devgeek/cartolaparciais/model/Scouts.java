package br.com.devgeek.cartolaparciais.model;

import io.realm.RealmObject;

/**
 * Created by geovannefduarte
 */
public class Scouts extends RealmObject {

    private String scout;
    private int quantidade;


    public Scouts(){
    }


    public Scouts(String scout, int quantidade){
        this.scout = scout;
        this.quantidade = quantidade;
    }


    public String getScout(){
        return scout;
    }
    public void setScout(String scout){
        this.scout = scout;
    }
    public int getQuantidade(){
        return quantidade;
    }
    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
}