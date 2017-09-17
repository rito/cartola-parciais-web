package br.com.devgeek.cartolaparciais.api.model;

/**
 * Created by geovannefduarte
 */
public class ApiAuthTime {

    private ApiTime time;
    private double patrimonio;
    private Double pontos;
    private double valor_time;
    private double rodada_atual;


    public ApiAuthTime(){
    }


    public ApiAuthTime(ApiTime time, double patrimonio, Double pontos, double valor_time, double rodada_atual){
        this.time = time;
        this.patrimonio = patrimonio;
        this.pontos = pontos;
        this.valor_time = valor_time;
        this.rodada_atual = rodada_atual;
    }


    public ApiTime getTime(){
        return time;
    }
    public void setTime(ApiTime time){
        this.time = time;
    }
    public double getPatrimonio(){
        return patrimonio;
    }
    public void setPatrimonio(double patrimonio){
        this.patrimonio = patrimonio;
    }
    public Double getPontos(){
        return pontos;
    }
    public void setPontos(Double pontos){
        this.pontos = pontos;
    }
    public double getValor_time(){
        return valor_time;
    }
    public void setValor_time(double valor_time){
        this.valor_time = valor_time;
    }
    public double getRodada_atual(){
        return rodada_atual;
    }
    public void setRodada_atual(double rodada_atual){
        this.rodada_atual = rodada_atual;
    }
}