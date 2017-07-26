package br.com.devgeek.cartolaparciais.util;

/**
 * Created by geovannefduarte
 */

public class PosicoesJogadoresUtil {

    public static final int GOLEIRO  = 1;
    public static final int LATERAL  = 2;
    public static final int ZAGUEIRO = 3;
    public static final int MEIA     = 4;
    public static final int ATACANTE = 5;
    public static final int TECNICO  = 6;

    public static String getPosicaoNome(int posicao){

        switch (posicao){
            case GOLEIRO:   return "Goleiro";
            case LATERAL:   return "Lateral";
            case ZAGUEIRO:  return "Zagueiro";
            case MEIA:      return "Meia";
            case ATACANTE:  return "Atacante";
            case TECNICO:   return "Técnico";
            default: return "";
        }
    }

    public static String getPosicaoAbreviada(int posicao){

        switch (posicao){
            case GOLEIRO:   return "gol";
            case LATERAL:   return "lat";
            case ZAGUEIRO:  return "zag";
            case MEIA:      return "mei";
            case ATACANTE:  return "ata";
            case TECNICO:   return "tec";
            default: return "";
        }
    }
}