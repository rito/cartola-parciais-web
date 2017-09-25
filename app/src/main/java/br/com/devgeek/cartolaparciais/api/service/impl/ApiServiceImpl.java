package br.com.devgeek.cartolaparciais.api.service.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado_PontuacaoAtleta;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados_PontuacaoAtleta;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigaSlug;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigaSlug_Time;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigas;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthLigas_liga;
import br.com.devgeek.cartolaparciais.api.model.ApiMercadoStatus;
import br.com.devgeek.cartolaparciais.api.model.ApiPartidas;
import br.com.devgeek.cartolaparciais.api.model.ApiPartidas_Partida;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug_Atleta;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.Liga;
import br.com.devgeek.cartolaparciais.model.MercadoStatus;
import br.com.devgeek.cartolaparciais.model.Partida;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.model.TimeLiga;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToAtualizarMercado;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdateLigas;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdateParciais;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdatePartidas;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.getX_GLB_Token;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ApiServiceImpl {

    private static final String TAG = "ApiServiceImpl";
    // Erro 503 - Mercado em manutencao

    private Retrofit retrofit;
    private ApiService apiService;


    public ApiServiceImpl(){
        retrofit = new Retrofit.Builder()
                               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                               .addConverterFactory(GsonConverterFactory.create())
                               .baseUrl("https://api.cartolafc.globo.com/")
                               .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void atualizarMercado(Context context){

        if (isNetworkAvailable(context)){

            if (isTimeToAtualizarMercado()){
                verificarMercadoStatus();
            }
        }
    }

    public void atualizarPartidas(Context context, boolean checkTime){

        if (isNetworkAvailable(context)){

            if (checkTime){
                if (isTimeToUpdatePartidas()){
                    buscarPartidas(null);
                }
            } else {
                buscarPartidas(null);
            }
        }
    }

    public void atualizarParciais(Context context, boolean checkTime){

        if (isNetworkAvailable(context)){

            if (checkTime){
                if (isTimeToUpdateParciais()){
                    buscarAtletasPontuados();
                }
            } else {
                buscarAtletasPontuados();
            }
        }
    }

    public void atualizarLigas(Context context, boolean checkTime){

        String token = getX_GLB_Token();
        if (isNetworkAvailable(context)){

            if (token != null){
                if (checkTime){
                    if (isTimeToUpdateLigas()){
                        buscarLigasDoTimeLogado(token);
                    }
                } else {
                    buscarLigasDoTimeLogado(token);
                }
            }
        }
    }

    private void verificarMercadoStatus(){

        try {

            Observable<ApiMercadoStatus> verificarMercadoStatus = apiService.verificarMercadoStatus();

            verificarMercadoStatus.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiMercadoStatus();
                        }
                        logErrorOnConsole(TAG, "verificarMercadoStatus.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(apiMercadoStatus -> {

                        Realm realm = null;

                        if( apiMercadoStatus != null ){

                            try {

                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(realmTransaction -> {

                                    MercadoStatus mercadoStatus = new MercadoStatus(apiMercadoStatus);
                                    MercadoStatus mercadoStatusOnRealm = realmTransaction.where(MercadoStatus.class).findFirst();

                                    if (mercadoStatusOnRealm == null || !mercadoStatusOnRealm.equals(mercadoStatus)){

                                        if (mercadoStatusOnRealm != null) mercadoStatusOnRealm.deleteFromRealm();
                                        realmTransaction.copyToRealm(mercadoStatus);
                                    }
                                });

                            } catch (Exception e){

                                logErrorOnConsole(TAG, e.getMessage(), e);

                            } finally {
                                if (realm != null) realm.close();
                            }
                        }
                    }, error -> {
                        try {
                            if (error instanceof NullPointerException){
                                logErrorOnConsole(TAG, "ApiMercadoStatus [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else if (error instanceof HttpException){ // We had non-200 http error
                                logErrorOnConsole(TAG, "ApiMercadoStatus [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else if (error instanceof IOException){ // A network error happened
                                logErrorOnConsole(TAG, "ApiMercadoStatus [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else {
                                logErrorOnConsole(TAG, "ApiMercadoStatus -> " + error.getMessage() + " / " + error.getClass(), error);
                            }
                        } catch (Exception e){
                            logErrorOnConsole(TAG, "ApiMercadoStatus -> " + error.getMessage() + " / " + error.getClass(), error);
                        }
                    });
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao verificarMercadoStatus() -> "+e.getMessage(), e);
        }
    }

    private void buscarAtletasPontuados(){

        try {

            Observable<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();

            buscarAtletasPontuados.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAtletasPontuados();
                        }
                        if (!throwable.getMessage().toString().equals("Null is not a valid element")){
                            logErrorOnConsole(TAG, "buscarAtletasPontuados.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        } return null;
                    })
                    .subscribe(
                            apiAtletasPontuados -> {

                                if (apiAtletasPontuados.getAtletas() != null && apiAtletasPontuados.getAtletas().size() > 0){

                                    AsyncTask.execute(() -> {

                                        Realm realm = null;

                                        try {

                                            realm = Realm.getDefaultInstance();
                                            final RealmResults<AtletasPontuados> atletasPontuados = realm.where(AtletasPontuados.class).isNotNull( "rodada" ).findAll();
                                            if (atletasPontuados.size() > 0)  realm.executeTransaction(realmTransaction -> atletasPontuados.deleteAllFromRealm() );

                                            RealmList<AtletasPontuados> listaAtletasPontuados = new RealmList<>();

                                            for (Map.Entry<String, ApiAtletasPontuados_PontuacaoAtleta> entry : apiAtletasPontuados.getAtletas().entrySet()){

                                                String atletaId = entry.getKey();
                                                ApiAtletasPontuados_PontuacaoAtleta atleta = entry.getValue();
                                                listaAtletasPontuados.add(new AtletasPontuados(atletaId, apiAtletasPontuados.getRodada(), atleta));
                                            }

                                            if (listaAtletasPontuados.size() > 0){

                                                realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(listaAtletasPontuados));
                                            }
                                        } catch (Exception e){

                                            logErrorOnConsole(TAG, e.getMessage(), e);

                                        } finally {
                                            if (realm != null) realm.close();
                                        }
                                    });

                                    atualizarParciaisTimesFavoritos(apiAtletasPontuados);
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        buscarAtletasMercado();
                                        atualizarParciaisTimesFavoritos(null);
                                        logErrorOnConsole(TAG, "ApiAtletasPontuados [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiAtletasPontuados [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiAtletasPontuados [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiAtletasPontuados -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiAtletasPontuados -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarAtletasPontuados() -> "+e.getMessage(), e);
            atualizarParciaisTimesFavoritos(null);
        }
    }

    private void atualizarParciaisTimesFavoritos(ApiAtletasPontuados atletasPontuadosEncontrados){

        Realm realm = null;
        List<TimeFavorito> timesFavoritos = null;

        try {

            realm = Realm.getDefaultInstance();

            timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

        } catch (Exception e){

            logErrorOnConsole(TAG, e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        if (timesFavoritos != null && timesFavoritos.size() > 0){

            for (TimeFavorito timeFavorito : timesFavoritos){

                atualizarParciaisDeCadaTimeFavorito(atletasPontuadosEncontrados, timeFavorito);
            }
        }
    }

    private void atualizarParciaisDeCadaTimeFavorito(ApiAtletasPontuados atletasPontuadosEncontrados, TimeFavorito timeFavorito){

        try {

            Observable<ApiTimeSlug> buscarTimeId = apiService.buscarTimeId(timeFavorito.getTimeId());

            buscarTimeId.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiTimeSlug();
                        }
                        logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(
                            timeSlug -> {

                                if (timeSlug != null && timeSlug.getAtletas() != null && timeSlug.getAtletas().size() > 0){

                                    Realm realm = null;
                                    double pontuacao = 0.0, variacaoCartoletas = 0.0;

                                    try {

                                        List<AtletasPontuados> atletas = new ArrayList<>();

                                        for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                            if (atletasPontuadosEncontrados != null){

                                                if (atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())) != null){

                                                    pontuacao += atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())).getPontuacao();
                                                    atletas.add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id()))));

                                                } else {

                                                    atletas.add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atleta.getApelido(), null, null, atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                                }

                                            } else {

                                                pontuacao += atleta.getPontos_num();
                                                variacaoCartoletas += atleta.getVariacao_num();
                                                atletas.add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atleta.getApelido(), atleta.getPontos_num(), atleta.getVariacao_num(), atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                            }
                                        }

                                        timeFavorito.setAtletas(new Gson().toJson(atletas));
                                        timeFavorito.setPontuacao(pontuacao);
                                        timeFavorito.setVariacaoCartoletas(variacaoCartoletas);

                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }

                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        logErrorOnConsole(TAG, "ApiTimeSlug [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiTimeSlug [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiTimeSlug [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiTimeSlug -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiTimeSlug -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            }
                    );

        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao atualizarParciaisDeCadaTimeFavorito() -> "+e.getMessage(), e);
        }
    }

    private void buscarAtletasMercado(){

        try {

            Observable<ApiAtletasMercado> buscarAtletasMercado = apiService.buscarAtletasMercado();

            buscarAtletasMercado.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAtletasMercado();
                        }
                        logErrorOnConsole(TAG, "buscarAtletasMercado.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(apiAtletasMercado -> {

                        if (apiAtletasMercado != null && apiAtletasMercado.getAtletas() != null && apiAtletasMercado.getAtletas().size() > 0){

                            Realm realm = null;

                            if( apiAtletasMercado != null && apiAtletasMercado.getAtletas().size() > 0 ){

                                try {

                                    realm = Realm.getDefaultInstance();

                                    int rodada = apiAtletasMercado.getAtletas().get(0).getRodada_id();
                                    RealmList<AtletasPontuados> listaAtletasPontuados = new RealmList<>();

                                    for (ApiAtletasMercado_PontuacaoAtleta atleta : apiAtletasMercado.getAtletas()){
                                        listaAtletasPontuados.add(new AtletasPontuados(rodada, atleta));
                                    }

                                    if (listaAtletasPontuados.size() > 0){

                                        final RealmResults<AtletasPontuados> atletasPontuados = realm.where(AtletasPontuados.class).isNotNull( "rodada" ).findAll();

                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(realmTransaction -> {
                                            atletasPontuados.deleteAllFromRealm();
                                            realmTransaction.copyToRealmOrUpdate(listaAtletasPontuados);
                                        });
                                    }

                                } catch (Exception e){

                                    logErrorOnConsole(TAG, e.getMessage(), e);

                                } finally {
                                    if (realm != null) realm.close();
                                }
                            }
                        }
                    }, error -> {
                        try {
                            if (error instanceof NullPointerException){
                                logErrorOnConsole(TAG, "ApiAtletasMercado_PontuacaoAtleta [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else if (error instanceof HttpException){ // We had non-200 http error
                                logErrorOnConsole(TAG, "ApiAtletasMercado_PontuacaoAtleta [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else if (error instanceof IOException){ // A network error happened
                                logErrorOnConsole(TAG, "ApiAtletasMercado_PontuacaoAtleta [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                            } else {
                                logErrorOnConsole(TAG, "ApiAtletasMercado_PontuacaoAtleta -> " + error.getMessage() + " / " + error.getClass(), error);
                            }
                        } catch (Exception e){
                            logErrorOnConsole(TAG, "ApiAtletasMercado_PontuacaoAtleta -> " + error.getMessage() + " / " + error.getClass(), error);
                        }
                    });
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarAtletasMercado() -> "+e.getMessage(), e);
        }
    }

    private void buscarLigasDoTimeLogado(String token){

        try {

            Observable<ApiAuthLigas> buscarLigasDoTimeLogado = apiService.buscarLigasDoTimeLogado(token);

            buscarLigasDoTimeLogado.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAuthLigas();
                        }
                        logErrorOnConsole(TAG, "buscarLigasDoTimeLogado.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            ligasDaApi -> {

                                if (ligasDaApi != null && ligasDaApi.getLigas() != null && ligasDaApi.getLigas().size() > 0){

                                    Realm realm = null;

                                    try {

                                        realm = Realm.getDefaultInstance();
                                        final RealmResults<Liga> ligasOnRealm = realm.where(Liga.class).findAll();
                                        if (ligasOnRealm != null && ligasOnRealm.size() > 0)  realm.executeTransaction(realmTransaction -> ligasOnRealm.deleteAllFromRealm() );

                                        RealmList<Liga> ligasDoCartoleiro = new RealmList<>();
                                        boolean hasEditorial = false, hasMataMata = false, hasMinhasLigas = false;

                                        if (ligasDaApi != null && ligasDaApi.getLigas() != null && ligasDaApi.getLigas().size() > 0){

                                            for (ApiAuthLigas_liga liga : ligasDaApi.getLigas()){

                                                String tipoLiga = "";
                                                if (liga.getClubeId() != null || liga.isEditorial() || liga.getTotalTimesLiga() == 0){
                                                    hasEditorial = true; tipoLiga = "Editorial";
                                                } else if (liga.isMata_mata()){
                                                    hasMataMata = true; tipoLiga = "Mata-Mata";
                                                } else {
                                                    hasMinhasLigas = true; tipoLiga = "Minhas ligas";
                                                }

                                                ligasDoCartoleiro.add(new Liga(liga, tipoLiga));
                                            }
                                        }

                                        if (hasEditorial)   ligasDoCartoleiro.add(new Liga(-999, "", "", "Editorial"));
                                        if (hasMataMata)    ligasDoCartoleiro.add(new Liga(-555, "", "", "Mata-Mata"));
                                        if (hasMinhasLigas) ligasDoCartoleiro.add(new Liga(-111, "", "", "Minhas ligas"));

                                        if (ligasDoCartoleiro.size() > 0){

                                            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(ligasDoCartoleiro));
                                        }

                                        atualizarTimesDasLigas(token);

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        buscarAtletasMercado();
                                        atualizarParciaisTimesFavoritos(null);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiAuthLigas [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiAuthLigas [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiAuthLigas -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiAuthLigas -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarLigasDoTimeLogado() -> "+e.getMessage(), e);
            atualizarParciaisTimesFavoritos(null);
        }
    }

    private void atualizarTimesDasLigas(String token){

        Realm realm = null;
        List<Liga> ligas = null;

        try {

            realm = Realm.getDefaultInstance();
            ligas = realm.copyFromRealm(realm.where(Liga.class).isNotNull("slug").findAll());

        } catch (Exception e){

            logErrorOnConsole(TAG, e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        if (ligas != null && ligas.size() > 0){

            for (Liga liga : ligas){

                buscarTimesDaLiga(liga, token);
            }
        }
    }

    private void buscarTimesDaLiga(Liga liga, String token){

        try {

            Observable<ApiAuthLigaSlug> buscarTimesDaLiga = apiService.buscarTimesDaLiga(token, liga.getSlug());

            buscarTimesDaLiga.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAuthLigaSlug();
                        }
                        logErrorOnConsole(TAG, "buscarTimesDaLiga.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            times -> {

                                if (times != null && times.getTimes() != null && times.getTimes().size() > 0){

                                    Realm realm = null;

                                    try {

                                        realm = Realm.getDefaultInstance();
                                        Map<String, String> chaveDeTimesDaApi = new HashMap<>();

                                        RealmList<TimeLiga> timesDaLiga = new RealmList<>();
                                        TimeFavorito timeFavorito = realm.where(TimeFavorito.class).equalTo("timeDoUsuario", true).findFirst();

                                        for (ApiAuthLigaSlug_Time time : times.getTimes()){

                                            boolean timeDoUsuario = false;
                                            if (timeFavorito != null && timeFavorito.getTimeId().equals(time.getTimeId())) timeDoUsuario = true;

                                            timesDaLiga.add(new TimeLiga(times.getLiga().getLigaId(), time, null, timeDoUsuario));
                                            chaveDeTimesDaApi.put(String.valueOf(times.getLiga().getLigaId()+time.getTimeId()), time.getSlug());
                                        }

                                        final RealmResults<TimeLiga> timesDaLigaOnRealm = realm.where(TimeLiga.class).equalTo("ligaId", liga.getLigaId()).findAll();
                                        if (timesDaLigaOnRealm != null && timesDaLigaOnRealm.size() > 0){

                                            for (TimeLiga timeDaLiga : timesDaLigaOnRealm){
                                                if (chaveDeTimesDaApi.get(timeDaLiga.getId()) == null)
                                                    realm.executeTransaction(realmTransaction -> timeDaLiga.deleteFromRealm() );
                                            }
                                        }

                                        if (timesDaLiga.size() > 0){

                                            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timesDaLiga));
                                        }

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        buscarAtletasMercado();
                                        atualizarParciaisTimesFavoritos(null);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiAuthLigaSlug [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiAuthLigaSlug [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiAuthLigaSlug -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiAuthLigaSlug -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });

        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarTimesDaLiga() -> "+e.getMessage(), e);
            atualizarParciaisTimesFavoritos(null);
        }
    }

    private void buscarPartidas(Integer buscarRodada){

        try {

            Observable<ApiPartidas> buscarPartidas;

            if (buscarRodada == null){

                buscarPartidas = apiService.buscarPartidas();

            } else {

                buscarPartidas = apiService.buscarPartidasDaRodada(buscarRodada);
            }

            buscarPartidas.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiPartidas();
                        }
                        logErrorOnConsole(TAG, "buscarPartidas.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            partidas -> {

                                if (partidas != null && partidas.getPartidas() != null && partidas.getPartidas().size() > 0){

                                    Realm realm = null;

                                    try {

                                        realm = Realm.getDefaultInstance();
                                        RealmList<Partida> listaPartidas = new RealmList<>();

                                        if (partidas != null){

                                            final int rodada = partidas.getRodada();
                                            listaPartidas.add(new Partida((-rodada), rodada, rodada+"ª rodada"));

                                            AsyncTask.execute(() -> verificarRodadasAnteriores(rodada-1) );

                                            if (partidas != null && partidas.getPartidas() != null && partidas.getPartidas().size() > 0){

                                                for (ApiPartidas_Partida partida : partidas.getPartidas()){

                                                    listaPartidas.add(new Partida(rodada, null, partida));
                                                }
                                            }
                                        }

                                        if (listaPartidas.size() > 0){

                                            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(listaPartidas));
                                        }

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        buscarAtletasMercado();
                                        atualizarParciaisTimesFavoritos(null);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiPartidas [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiPartidas [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiPartidas -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiPartidas -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarPartidas() -> "+e.getMessage(), e);
            atualizarParciaisTimesFavoritos(null);
        }
    }

    private void verificarRodadasAnteriores(int rodada){

        if (rodada > 0){

            Realm realm = null;

            try {

                realm = Realm.getDefaultInstance();
                final RealmResults<Partida> partidas = realm.where(Partida.class).equalTo("rodada", rodada).findAll();

                if (partidas == null || partidas.size() == 0){

                    buscarPartidas(rodada);

                } else {

                    verificarRodadasAnteriores(rodada - 1);
                }

            } catch (Exception e){

                logErrorOnConsole(TAG, e.getMessage(), e);

            } finally {
                if (realm != null) realm.close();
            }
        }
    }
}