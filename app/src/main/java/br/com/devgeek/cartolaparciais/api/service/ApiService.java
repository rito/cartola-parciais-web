package br.com.devgeek.cartolaparciais.api.service;

import java.util.List;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigas;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthTime;
import br.com.devgeek.cartolaparciais.api.model.ApiLogin;
import br.com.devgeek.cartolaparciais.api.model.ApiMercadoStatus;
import br.com.devgeek.cartolaparciais.api.model.ApiPartidas;
import br.com.devgeek.cartolaparciais.api.model.ApiTime;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by geovannefduarte
 */
public interface ApiService {

    String APPLICATION_JSON = "application/json; charset=utf-8";

    @GET("partidas")
    Observable<ApiPartidas> buscarPartidas();

    @GET("partidas/{rodada}")
    Observable<ApiPartidas> buscarPartidasDaRodada(@Path("rodada") int rodada);

    @GET("times?")
    Observable<List<ApiTime>> buscarTimes(@Query("q") String nomeTime);

    @GET("mercado/status")
    Observable<ApiMercadoStatus> verificarMercadoStatus();

    @GET("time/id/{idDoTime}")
    Observable<ApiTimeSlug> buscarTimeId(@Path("idDoTime") Long idDoTime);

    @GET("atletas/pontuados")
    Observable<ApiAtletasPontuados> buscarAtletasPontuados();

    @GET("atletas/mercado")
    Observable<ApiAtletasMercado> buscarAtletasMercado();

    @POST("api/authentication")
    Observable<ApiLogin> fazerLoginNaGlobo(@Body RequestBody params);

    @GET("auth/time/info")
    Observable<ApiAuthTime> informacoesDoTimeLogado(@Header("X-GLB-Token") String token);

    @GET("auth/ligas")
    Observable<ApiAuthLigas> buscarLigasDoTimeLogado(@Header("X-GLB-Token") String token);
}