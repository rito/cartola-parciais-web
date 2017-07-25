package br.com.devgeek.cartolaparciais.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by geovannefduarte
 */
public class ApiMercadoStatusFechamento {

    @Expose
    @SerializedName("dia")
    private int dia;

    @Expose
    @SerializedName("mes")
    private int mes;

    @Expose
    @SerializedName("ano")
    private int ano;

    @Expose
    @SerializedName("hora")
    private int hora;

    @Expose
    @SerializedName("minuto")
    private int minuto;

    @Expose
    @SerializedName("timestamp")
    private long timestamp;


    public ApiMercadoStatusFechamento(){
    }


    public ApiMercadoStatusFechamento(int dia, int mes, int ano, int hora, int minuto, long timestamp){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.timestamp = timestamp;
    }


    public int getDia(){
        return dia;
    }
    public void setDia(int dia){
        this.dia = dia;
    }
    public int getMes(){
        return mes;
    }
    public void setMes(int mes){
        this.mes = mes;
    }
    public int getAno(){
        return ano;
    }
    public void setAno(int ano){
        this.ano = ano;
    }
    public int getHora(){
        return hora;
    }
    public void setHora(int hora){
        this.hora = hora;
    }
    public int getMinuto(){
        return minuto;
    }
    public void setMinuto(int minuto){
        this.minuto = minuto;
    }
    public long getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }
}