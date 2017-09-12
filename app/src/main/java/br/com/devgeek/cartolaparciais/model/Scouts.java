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


    public static String getLegendaDosScouts(String scout, int quantidade){

        if (scout.equals("G")){

            if (quantidade > 1) return quantidade + " Gols";
            return quantidade + " Gol";

        } else if (scout.equals("A")){

            if (quantidade > 1) return quantidade + " Assistências";
            return quantidade + " Assistência";

        } else if (scout.equals("FT")){

            if (quantidade > 1) return quantidade + " Finalizações na trave";
            return quantidade + " Finalização na trave";

        } else if (scout.equals("FD")){

            if (quantidade > 1) return quantidade + " Finalizações defendidas";
            return quantidade + " Finalização defendida";

        } else if (scout.equals("FF")){

            if (quantidade > 1) return quantidade + " Finalizações pra fora";
            return quantidade + " Finalização pra fora";

        } else if (scout.equals("FS")){

            if (quantidade > 1) return quantidade + " Faltas sofridas";
            return quantidade + " Falta sofrida";

        } else if (scout.equals("PE")){

            if (quantidade > 1) return quantidade + " Passes errados";
            return quantidade + " Passe errado";

        } else if (scout.equals("I")){

            if (quantidade > 1) return quantidade + " Impedimentos";
            return quantidade + " Impedimento";

        } else if (scout.equals("PP")){

            if (quantidade > 1) return quantidade + " Pênaltis perdidos";
            return quantidade + " Pênalti perdido";

        } else if (scout.equals("RB")){

            if (quantidade > 1) return quantidade + " Roubadas de bolas";
            return quantidade + " Roubada de bola";

        } else if (scout.equals("SG")){

            if (quantidade > 1) return quantidade + " Jogos sem sofrer gols";
            return quantidade + " Jogo sem sofrer gols";

        } else if (scout.equals("DD")){

            if (quantidade > 1) return quantidade + " Defesas difíceis";
            return quantidade + " Defesa difícil";

        } else if (scout.equals("DP")){

            if (quantidade > 1) return quantidade + " Defesas de pênaltis";
            return quantidade + " Defesa de pênalti";

        } else if (scout.equals("CV")){

            if (quantidade > 1) return quantidade + " Cartões vermelhos";
            return quantidade + " Cartão vermelho";

        } else if (scout.equals("CA")){

            if (quantidade > 1) return quantidade + " Cartões amarelos";
            return quantidade + " Cartão amarelo";

        } else if (scout.equals("FC")){

            if (quantidade > 1) return quantidade + " Faltas cometidas";
            return quantidade + " Falta cometida";

        } else if (scout.equals("GC")){

            if (quantidade > 1) return quantidade + " Gols contra";
            return quantidade + " Gol contra";

        } else if (scout.equals("GS")){

            if (quantidade > 1) return quantidade + " Gols sofridos";
            return quantidade + " Gols sofrido";

        } else {
            return quantidade + scout;
        }
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