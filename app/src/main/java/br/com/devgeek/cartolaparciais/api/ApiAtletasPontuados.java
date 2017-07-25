package br.com.devgeek.cartolaparciais.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by geovannefduarte
 */

public class ApiAtletasPontuados implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String URL = "https://api.cartolafc.globo.com/atletas/pontuados"; // http://programmers.stackexchange.com/a/203493

    private int rodada;

    private Map<String, ApiAtletasPontuados_PontuacaoAtleta> atletas;


    public ApiAtletasPontuados() {
        super();
    }


    public ApiAtletasPontuados(Integer rodada,
                               Map<String, ApiAtletasPontuados_PontuacaoAtleta> atletas) {
        super();
        this.rodada = rodada;
        this.atletas = atletas;
    }


    public int getRodada(){
        return rodada;
    }
    public void setRodada(int rodada){
        this.rodada = rodada;
    }
    public Map<String, ApiAtletasPontuados_PontuacaoAtleta> getAtletas(){
        return atletas;
    }
    public void setAtletas(Map<String, ApiAtletasPontuados_PontuacaoAtleta> atletas){
        this.atletas = atletas;
    }


    public static ApiAtletasPontuados getDataViaAPI(OkHttpClient httpClient){

        ApiAtletasPontuados atletasPontuados = null;

        try {

            Request request = new Request.Builder().url(URL).build();
            Response response = httpClient.newCall(request).execute();
            ApiReturn apiReturn = new ApiReturn(response.code(), response.body().string());

            if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND || apiReturn.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT){

                atletasPontuados = null;

            } else if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK){

                atletasPontuados = new Gson().fromJson(apiReturn.getResponse(), new TypeToken<ApiAtletasPontuados>(){}.getType());

            } else {
                System.out.println(URL);
                System.out.println(apiReturn.getStatusCode()+" -> "+apiReturn.getResponse());
            }

        } catch (IOException e){
            System.out.println("###############################################################");
            System.out.println("##### ApiAtletasPontuados - getDataViaAPI() | IOException #####");
            System.out.println("###############################################################");
            e.printStackTrace();
        }

        return atletasPontuados;
    }
}