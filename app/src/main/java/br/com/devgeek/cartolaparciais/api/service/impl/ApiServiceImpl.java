package br.com.devgeek.cartolaparciais.api.service.impl;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiMercadoStatus;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug_Atleta;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.MercadoStatus;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ApiServiceImpl {

    private static final String TAG = "ApiServiceImpl";

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


    public void verificarMercadoStatus(){

        try {

            Observable<ApiMercadoStatus> verificarMercadoStatus = apiService.verificarMercadoStatus();

            verificarMercadoStatus.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        Log.e(TAG, "verificarMercadoStatus() -> "+throwable.getMessage());
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

                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();

                            } finally {
                                if (realm != null) realm.close();
                            }
                        }
                    }, error -> {
                        try {
                            if (error instanceof HttpException){ // We had non-200 http error
                                Log.e(TAG, "HttpException -> " + error.getMessage() + " / " + error.getClass());
                            } else if (error instanceof IOException){ // A network error happened
                                Log.e(TAG, "IOException -> " + error.getMessage() + " / " + error.getClass());
                            } else {
                                Log.e(TAG, error.getMessage() + " / " + error.getClass());
                            }
                        } catch (Exception e){
                            Log.e(TAG, e.getMessage());
                        }
                    });
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void buscarAtletasPontuados(FragmentActivity fragmentActivity){

        try {

            Observable<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();

            buscarAtletasPontuados.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
                        Log.e("ApiAtletasPontuados", throwable.getMessage());
                        return null; //empty object of the datatype
                    })
                    .subscribe(
                            apiAtletasPontuados -> atualizarParciaisTimesFavoritos(apiAtletasPontuados, fragmentActivity),
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        atualizarParciaisTimesFavoritos(null, fragmentActivity);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        HttpException httpException = (HttpException) error;
                                        Response response = httpException.response();
                                        Log.e("ApiAtletasPontuados", "HttpException -> " + error.getMessage() + " / " + error.getClass());
                                    } else if (error instanceof IOException){ // A network error happened
                                        Log.e("ApiAtletasPontuados", "IOException -> " + error.getMessage() + " / " + error.getClass());
                                    } else {
                                        Log.e("ApiAtletasPontuados", error.getMessage() + " / " + error.getClass());
                                    }
                                } catch (Exception e) {
                                    Log.e("ApiAtletasPontuados", e.getMessage());
                                }
                            });
        } catch (Exception e){
            Log.e("BuscarAtletasPontuados", e.getMessage());
            e.printStackTrace();
            atualizarParciaisTimesFavoritos(null, fragmentActivity);
        }
    }

    private void atualizarParciaisTimesFavoritos(ApiAtletasPontuados atletasPontuadosEncontrados, FragmentActivity fragmentActivity){

        Realm realm = null;
        List<TimeFavorito> timesFavoritos = null;

        try {

            realm = Realm.getDefaultInstance();

            timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (realm != null) realm.close();
        }

        if (timesFavoritos != null && timesFavoritos.size() > 0){

            for (TimeFavorito timeFavorito : timesFavoritos){

                atualizarParciaisDeCadaTimeFavorito(atletasPontuadosEncontrados, timeFavorito, fragmentActivity);
            }
        }
    }

    private void atualizarParciaisDeCadaTimeFavorito(ApiAtletasPontuados atletasPontuadosEncontrados, TimeFavorito timeFavorito, FragmentActivity fragmentActivity){

        Observable<ApiTimeSlug> buscarTimeId = apiService.buscarTimeId(timeFavorito.getTimeId());

        buscarTimeId.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn((Throwable throwable) -> {
                    Log.e("ApiTimeSlug", throwable.getMessage());
                    return null; //empty object of the datatype
                })
                .subscribe(
                        timeSlug -> {

                            Realm realm = null;
                            double pontuacao = 0.0, variacaoCartoletas = 0.0;

                            try {

                                timeFavorito.setAtletas(new RealmList<>());

                                for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                    if (atletasPontuadosEncontrados != null){

                                        if (atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())) != null){

                                            pontuacao += atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())).getPontuacao();
                                            timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id()))));

                                        } else {

                                            timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), atleta.getApelido(), null, atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                        }

                                    } else {

                                        pontuacao += atleta.getPontos_num();
                                        variacaoCartoletas += atleta.getVariacao_num();
                                        timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), atleta.getApelido(), atleta.getPontos_num(), atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                    }
                                }


                                timeFavorito.setPontuacao(pontuacao);
                                timeFavorito.setVariacaoCartoletas(variacaoCartoletas);

                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));

                            } catch (Exception e){

                                e.printStackTrace();

                            } finally {
                                if (realm != null) realm.close();
                            }

                        },
                        error -> {
                            try {
                                if (error instanceof HttpException){ // We had non-200 http error
                                    Log.e("ApiTimeSlug", "HttpException -> " + error.getMessage() + " / " + error.getClass());
                                } else if (error instanceof IOException){ // A network error happened
                                    Log.e("ApiTimeSlug", "IOException -> " + error.getMessage() + " / " + error.getClass());
                                } else {
                                    Log.e("ApiTimeSlug", error.getMessage() + " / " + error.getClass());
                                }
                            } catch (Exception e) {
                                Log.e("ApiTimeSlug", e.getMessage());
                            }
                        }
                );
    }
}