package br.com.devgeek.cartolaparciais.api.service.impl;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasMercado_PontuacaoAtleta;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados_PontuacaoAtleta;
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
import io.realm.RealmResults;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToAtualizarMercado;
import static br.com.devgeek.cartolaparciais.CartolaParciais.isTimeToAtualizarParciais;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

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

    public void atualizarMercado(Context context){

        if (isNetworkAvailable(context)){

            if (isTimeToAtualizarMercado()){
                verificarMercadoStatus();
            }

        } else {
            Snackbar.make( ((Activity) context).getWindow().getDecorView().findViewById( android.R.id.content ), "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
            logErrorOnConsole(TAG, "Sem conexão com a internet", null);
        }
    }

    public void atualizarParciais(Context context){

        if (isNetworkAvailable(context)){

            if (isTimeToAtualizarParciais()){
                buscarAtletasPontuados();
            }

        } else {
            Snackbar.make( ((Activity) context).getWindow().getDecorView().findViewById( android.R.id.content ), "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
            logErrorOnConsole(TAG, "Sem conexão com a internet", null);
        }
    }

    private void verificarMercadoStatus(){

        try {

            Observable<ApiMercadoStatus> verificarMercadoStatus = apiService.verificarMercadoStatus();

            verificarMercadoStatus.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn((Throwable throwable) -> {
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
                        if (!throwable.getMessage().toString().equals("Null is not a valid element")){
                            logErrorOnConsole(TAG, "buscarAtletasPontuados.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        } return null;
                    })
                    .subscribe(
                            apiAtletasPontuados -> {

                                AsyncTask.execute(() -> {

                                    Realm realm = null;

                                    try {

                                        RealmList<AtletasPontuados> listaAtletasPontuados = new RealmList<>();

                                        for (Map.Entry<String, ApiAtletasPontuados_PontuacaoAtleta> entry : apiAtletasPontuados.getAtletas().entrySet()){

                                            String atletaId = entry.getKey();
                                            ApiAtletasPontuados_PontuacaoAtleta atleta = entry.getValue();
                                            listaAtletasPontuados.add(new AtletasPontuados(atletaId, apiAtletasPontuados.getRodada(), atleta));
                                        }

                                        if (listaAtletasPontuados.size() > 0){

                                            realm = Realm.getDefaultInstance();
                                            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(listaAtletasPontuados));
                                        }
                                    } catch (Exception e){

                                        logErrorOnConsole(TAG, e.getMessage(), e);

                                    } finally {
                                        if (realm != null) realm.close();
                                    }
                                });

                                atualizarParciaisTimesFavoritos(apiAtletasPontuados);
                            },
                            error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        buscarAtletasMercado();
                                        atualizarParciaisTimesFavoritos(null);
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
                        logErrorOnConsole(TAG, "atualizarParciaisDeCadaTimeFavorito.onErrorReturn()  -> "+throwable.getMessage(), throwable);
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
                                                timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id()))));

                                            } else {

                                                timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atleta.getApelido(), null, null, atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                            }

                                        } else {

                                            pontuacao += atleta.getPontos_num();
                                            variacaoCartoletas += atleta.getVariacao_num();
                                            timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), null, atleta.getApelido(), atleta.getPontos_num(), atleta.getVariacao_num(), atleta.getScout(), atleta.getFoto(), atleta.getPosicao_id(), atleta.getClube_id()));
                                        }
                                    }


                                    timeFavorito.setPontuacao(pontuacao);
                                    timeFavorito.setVariacaoCartoletas(variacaoCartoletas);

                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));

                                } catch (Exception e){

                                    logErrorOnConsole(TAG, e.getMessage(), e);

                                } finally {
                                    if (realm != null) realm.close();
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
                        logErrorOnConsole(TAG, "buscarAtletasMercado.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                        return null; //empty object of the datatype
                    })
                    .subscribe(apiAtletasMercado -> {

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
}