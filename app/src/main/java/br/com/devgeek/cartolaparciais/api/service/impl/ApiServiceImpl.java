package br.com.devgeek.cartolaparciais.api.service.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import io.realm.Sort;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToAtualizarMercado;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdateLigas;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdateParciais;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToUpdatePartidas;
import static br.com.devgeek.cartolaparciais.model.AtletasPontuados.mergeAtletasPontuados;
import static br.com.devgeek.cartolaparciais.model.Liga.mergeLigas;
import static br.com.devgeek.cartolaparciais.model.MercadoStatus.mergeMercadoStatus;
import static br.com.devgeek.cartolaparciais.model.Partida.mergePartidas;
import static br.com.devgeek.cartolaparciais.model.TimeLiga.mergeTimesDaLiga;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.getX_GLB_Token;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ApiServiceImpl {

    private static final String TAG = "ApiServiceImpl";
    // Erro 503 - Mercado em manutenção

    private Gson gson;
    private Retrofit retrofit;
    private ApiService apiService;


    public ApiServiceImpl(){
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.cartolafc.globo.com/")
                .build();

        apiService = retrofit.create(ApiService.class);
        gson = new Gson();
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

    public void atualizarParciais(Context context, boolean checkTime, Long ligaId){

        if (isNetworkAvailable(context)){

            if (checkTime){
                if (isTimeToUpdateParciais()){
                    if (ligaId == null){
                        buscarAtletasPontuados(true, false, null);
                    } else {
                        buscarAtletasPontuados(false, true, ligaId);
                    }
                }
            } else {
                if (ligaId == null){
                    buscarAtletasPontuados(true, false, null);
                } else {
                    buscarAtletasPontuados(false, true, ligaId);
                }
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
                        if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiMercadoStatus();
                        }
                        logErrorOnConsole(TAG, "verificarMercadoStatus.onErrorReturn() -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(apiMercadoStatus -> {

                        Realm realm = null;

                        if( apiMercadoStatus != null ){

                            try {

                                realm = Realm.getDefaultInstance();

                                MercadoStatus mercadoStatus = new MercadoStatus(apiMercadoStatus);
                                final MercadoStatus mercadoStatusOnRealm = realm.where(MercadoStatus.class).equalTo("temporada", mercadoStatus.getTemporada()).findFirst();

                                if (mercadoStatusOnRealm != null){

                                    realm.executeTransaction(realmTransaction -> {

                                        if (mergeMercadoStatus(mercadoStatusOnRealm, mercadoStatus)){
                                            // dd..;;
                                        }

                                        buscarAtletasPontuados(true, false, null);
                                    });

                                } else {
                                    realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(mercadoStatus));
                                }
                            } catch (Exception e){

                                logErrorOnConsole(TAG, "verificarMercadoStatus.subscribe() -> "+e.getMessage(), e);

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

    private void buscarAtletasPontuados(boolean atualizarParciaisTimesFavoritos, boolean atualizarParciaisTimesDaLigas, Long ligaId){

        try {

            Observable<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();

            buscarAtletasPontuados.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAtletasPontuados();
                        }

                        if (throwable.getMessage().toString().equals("Null is not a valid element")){
                            buscarAtletasMercado();
                            return new ApiAtletasPontuados();
                        }

                        logErrorOnConsole(TAG, "buscarAtletasPontuados.onErrorReturn() -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            apiAtletasPontuados -> {

                                if (apiAtletasPontuados.getAtletas() != null && apiAtletasPontuados.getAtletas().size() > 0){

                                    AsyncTask.execute(() -> {

                                        Realm realm = null;

                                        try {

                                            realm = Realm.getDefaultInstance();
                                            realm.executeTransaction(realmTransaction -> {

                                                int rodada = apiAtletasPontuados.getRodada();
                                                RealmList<AtletasPontuados> listaAtletasPontuados = new RealmList<>();
                                                Map<String, AtletasPontuados> chaveDeAtletasPontuados = new HashMap<>();

                                                for (Map.Entry<String, ApiAtletasPontuados_PontuacaoAtleta> entry : apiAtletasPontuados.getAtletas().entrySet()){

                                                    String atletaId = entry.getKey();
                                                    ApiAtletasPontuados_PontuacaoAtleta atleta = entry.getValue();

                                                    AtletasPontuados atletasPontuados = new AtletasPontuados(atletaId, rodada, atleta);

                                                    listaAtletasPontuados.add(atletasPontuados);
                                                    chaveDeAtletasPontuados.put(String.valueOf(rodada+atletasPontuados.getAtletaId()), atletasPontuados);
                                                }

                                                //final RealmResults<AtletasPontuados> atletasPontuados = realmTransaction.where(AtletasPontuados.class).isNotNull( "rodada" ).findAll();
                                                final RealmResults<AtletasPontuados> atletasPontuadosOnRealm = realmTransaction.where(AtletasPontuados.class).findAll();
                                                if (atletasPontuadosOnRealm != null && atletasPontuadosOnRealm.size() > 0){

                                                    for (AtletasPontuados atletaPontuado : atletasPontuadosOnRealm){

                                                        if (chaveDeAtletasPontuados.get(String.valueOf(rodada+atletaPontuado.getAtletaId())) == null){

                                                            atletaPontuado.deleteFromRealm();

                                                        } else {

                                                            mergeAtletasPontuados(realmTransaction, atletaPontuado, chaveDeAtletasPontuados.get(String.valueOf(rodada+atletaPontuado.getAtletaId())));
                                                            chaveDeAtletasPontuados.remove(String.valueOf(rodada+atletaPontuado.getAtletaId()));
                                                        }
                                                    }

                                                    if (chaveDeAtletasPontuados.size() > 0){

                                                        for (Map.Entry<String, AtletasPontuados> entry : chaveDeAtletasPontuados.entrySet()){

                                                            AtletasPontuados atleta = entry.getValue();
                                                            realmTransaction.copyToRealmOrUpdate(atleta);
                                                        }
                                                    }
                                                } else if (listaAtletasPontuados.size() > 0){

                                                    realmTransaction.copyToRealmOrUpdate(listaAtletasPontuados);
                                                }

                                            });
                                        } catch (Exception e){

                                            logErrorOnConsole(TAG, "buscarAtletasPontuados.subscribe() -> "+e.getMessage(), e);

                                        } finally {
                                            if (realm != null) realm.close();
                                        }
                                    });

                                    if (atualizarParciaisTimesFavoritos) atualizarParciaisTimesFavoritos(apiAtletasPontuados);
                                    if (atualizarParciaisTimesDaLigas) atualizarParciaisTimesDaLigas(apiAtletasPontuados, ligaId);

                                } else {

                                    if (atualizarParciaisTimesFavoritos) atualizarParciaisTimesFavoritos(null);
                                    if (atualizarParciaisTimesDaLigas) atualizarParciaisTimesDaLigas(null, ligaId);
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
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
            if (atualizarParciaisTimesFavoritos) atualizarParciaisTimesFavoritos(null);
            if (atualizarParciaisTimesDaLigas) atualizarParciaisTimesDaLigas(null, ligaId);
        }
    }

    private void atualizarParciaisTimesFavoritos(ApiAtletasPontuados atletasPontuadosEncontrados){

        Realm realm = null;
        MercadoStatus mercadoStatus = null;
        List<TimeFavorito> timesFavoritos = null;
        Map<String, TimeFavorito> mapDeTimesFavoritos = null;

        try {

            realm = Realm.getDefaultInstance();

            timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());
            mercadoStatus = realm.copyFromRealm(realm.where(MercadoStatus.class).findFirst());

        } catch (Exception e){

            logErrorOnConsole(TAG, "atualizarParciaisTimesFavoritos() -> "+e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        if (timesFavoritos != null && timesFavoritos.size() > 0){

            mapDeTimesFavoritos = new HashMap<>();
            for (TimeFavorito timeFavorito : timesFavoritos){

                mapDeTimesFavoritos.put(String.valueOf(timeFavorito.getTimeId()), timeFavorito);
            }

            atualizarParciaisDeCadaTimeFavorito(atletasPontuadosEncontrados, mapDeTimesFavoritos, mercadoStatus);
        }
    }

    private void atualizarParciaisDeCadaTimeFavorito(ApiAtletasPontuados atletasPontuadosEncontrados, Map<String, TimeFavorito> mapDeTimesFavoritos, MercadoStatus mercadoStatus){

        try {

            if (mapDeTimesFavoritos.size() > 0){

                boolean fazerBuscaNaApi = true;
                TimeFavorito timeFavorito = mapDeTimesFavoritos.entrySet().iterator().next().getValue();

//                if (timeFavorito.getAtletasIds() != null && (
//                   (mercadoStatus.getStatusDoMercado() == MercadoStatus.ABERTO && mercadoStatus.getRodadaAtual() == timeFavorito.getAtletasIds_rodada()+1) ||
//                   (mercadoStatus.getStatusDoMercado() == MercadoStatus.FECHADO && mercadoStatus.getRodadaAtual() == timeFavorito.getAtletasIds_rodada()))){
//
//                    fazerBuscaNaApi = false;
//                    if (atletasPontuadosEncontrados != null){
//
//                        Realm realm = null;
//
//                        try {
//
//                            realm = Realm.getDefaultInstance();
//                            double pontuacao = 0.0, variacaoCartoletas = 0.0;
//                            List<AtletasPontuados> atletas = parseAndSortAtletasPontuados(gson, timeFavorito.getAtletas());
//
//                            List<String> atletasIds = Arrays.asList(timeFavorito.getAtletasIds().split("=#="));
//
//                            for (String atletaId : atletasIds){
//
//                                if (atletasPontuadosEncontrados.getAtletas().get(atletaId) != null){
//
//                                    pontuacao += atletasPontuadosEncontrados.getAtletas().get(atletaId).getPontuacao();
//                                    atletas.add(new AtletasPontuados(atletaId, null, atletasPontuadosEncontrados.getAtletas().get(atletaId)));
//                                }
//                            }
//
//                            timeFavorito.setAtletas(gson.toJson(atletas));
//                            timeFavorito.setPontuacao(pontuacao);
//                            timeFavorito.setVariacaoCartoletas(variacaoCartoletas);
//
//                            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));
//
//                            mapDeTimesFavoritos.remove(String.valueOf(timeFavorito.getTimeId()));
//                            atualizarParciaisDeCadaTimeFavorito(atletasPontuadosEncontrados, mapDeTimesFavoritos, mercadoStatus);
//
//                        } catch (Exception e) {
//
//                            logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.atletasPontuadosEncontrados != null -> " + e.getMessage(), e);
//
//                        } finally {
//                            if (realm != null) realm.close();
//                        }
//                    } else if (timeFavorito.getVariacaoCartoletas() == null || timeFavorito.getVariacaoCartoletas() == 0){
//                        fazerBuscaNaApi = true;
//                    }
//                }

                if (fazerBuscaNaApi){

                    Log.w(TAG, "atualizarParciaisDeCadaTimeFavorito() -> "+timeFavorito.getSlug());
                    Observable<ApiTimeSlug> buscarTimeId = apiService.buscarTimeId(timeFavorito.getTimeId());

                    buscarTimeId.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorReturn((Throwable throwable) -> {
                                if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                                    return new ApiTimeSlug();
                                }
                                logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.onErrorReturn() -> "+throwable.getMessage(), throwable);
                                return null; //empty object of the datatype
                            })
                            .subscribe(
                                    timeSlug -> AsyncTask.execute(() -> {

                                        if (timeSlug != null && timeSlug.getAtletas() != null && timeSlug.getAtletas().size() > 0){

                                            new Thread(() -> {

                                                Realm realm = null;

                                                try {

                                                    realm = Realm.getDefaultInstance();
                                                    double pontuacao = 0.0, variacaoCartoletas = 0.0;
                                                    List<AtletasPontuados> atletas = new ArrayList<>();
                                                    String idsDosAtletas = "";

                                                    for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                                        if (!idsDosAtletas.equals("")){ idsDosAtletas += "=#="; }
                                                        idsDosAtletas += String.valueOf(atleta.getAtleta_id());

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

                                                    timeFavorito.setAtletasIds_rodada(timeSlug.getRodada_atual());
                                                    timeFavorito.setAtletasIds(idsDosAtletas);
                                                    timeFavorito.setAtletas(gson.toJson(atletas));
                                                    timeFavorito.setPontuacao(pontuacao);
                                                    timeFavorito.setVariacaoCartoletas(variacaoCartoletas);

                                                    realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));

                                                    mapDeTimesFavoritos.remove(String.valueOf(timeFavorito.getTimeId()));
                                                    atualizarParciaisDeCadaTimeFavorito(atletasPontuadosEncontrados, mapDeTimesFavoritos, mercadoStatus);

                                                } catch (Exception e){

                                                    logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.subscribe() -> "+e.getMessage(), e);

                                                } finally {
                                                    if (realm != null) realm.close();
                                                }
                                            }).start();
                                        }
                                    }),
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
                }
            }
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao atualizarParciaisDeCadaTimeFavorito() -> "+e.getMessage(), e);
        }
    }

    private void atualizarParciaisTimesDaLigas(ApiAtletasPontuados atletasPontuadosEncontrados, Long ligaId){

        Realm realm = null;
        MercadoStatus mercadoStatus = null;
        Map<String, TimeLiga> mapDeTimesDaLiga = null;

        try {

            realm = Realm.getDefaultInstance();

            List<Liga> ligas;
            Sort[] ligasSortOrder = { Sort.DESCENDING, Sort.ASCENDING, Sort.ASCENDING };
            String[] ligasSortColumns = { "tipoLiga", "nomeDaLiga", "descricaoDaLiga" };
            if (ligaId == null){
                ligas = realm.copyFromRealm(realm.where(Liga.class).findAllSorted(ligasSortColumns, ligasSortOrder));
            } else {
                ligas = realm.copyFromRealm(realm.where(Liga.class).equalTo("ligaId", ligaId).findAllSorted(ligasSortColumns, ligasSortOrder));
            }

            for (Liga liga : ligas){

                if (liga.getLigaId() > 0 && liga.getTipoLiga().equals("Minhas ligas")){

                    Log.w(TAG, "atualizarTimesDaLiga() -> "+liga.getSlug());
                    if (mapDeTimesDaLiga == null) mapDeTimesDaLiga = new HashMap<>();

                    Sort[] timesSortOrder = { Sort.DESCENDING, Sort.DESCENDING, Sort.ASCENDING };
                    String[] timesSortColumns = { "pontuacao", "pontuacaoRodada", "nomeDoTime" };
                    List<TimeLiga> timesDaLigas = realm.copyFromRealm(realm.where(TimeLiga.class).equalTo("ligaId", liga.getLigaId()).findAllSorted(timesSortColumns, timesSortOrder));

                    if (timesDaLigas != null && timesDaLigas.size() > 0){

                        for (TimeLiga timeDaLiga : timesDaLigas){

                            mapDeTimesDaLiga.put(String.valueOf(timeDaLiga.getId()), timeDaLiga);
                        }
                    }
                }
            }

            if (mapDeTimesDaLiga != null && mapDeTimesDaLiga.size() > 0){

                atualizarParciaisDeCadaTimeDaLiga(atletasPontuadosEncontrados, mapDeTimesDaLiga, mercadoStatus);
            }

        } catch (Exception e){

            logErrorOnConsole(TAG, "atualizarParciaisTimesDaLigas() -> "+e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }
    }

    private void atualizarParciaisDeCadaTimeDaLiga(ApiAtletasPontuados atletasPontuadosEncontrados, Map<String, TimeLiga> mapDeTimesDaLiga, MercadoStatus mercadoStatus){

        try {

            if (mapDeTimesDaLiga.size() > 0){

                TimeLiga timeDaLiga = mapDeTimesDaLiga.entrySet().iterator().next().getValue();
                Log.w(TAG, "atualizarParciaisDeCadaTimeDaLiga() -> "+timeDaLiga.getSlug());

                Observable<ApiTimeSlug> buscarTimeId = apiService.buscarTimeId(timeDaLiga.getTimeId());

                buscarTimeId.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn((Throwable throwable) -> {
                            if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                                return new ApiTimeSlug();
                            }
                            logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.onErrorReturn() -> "+throwable.getMessage(), throwable);
                            return null; //empty object of the datatype
                        })
                        .subscribe(
                                timeSlug -> AsyncTask.execute(() -> {

                                    if (timeSlug != null && timeSlug.getAtletas() != null && timeSlug.getAtletas().size() > 0){

                                        new Thread(() -> {

                                            Realm realm = null;

                                            try {

                                                realm = Realm.getDefaultInstance();
                                                double pontuacao = 0.0, variacaoCartoletas = 0.0;
                                                List<AtletasPontuados> atletas = new ArrayList<>();
                                                String idsDosAtletas = "";

                                                for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                                    if (!idsDosAtletas.equals("")){ idsDosAtletas += "=#="; }
                                                    idsDosAtletas += String.valueOf(atleta.getAtleta_id());

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

                                                timeDaLiga.setAtletasIds_rodada(timeSlug.getRodada_atual());
                                                timeDaLiga.setAtletasIds(idsDosAtletas);
                                                timeDaLiga.setAtletas(gson.toJson(atletas));
                                                timeDaLiga.setPontuacao(pontuacao);
                                                timeDaLiga.setVariacaoCartoletas(variacaoCartoletas);

                                                realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeDaLiga) );

                                                mapDeTimesDaLiga.remove(String.valueOf(timeDaLiga.getId()));
                                                atualizarParciaisDeCadaTimeDaLiga(atletasPontuadosEncontrados, mapDeTimesDaLiga, mercadoStatus);

                                            } catch (Exception e){

                                                logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeDaLiga()[subscribe] -> "+e.getMessage(), e);

                                            } finally {
                                                if (realm != null) realm.close();
                                            }
                                        }).start();
                                    }

                                }),
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
            }

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
                        if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAtletasMercado();
                        }
                        logErrorOnConsole(TAG, "buscarAtletasMercado.onErrorReturn() -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(apiAtletasMercado -> {

                        if (apiAtletasMercado != null && apiAtletasMercado.getAtletas() != null && apiAtletasMercado.getAtletas().size() > 0){

                            Realm realm = null;

                            try {

                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(realmTransaction -> {

                                    int rodada = apiAtletasMercado.getAtletas().get(0).getRodada_id();
                                    RealmList<AtletasPontuados> listaAtletasPontuados = new RealmList<>();
                                    Map<String, AtletasPontuados> chaveDeAtletasPontuados = new HashMap<>();

                                    for (ApiAtletasMercado_PontuacaoAtleta atleta : apiAtletasMercado.getAtletas()){

                                        AtletasPontuados atletasPontuados = new AtletasPontuados(rodada, atleta);

                                        listaAtletasPontuados.add(atletasPontuados);
                                        chaveDeAtletasPontuados.put(String.valueOf(rodada+atletasPontuados.getAtletaId()), atletasPontuados);
                                    }

                                    //final RealmResults<AtletasPontuados> atletasPontuadosOnRealm = realmTransaction.where(AtletasPontuados.class).isNotNull( "rodada" ).findAll();
                                    final RealmResults<AtletasPontuados> atletasPontuadosOnRealm = realmTransaction.where(AtletasPontuados.class).findAll();
                                    if (atletasPontuadosOnRealm != null && atletasPontuadosOnRealm.size() > 0){

                                        for (AtletasPontuados atletaPontuado : atletasPontuadosOnRealm){

                                            if (chaveDeAtletasPontuados.get(String.valueOf(rodada+atletaPontuado.getAtletaId())) == null){

                                                atletaPontuado.deleteFromRealm();

                                            } else {

                                                mergeAtletasPontuados(realmTransaction, atletaPontuado, chaveDeAtletasPontuados.get(String.valueOf(rodada+atletaPontuado.getAtletaId())));
                                                chaveDeAtletasPontuados.remove(String.valueOf(rodada+atletaPontuado.getAtletaId()));
                                            }
                                        }

                                        if (chaveDeAtletasPontuados.size() > 0){

                                            for (Map.Entry<String, AtletasPontuados> entry : chaveDeAtletasPontuados.entrySet()){

                                                realmTransaction.copyToRealmOrUpdate(entry.getValue());
                                            }
                                        }
                                    } else if (listaAtletasPontuados.size() > 0){

                                        realmTransaction.copyToRealmOrUpdate(listaAtletasPontuados);
                                    }
                                });

                            } catch (Exception e){

                                logErrorOnConsole(TAG, "buscarAtletasMercado.subscribe() -> "+e.getMessage(), e);

                            } finally {
                                if (realm != null) realm.close();
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
                        if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiAuthLigas();
                        }
                        logErrorOnConsole(TAG, "buscarLigasDoTimeLogado.onErrorReturn() -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            ligasDaApi -> {

                                if (ligasDaApi != null && ligasDaApi.getLigas() != null && ligasDaApi.getLigas().size() > 0){

                                    Realm realm = null;

                                    try {

                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(realmTransaction -> {

                                            RealmList<Liga> ligasDoCartoleiro = new RealmList<>();
                                            Map<String, Liga> chaveDeLigasDoCartoleiro = new HashMap<>();
                                            boolean hasEditorial = false, hasMataMata = false, hasMinhasLigas = false;

                                            for (ApiAuthLigas_liga liga : ligasDaApi.getLigas()){

                                                String tipoLiga = "";

                                                if (liga.getSlug().equals("nacional")    ||
                                                        liga.getSlug().equals("liga-do-ge")  ||
                                                        liga.getSlug().equals("patrimonio")  ||
                                                        liga.getSlug().equals("cartola-pro") ||
                                                        liga.getSlug().equals("o-melhor-time-e-o-seu") ||
                                                        liga.getClubeId() != null || liga.isEditorial() || liga.getTotalTimesLiga() == 0){
                                                    hasEditorial = true; tipoLiga = "Editorial";
                                                } else if (liga.isMata_mata()){
                                                    hasMataMata = true; tipoLiga = "Mata-Mata";
                                                } else {
                                                    hasMinhasLigas = true; tipoLiga = "Minhas ligas";
                                                }

                                                Liga ligaDoCartoleiro = new Liga(liga, tipoLiga);

                                                ligasDoCartoleiro.add(ligaDoCartoleiro);
                                                chaveDeLigasDoCartoleiro.put(String.valueOf(liga.getLigaId()), ligaDoCartoleiro);
                                            }

                                            if (hasEditorial){
                                                Liga ligaDoCartoleiro = new Liga(-999, "", "", "Editorial");
                                                ligasDoCartoleiro.add(ligaDoCartoleiro);
                                                chaveDeLigasDoCartoleiro.put(String.valueOf(ligaDoCartoleiro.getLigaId()), ligaDoCartoleiro);
                                            }

                                            if (hasMataMata){
                                                Liga ligaDoCartoleiro = new Liga(-555, "", "", "Mata-Mata");
                                                ligasDoCartoleiro.add(ligaDoCartoleiro);
                                                chaveDeLigasDoCartoleiro.put(String.valueOf(ligaDoCartoleiro.getLigaId()), ligaDoCartoleiro);
                                            }

                                            if (hasMinhasLigas){
                                                Liga ligaDoCartoleiro = new Liga(-111, "", "", "Minhas ligas");
                                                ligasDoCartoleiro.add(ligaDoCartoleiro);
                                                chaveDeLigasDoCartoleiro.put(String.valueOf(ligaDoCartoleiro.getLigaId()), ligaDoCartoleiro);
                                            }

                                            final RealmResults<Liga> ligasDoCartoleiroOnRealm = realmTransaction.where(Liga.class).findAll();
                                            if (ligasDoCartoleiroOnRealm != null && ligasDoCartoleiroOnRealm.size() > 0){

                                                for (Liga liga : ligasDoCartoleiroOnRealm){

                                                    if (chaveDeLigasDoCartoleiro.get(String.valueOf(liga.getLigaId())) == null){

                                                        if (!(liga.getLigaId() < 0 && ((hasEditorial && liga.getLigaId() == -999) || (hasMataMata && liga.getLigaId() == -555) || (hasMinhasLigas && liga.getLigaId() == -111)))){
                                                            final RealmResults<TimeLiga> listaTimesDaLiga = realmTransaction.where(TimeLiga.class).equalTo("ligaId", liga.getLigaId()).findAll();
                                                            listaTimesDaLiga.deleteAllFromRealm();
                                                            liga.deleteFromRealm();
                                                        }

                                                    } else {

                                                        mergeLigas(liga, chaveDeLigasDoCartoleiro.get(String.valueOf(liga.getLigaId())));
                                                        chaveDeLigasDoCartoleiro.remove(String.valueOf(liga.getLigaId()));
                                                    }
                                                }

                                                if (chaveDeLigasDoCartoleiro.size() > 0){

                                                    for (Map.Entry<String, Liga> entry : chaveDeLigasDoCartoleiro.entrySet()){

                                                        realmTransaction.copyToRealmOrUpdate(entry.getValue());
                                                    }
                                                }
                                            } else if (ligasDoCartoleiro.size() > 0){

                                                realmTransaction.copyToRealmOrUpdate(ligasDoCartoleiro);
                                            }

                                            atualizarTimesDasLigas(token);
                                        });

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, "buscarLigasDoTimeLogado.subscribe() -> "+e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        logErrorOnConsole(TAG, "ApiAuthLigas [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
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
        }
    }

    private void atualizarTimesDasLigas(String token){

        Realm realm = null;
        List<Liga> ligas = null;

        try {

            realm = Realm.getDefaultInstance();
            ligas = realm.copyFromRealm(realm.where(Liga.class).isNotNull("slug").findAll());

        } catch (Exception e){

            logErrorOnConsole(TAG, "atualizarTimesDasLigas() -> "+e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        if (ligas != null && ligas.size() > 0){

            Map<String, Liga> mapDeLigas = new HashMap<>();
            for (Liga liga : ligas){

                mapDeLigas.put(liga.getSlug(), liga);
            }

            buscarTimesDaLiga(mapDeLigas, token);
        }
    }

    private void buscarTimesDaLiga(Map<String, Liga> mapDeLigas, String token){

        try {

            if (mapDeLigas.size() > 0){

                Liga liga = mapDeLigas.entrySet().iterator().next().getValue();
                Log.w(TAG, "buscarTimesDaLiga(liga) -> "+liga.getSlug());

                Observable<ApiAuthLigaSlug> buscarTimesDaLiga = apiService.buscarTimesDaLiga(token, liga.getSlug());

                buscarTimesDaLiga.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn((Throwable throwable) -> {
                            if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                                return new ApiAuthLigaSlug();
                            }
                            logErrorOnConsole(TAG, "buscarTimesDaLiga.onErrorReturn() -> "+throwable.getMessage(), throwable);
                            return null;
                        })
                        .subscribe(
                                times -> AsyncTask.execute(() -> {

                                    if (times != null && times.getTimes() != null && times.getTimes().size() > 0){

                                        new Thread(() -> {

                                            Realm realm = null;

                                            try {

                                                realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(realmTransaction -> {

                                                    RealmList<TimeLiga> timesDaLiga = new RealmList<>();
                                                    Map<String, TimeLiga> chaveDeTimesDaApi = new HashMap<>();
                                                    TimeFavorito timeFavorito = realmTransaction.where(TimeFavorito.class).equalTo("timeDoUsuario", true).findFirst();

                                                    for (ApiAuthLigaSlug_Time time : times.getTimes()){

                                                        boolean timeDoUsuario = false;
                                                        if (timeFavorito != null && timeFavorito.getTimeId().equals(time.getTimeId())) timeDoUsuario = true;

                                                        TimeLiga timeDaLiga = new TimeLiga(liga.getLigaId(), time, null, timeDoUsuario);

                                                        timesDaLiga.add(timeDaLiga);
                                                        chaveDeTimesDaApi.put(String.valueOf(liga.getLigaId())+String.valueOf(time.getTimeId()), timeDaLiga);
                                                    }

                                                    final RealmResults<TimeLiga> timesDaLigaOnRealm = realmTransaction.where(TimeLiga.class).equalTo("ligaId", liga.getLigaId()).findAll();
                                                    if (timesDaLigaOnRealm != null && timesDaLigaOnRealm.size() > 0){

                                                        for (TimeLiga timeDaLiga : timesDaLigaOnRealm){

                                                            if (chaveDeTimesDaApi.get(String.valueOf(timeDaLiga.getId())) == null){

                                                                timeDaLiga.deleteFromRealm();

                                                            } else {

                                                                mergeTimesDaLiga(timeDaLiga, chaveDeTimesDaApi.get(String.valueOf(timeDaLiga.getId())));
                                                                chaveDeTimesDaApi.remove(String.valueOf(timeDaLiga.getId()));
                                                            }
                                                        }

                                                        if (chaveDeTimesDaApi.size() > 0){

                                                            for (Map.Entry<String, TimeLiga> entry : chaveDeTimesDaApi.entrySet()){

                                                                realmTransaction.copyToRealmOrUpdate(entry.getValue());
                                                            }
                                                        }
                                                    } else if (timesDaLiga.size() > 0){

                                                        realmTransaction.copyToRealmOrUpdate(timesDaLiga);
                                                    }

                                                    mapDeLigas.remove(liga.getSlug());
                                                    buscarTimesDaLiga(mapDeLigas, token);
                                                });

                                            } catch (Exception e){

                                                logErrorOnConsole(TAG, "buscarTimesDaLiga.subscribe() -> "+e.getMessage(), e);

                                            } finally {
                                                if (realm != null) realm.close();
                                            }
                                        }).start();
                                    }
                                }),
                                error -> {
                                    try {
                                        if (error instanceof NullPointerException){
                                            logErrorOnConsole(TAG, "ApiAuthLigaSlug [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
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
            } else {

                Log.w(TAG, "buscarTimesDaLiga(liga) -> fim");
                buscarAtletasPontuados(false, true, null);
            }
        } catch (Exception e){
            logErrorOnConsole(TAG, "Falha ao buscarTimesDaLiga() -> "+e.getMessage(), e);
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
                        if (throwable.getMessage().toString().equals("Network is unreachable") || throwable.getMessage().toString().equals("SSL handshake timed out") || throwable.getMessage().toString().equals("timeout")){
                            return new ApiPartidas();
                        }
                        logErrorOnConsole(TAG, "buscarPartidas("+buscarRodada+").onErrorReturn() -> "+throwable.getMessage(), throwable);
                        return null;
                    })
                    .subscribe(
                            partidas -> {

                                if (partidas != null && partidas.getPartidas() != null && partidas.getPartidas().size() > 0){

                                    Realm realm = null;

                                    try {

                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(realmTransaction -> {

                                            final int rodada = partidas.getRodada();

                                            RealmList<Partida> partidasDaRodada = new RealmList<>();
                                            Map<String, Partida> chaveDePartidasDaApi = new HashMap<>();

                                            realmTransaction.copyToRealmOrUpdate(new Partida((-rodada), rodada, rodada+"ª rodada"));

                                            for (ApiPartidas_Partida partidaApi : partidas.getPartidas()){

                                                Partida partida = new Partida(rodada, null, partidaApi);
                                                partidasDaRodada.add(partida);

                                                chaveDePartidasDaApi.put(String.valueOf(partidaApi.getIdPartida()),partida);
                                            }

                                            final RealmResults<Partida> partidasDaRodadaOnRealm = realmTransaction.where(Partida.class).equalTo("rodada", rodada).findAll();
                                            if (partidasDaRodadaOnRealm != null && partidasDaRodadaOnRealm.size() > 0){

                                                for (Partida partida : partidasDaRodadaOnRealm){

                                                    if (chaveDePartidasDaApi.get(String.valueOf(partida.getIdPartida())) == null){

                                                        if (!(partida.getIdPartida() < 0 && partida.getIdPartida() == (-rodada))){
                                                            partida.deleteFromRealm();
                                                        }

                                                    } else {

                                                        mergePartidas(partida, chaveDePartidasDaApi.get(String.valueOf(partida.getIdPartida())));
                                                        chaveDePartidasDaApi.remove(String.valueOf(partida.getIdPartida()));
                                                    }
                                                }

                                                if (chaveDePartidasDaApi.size() > 0){

                                                    for (Map.Entry<String, Partida> entry : chaveDePartidasDaApi.entrySet()){

                                                        realmTransaction.copyToRealmOrUpdate(entry.getValue());
                                                    }
                                                }
                                            } else if (partidasDaRodada.size() > 0){

                                                realmTransaction.copyToRealmOrUpdate(partidasDaRodada);
                                            }
                                        });

                                        new Thread(() -> AsyncTask.execute(() -> verificarRodadasAnteriores(partidas.getRodada()-1) ) ).start();

                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, "buscarPartidas("+buscarRodada+") -> "+e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                }
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        logErrorOnConsole(TAG, "ApiPartidas [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
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
            logErrorOnConsole(TAG, "Falha ao buscarPartidas("+buscarRodada+") -> "+e.getMessage(), e);
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

                logErrorOnConsole(TAG, "verificarRodadasAnteriores() -> "+e.getMessage(), e);

            } finally {
                if (realm != null) realm.close();
            }
        }
    }
}