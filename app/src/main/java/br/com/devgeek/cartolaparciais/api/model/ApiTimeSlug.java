package br.com.devgeek.cartolaparciais.api.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by geovannefduarte
 */

public class ApiTimeSlug implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String URL = "https://api.cartolafc.globo.com/time/slug/";

    private List<ApiTimeSlug_Atleta> atletas;

    private double patrimonio;

    private Double pontos;

    private double valor_time;

    private double rodada_atual;


    public ApiTimeSlug(){
        super();
    }


    public ApiTimeSlug(List<ApiTimeSlug_Atleta> atletas, double patrimonio,
                       Double pontos, double valor_time, double rodada_atual){
        super();
        this.atletas = atletas;
        this.patrimonio = patrimonio;
        this.pontos = pontos;
        this.valor_time = valor_time;
        this.rodada_atual = rodada_atual;
    }


    public static ApiTimeSlug getDataViaAPI(OkHttpClient httpClient, String slug){

        ApiTimeSlug apiTimeSlug = null;

        try {

            Request request = new Request.Builder().url(URL+slug).build();
            Response response = httpClient.newCall(request).execute();
            ApiReturn apiReturn = new ApiReturn(response.code(), response.body().string());

            if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK){

                apiTimeSlug = new Gson().fromJson(apiReturn.getResponse(), new TypeToken<ApiTimeSlug>(){}.getType());

            }

        } catch (IOException e){
            System.out.println("#######################################################");
            System.out.println("##### ApiTimeSlug - getDataViaAPI() | IOException #####");
            System.out.println("#######################################################");
            e.printStackTrace();
        }

        return apiTimeSlug;
    }


    public static String getURL(){
        return URL;
    }
    public static void setURL(String uRL){
        URL = uRL;
    }
    public List<ApiTimeSlug_Atleta> getAtletas(){
        return atletas;
    }
    public void setAtletas(List<ApiTimeSlug_Atleta> atletas){
        this.atletas = atletas;
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