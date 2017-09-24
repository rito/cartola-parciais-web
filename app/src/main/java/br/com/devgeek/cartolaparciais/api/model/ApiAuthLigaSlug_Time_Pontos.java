package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */
public class ApiAuthLigaSlug_Time_Pontos {

    @SerializedName("rodada")     private double rodada;
    @SerializedName("mes")        private double mes;
    @SerializedName("turno")      private double turno;
    @SerializedName("campeonato") private double campeonato;


    public ApiAuthLigaSlug_Time_Pontos(){
    }


    public ApiAuthLigaSlug_Time_Pontos(double rodada, double mes, double turno, double campeonato){
        this.rodada = rodada;
        this.mes = mes;
        this.turno = turno;
        this.campeonato = campeonato;
    }


    public double getRodada(){
        return rodada;
    }
    public void setRodada(double rodada){
        this.rodada = rodada;
    }
    public double getMes(){
        return mes;
    }
    public void setMes(double mes){
        this.mes = mes;
    }
    public double getTurno(){
        return turno;
    }
    public void setTurno(double turno){
        this.turno = turno;
    }
    public double getCampeonato(){
        return campeonato;
    }
    public void setCampeonato(double campeonato){
        this.campeonato = campeonato;
    }
}
