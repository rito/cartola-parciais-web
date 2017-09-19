package br.com.devgeek.cartolaparciais.api.model;

import java.util.List;

/**
 * Created by geovannefduarte on 18/09/17.
 */
public class ApiPartidas {

    private int rodada;
    private List<ApiPartidas_Partida> partidas;


    public ApiPartidas(){
    }


    public ApiPartidas(int rodada, List<ApiPartidas_Partida> partidas){
        this.rodada = rodada;
        this.partidas = partidas;
    }


    public int getRodada(){
        return rodada;
    }
    public void setRodada(int rodada){
        this.rodada = rodada;
    }
    public List<ApiPartidas_Partida> getPartidas(){
        return partidas;
    }
    public void setPartidas(List<ApiPartidas_Partida> partidas){
        this.partidas = partidas;
    }
}