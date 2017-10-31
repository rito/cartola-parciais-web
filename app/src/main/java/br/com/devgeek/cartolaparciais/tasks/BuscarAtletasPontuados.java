package br.com.devgeek.cartolaparciais.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import br.com.devgeek.cartolaparciais.api.model.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug;
import br.com.devgeek.cartolaparciais.api.model.ApiTimeSlug_Atleta;
import br.com.devgeek.cartolaparciais.model.ParciaisTimes;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by geovannefduarte
 */

public class BuscarAtletasPontuados extends AsyncTask<String, String, String> {

    private static final String TAG = "BuscarAtletasPontuados";


    @Override
    protected String doInBackground(String... params){

        Realm realm = null;
        String result = "OK";

        try {

            realm = Realm.getDefaultInstance();

            List<ParciaisTimes> listaTimes = null;
            OkHttpClient httpClient = new OkHttpClient();

            ApiAtletasPontuados atletasPontuados = ApiAtletasPontuados.getDataViaAPI(httpClient);


            listaTimes = realm.copyFromRealm(realm.where(ParciaisTimes.class).findAllSorted("posicao"));

            if (listaTimes.size() == 0){
                listaTimes.add(new ParciaisTimes("dj-sportclub", 		    "dj-sportclub", 		1, "DJ SportClub", 			"Djonnathan Duarte",0.0, 0.0));
                listaTimes.add(new ParciaisTimes("arkenstone-fc", 		    "arkenstone-fc", 		2, "Arkenstone-fc", 		"Geovanne Duarte", 	0.0, 0.0));
                listaTimes.add(new ParciaisTimes("caiaponiaduartefc", 	    "caiaponiaduartefc", 	3, "CaiapôniaDuarteFC", 	"Genésio Duarte", 	0.0, 0.0));
                listaTimes.add(new ParciaisTimes("sport-club-azanki", 	    "sport-club-azanki", 	4, "Sport¨Club Azanki", 	"Neto Azanki", 		0.0, 0.0));
                listaTimes.add(new ParciaisTimes("auto-pecas-santos-ec",    "auto-pecas-santos-ec", 5, "Auto Pecas Santos EC",  "P.H",              0.0, 0.0));
            }


            for (ParciaisTimes parciaisTime : listaTimes){

                double pontuacao = 0.0, variacaoCartoletas = 0.0;

                ApiTimeSlug timeSlug = ApiTimeSlug.getDataViaAPI(httpClient, parciaisTime.getSlug());

                if (atletasPontuados != null){

                    for (String jogador : buscarListaJogadores(httpClient, parciaisTime.getSlug())){
                        if (atletasPontuados.getAtletas().get(jogador) != null){
                            pontuacao += atletasPontuados.getAtletas().get(jogador).getPontuacao();
                        }
                    }

                } else {

                    for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){
                        pontuacao += atleta.getPontos_num();
                        variacaoCartoletas += atleta.getVariacao_num();
                    }
                }

                parciaisTime.setPontuacao(pontuacao);
                parciaisTime.setVariacaoCartoletas(variacaoCartoletas);
            }

            Collections.sort(listaTimes, (t1, t2) -> {
                if (t1.getPontuacao() < t2.getPontuacao()) return  1;
                if (t1.getPontuacao() > t2.getPontuacao()) return -1;
                return 0;
            });

            for (int i=0; i<listaTimes.size(); i++){ listaTimes.get(i).setPosicao(i+1); }

            realm.beginTransaction();
            realm.insertOrUpdate(listaTimes);
            realm.commitTransaction();









//            realm = Realm.getDefaultInstance();
//
//            realm.beginTransaction();
//            realm.insertOrUpdate(new ParciaisTimes("auto-pecas-santos-ec",  "auto-pecas-santos-ec", 1, "Auto Pecas Santos EC",  "P.H",              0.0, 0.0));
//            realm.insertOrUpdate(new ParciaisTimes("caiaponiaduartefc", 	"caiaponiaduartefc", 	2, "CaiapôniaDuarteFC", 	"Genésio Duarte", 	0.0, 0.0));
//            realm.insertOrUpdate(new ParciaisTimes("sport-club-azanki", 	"sport-club-azanki", 	3, "Sport¨Club Azanki", 	"Neto Azanki", 		0.0, 0.0));
//            realm.insertOrUpdate(new ParciaisTimes("dj-sportclub", 		    "dj-sportclub", 		4, "DJ SportClub", 			"Djonnathan Duarte",0.0, 0.0));
//            realm.insertOrUpdate(new ParciaisTimes("arkenstone-fc", 		"arkenstone-fc", 		5, "Arkenstone-fc", 		"Geovanne Duarte", 	0.0, 0.0));
//            realm.commitTransaction();

        } catch (Exception e){

            e.printStackTrace();
            result = e.getMessage();

        } finally {
            if (realm != null) realm.close();
        }

        return result+"|"+params[0];
    }

    @Override
    protected void onPostExecute(String result){

        String resultado[] = result.split(Pattern.quote("|"));

        if (resultado[0].equals("OK")){

            if (resultado[1].equals("MainActivity")){

//                MainActivity.createViewPager();

            } else {
                Log.w(TAG, "Dados atualizados");
            }

        } else {

            if (resultado[1].equals("MainActivity")){
//                Snackbar.make(MainActivity.fab, resultado[0], Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                Log.e(TAG, resultado[0]);
            }
        }
    }

    private List<String> buscarListaJogadores(OkHttpClient httpClient, String slug) throws IOException {

        List<String> listaJogadores = new ArrayList<String>();

        Request request = new Request.Builder().url("https://api.cartolafc.globo.com/time/slug/"+slug).build();
        Response response = httpClient.newCall(request).execute();

        ApiTimeSlug apiTimeSlug = new Gson().fromJson(response.body().string(), new TypeToken<ApiTimeSlug>(){}.getType());

        if (apiTimeSlug.getAtletas() != null){
            for (ApiTimeSlug_Atleta atleta : apiTimeSlug.getAtletas()){
                listaJogadores.add(String.valueOf(atleta.getAtleta_id()));
            }
        }

        return listaJogadores;
    }
}