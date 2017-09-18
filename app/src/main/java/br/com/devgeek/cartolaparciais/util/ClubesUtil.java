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

    public static int getClubeEscudo(int idClube){

        switch (idClube){
            case ATLETICO_GO:   return R.drawable.atletico_go_60x60;
            case ATLETICO_MG:   return R.drawable.atletico_mg_60x60;
            case ATLETICO_PR:   return R.drawable.atletico_pr_60x60;
            case AVAI:          return R.drawable.avai_60x60;
            case BAHIA:         return R.drawable.bahia_60x60;
            case BOTAFOGO:      return R.drawable.botafogo_60x60;
            case CHAPECOENSE:   return R.drawable.chapecoense_60x60;
            case CORINTHIANS:   return R.drawable.corinthians_60x60;
            case CORITIBA:      return R.drawable.coritiba_60x60;
            case CRUZEIRO:      return R.drawable.cruzeiro_60x60;
            case FLAMENGO:      return R.drawable.flamengo_60x60;
            case FLUMINENSE:    return R.drawable.fluminense_60x60;
            case GREMIO:        return R.drawable.gremio_60x60;
            case PALMEIRAS:     return R.drawable.palmeiras_60x60;
            case PONTE_PRETA:   return R.drawable.ponte_preta_60x60;
            case SANTOS:        return R.drawable.santos_60x60;
            case SAO_PAULO:     return R.drawable.sao_paulo_60x60;
            case SPORT:         return R.drawable.sport_60x60;
            case VASCO:         return R.drawable.vasco_60x60;
            case VITORIA:       return R.drawable.vitoria_60x60;
            default: return R.drawable.clube;
        }
    }
}