package br.com.devgeek.cartolaparciais.util;

import br.com.devgeek.cartolaparciais.R;

/**
 * Created by geovannefduarte
 */

public class ClubesUtil {

    public static final int ATLETICO_GO = 373;
    public static final int ATLETICO_MG = 282;
    public static final int ATLETICO_PR = 293;
    public static final int AVAI = 314;
    public static final int BAHIA = 265;
    public static final int BOTAFOGO = 263;
    public static final int CHAPECOENSE = 315;
    public static final int CORINTHIANS = 264;
    public static final int CORITIBA = 294;
    public static final int CRUZEIRO = 283;
    public static final int FLAMENGO = 262;
    public static final int FLUMINENSE = 266;
    public static final int GREMIO = 284;
    public static final int PALMEIRAS = 275;
    public static final int PONTE_PRETA = 303;
    public static final int SANTOS = 277;
    public static final int SAO_PAULO = 276;
    public static final int SPORT = 292;
    public static final int VASCO = 267;
    public static final int VITORIA = 287;

    public static String getClubeNome(int idClube){

        switch (idClube){
            case ATLETICO_GO:	return "Atlético-GO";
            case ATLETICO_MG:	return "Atlético-MG";
            case ATLETICO_PR:	return "Atlético-PR";
            case AVAI:			return "Avaí";
            case BAHIA:			return "Bahia";
            case BOTAFOGO:		return "Botafogo";
            case CHAPECOENSE:	return "Chapecoense";
            case CORINTHIANS:	return "Corinthians";
            case CORITIBA:		return "Coritiba";
            case CRUZEIRO:		return "Cruzeiro";
            case FLAMENGO:		return "Flamengo";
            case FLUMINENSE:	return "Fluminense";
            case GREMIO:		return "Grêmio";
            case PALMEIRAS:		return "Palmeiras";
            case PONTE_PRETA:	return "Ponte Preta";
            case SANTOS:		return "Santos";
            case SAO_PAULO:		return "São Paulo";
            case SPORT:			return "Sport";
            case VASCO:			return "Vasco";
            case VITORIA:		return "Vitória";
            default: return "";
        }
    }

    public static String getClubeAbreviada(int idClube){

        switch (idClube){
            case FLAMENGO:		return "FLA";
            case BOTAFOGO:		return "BOT";
            case CORINTHIANS:	return "COR";
            case BAHIA:			return "BAH";
            case FLUMINENSE:	return "FLU";
            case VASCO:			return "VAS";
            case PALMEIRAS:		return "PAL";
            case SAO_PAULO:		return "SAO";
            case SANTOS:		return "SAN";
            case ATLETICO_MG:	return "ATL";
            case CRUZEIRO:		return "CRU";
            case GREMIO:		return "GRE";
            case VITORIA:		return "VIT";
            case SPORT:			return "SPO";
            case ATLETICO_PR:	return "ATL";
            case CORITIBA:		return "COR";
            case PONTE_PRETA:	return "PON";
            case AVAI:			return "AVA";
            case CHAPECOENSE:	return "CHA";
            case ATLETICO_GO:	return "ATL";
            default: return "";
        }
    }

    public static int getClubeEscudo(int idClube, String tamanho){

        switch (idClube){
            case ATLETICO_GO:
                     if (tamanho.equals("30x30")){ return R.drawable.atletico_go_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.atletico_go_45x45; }
                else {                             return R.drawable.atletico_go_60x60; }
            case ATLETICO_MG:
                     if (tamanho.equals("30x30")){ return R.drawable.atletico_mg_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.atletico_mg_45x45; }
                else {                             return R.drawable.atletico_mg_60x60; }
            case ATLETICO_PR:
                     if (tamanho.equals("30x30")){ return R.drawable.atletico_pr_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.atletico_pr_45x45; }
                else {                             return R.drawable.atletico_pr_60x60; }
            case AVAI:
                     if (tamanho.equals("30x30")){ return R.drawable.avai_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.avai_45x45; }
                else {                             return R.drawable.avai_60x60; }
            case BAHIA:
                     if (tamanho.equals("30x30")){ return R.drawable.bahia_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.bahia_45x45; }
                else {                             return R.drawable.bahia_60x60; }
            case BOTAFOGO:
                     if (tamanho.equals("30x30")){ return R.drawable.botafogo_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.botafogo_45x45; }
                else {                             return R.drawable.botafogo_60x60; }
            case CHAPECOENSE:
                     if (tamanho.equals("30x30")){ return R.drawable.chapecoense_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.chapecoense_45x45; }
                else {                             return R.drawable.chapecoense_60x60; }
            case CORINTHIANS:
                     if (tamanho.equals("30x30")){ return R.drawable.corinthians_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.corinthians_45x45; }
                else {                             return R.drawable.corinthians_60x60; }
            case CORITIBA:
                     if (tamanho.equals("30x30")){ return R.drawable.coritiba_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.coritiba_45x45; }
                else {                             return R.drawable.coritiba_60x60; }
            case CRUZEIRO:
                     if (tamanho.equals("30x30")){ return R.drawable.cruzeiro_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.cruzeiro_45x45; }
                else {                             return R.drawable.cruzeiro_60x60; }
            case FLAMENGO:
                     if (tamanho.equals("30x30")){ return R.drawable.flamengo_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.flamengo_45x45; }
                else {                             return R.drawable.flamengo_60x60; }
            case FLUMINENSE:
                     if (tamanho.equals("30x30")){ return R.drawable.fluminense_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.fluminense_45x45; }
                else {                             return R.drawable.fluminense_60x60; }
            case GREMIO:
                     if (tamanho.equals("30x30")){ return R.drawable.gremio_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.gremio_45x45; }
                else {                             return R.drawable.gremio_60x60; }
            case PALMEIRAS:
                     if (tamanho.equals("30x30")){ return R.drawable.palmeiras_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.palmeiras_45x45; }
                else {                             return R.drawable.palmeiras_60x60; }
            case PONTE_PRETA:
                     if (tamanho.equals("30x30")){ return R.drawable.ponte_preta_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.ponte_preta_45x45; }
                else {                             return R.drawable.ponte_preta_60x60; }
            case SANTOS:
                     if (tamanho.equals("30x30")){ return R.drawable.santos_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.santos_45x45; }
                else {                             return R.drawable.santos_60x60; }
            case SAO_PAULO:
                     if (tamanho.equals("30x30")){ return R.drawable.sao_paulo_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.sao_paulo_45x45; }
                else {                             return R.drawable.sao_paulo_60x60; }
            case SPORT:
                     if (tamanho.equals("30x30")){ return R.drawable.sport_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.sport_45x45; }
                else {                             return R.drawable.sport_60x60; }
            case VASCO:
                     if (tamanho.equals("30x30")){ return R.drawable.vasco_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.vasco_45x45; }
                else {                             return R.drawable.vasco_60x60; }
            case VITORIA:
                     if (tamanho.equals("30x30")){ return R.drawable.vitoria_30x30; }
                else if (tamanho.equals("45x45")){ return R.drawable.vitoria_45x45; }
                else {                             return R.drawable.vitoria_60x60; }
            default: return R.drawable.clube;
        }
    }
}