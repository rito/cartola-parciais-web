package br.com.devgeek.cartolaparciais.api.service;

import java.util.List;

import br.com.devgeek.cartolaparciais.api.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.ApiMercadoStatus;
import br.com.devgeek.cartolaparciais.api.ApiTime;
import br.com.devgeek.cartolaparciais.api.ApiTimeSlug;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by geovannefduarte
 */
public interface ApiService {

    @GET("times?")
    Observable<List<ApiTime>> buscarTimes(@Query("q") String nomeTime);

    @GET("mercado/status")
    Observable<ApiMercadoStatus> verificarMercadoStatus();

    @GET("time/id/{idDoTime}")
    Observable<ApiTimeSlug> buscarTimeId(@Path("idDoTime") Long idDoTime);

    @GET("time/slug/{slugDoTime}")
    Observable<ApiTimeSlug> buscarTimeSlug(@Path("slugDoTime") String slugDoTime);

    @GET("atletas/pontuados")
    Observable<ApiAtletasPontuados> buscarAtletasPontuados();

//    @GET("atletas/pontuados")
//    Call<ApiAtletasPontuados> buscarAtletasPontuados();
}