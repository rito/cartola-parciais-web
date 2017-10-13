package br.com.devgeek.cartolaparciais.api.service;

import java.util.List;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigaSlug;
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
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by geovannefduarte
 */
public interface ApiService {

    String APPLICATION_JSON = "application/json; charset=utf-8";

    @GET("partidas")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiPartidas> buscarPartidas();

    @GET("partidas/{rodada}")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiPartidas> buscarPartidasDaRodada(@Path("rodada") int rodada);

    @GET("times?")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<List<ApiTime>> buscarTimes(@Query("q") String nomeTime);

    @GET("mercado/status")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiMercadoStatus> verificarMercadoStatus();

    @GET("time/id/{idDoTime}")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiTimeSlug> buscarTimeId(@Path("idDoTime") Long idDoTime);

    @GET("atletas/pontuados")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiAtletasPontuados> buscarAtletasPontuados();

    @GET("atletas/mercado")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiAtletasMercado> buscarAtletasMercado();

    @POST("api/authentication")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiLogin> fazerLoginNaGlobo(@Body RequestBody params);

    @GET("auth/time/info")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiAuthTime> informacoesDoTimeLogado(@Header("X-GLB-Token") String token);

    @GET("auth/ligas")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiAuthLigas> buscarLigasDoTimeLogado(@Header("X-GLB-Token") String token);

    @GET("auth/liga/{slug}")
    @Headers({"User-Agent: cartolafcoficial/1.6.0", "Accept: (iPhone; iOS 11.0.1; Scale/2.00)"})
    Observable<ApiAuthLigaSlug> buscarTimesDaLiga(@Header("X-GLB-Token") String token, @Path("slug") String slugDaLiga);
}