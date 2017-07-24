package br.com.devgeek.cartolaparciais;

import org.junit.Test;

import java.util.List;

import br.com.devgeek.cartolaparciais.api.ApiTime;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by geovannefduarte
 */

public class TestApiTime {

    static ApiTime apiTime = null;

    @Test
    public void buscarTime_arkenstone(){


        System.out.println("Start Test");

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.cartolafc.globo.com/")
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Observable<List<ApiTime>> time = apiService.buscarTimes("arkenstone");

        time.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(timeData -> {

                    apiTime = timeData.get(0);
//                    System.out.println("Slug -> "+timeData.get(0).getSlug());
                });

        while (apiTime == null) sleep(1000);

        System.out.println(apiTime.getSlug()+" -> "+apiTime.getNomeDoCartoleiro());
    }

    private static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}