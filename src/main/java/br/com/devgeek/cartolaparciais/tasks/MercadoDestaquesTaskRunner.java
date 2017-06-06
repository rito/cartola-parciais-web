package br.com.devgeek.cartolaparciais.tasks;

import br.com.devgeek.cartolaparciais.controller.dto.ApiReturn;
import br.com.devgeek.cartolaparciais.service.FirebaseService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.equalLists;
import static br.com.devgeek.cartolaparciais.util.HttpURLConnectionUtil.sendRequestGET;

/**
 * Created by geovannefduarte
 */
@Component
public class MercadoDestaquesTaskRunner {

    private static final int ONE_MINUTE = 60000;
    private static final String FIREBASE_DATABASE_REFERENCE = "mercado/destaques";
    private static final String URL = "https://api.cartolafc.globo.com/mercado/destaques";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Gson gson = new Gson();

    private FirebaseService firebaseService;

    private static ArrayList<Map<String, Object>> mercadoDestaques = null;

    @Autowired
    public MercadoDestaquesTaskRunner(FirebaseService firebaseService){

        try {

            this.firebaseService = firebaseService;

            ApiReturn apiReturn = sendRequestGET(firebaseService.getFirebaseUrl()+"/"+FIREBASE_DATABASE_REFERENCE+".json?auth="+firebaseService.getFirebaseDatabaseSecret());

            if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK && !apiReturn.getResponse().equals("null")){

                mercadoDestaques = gson.fromJson(apiReturn.getResponse(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            }

        } catch (Exception e){
            System.out.println("getDataFromFirebaseDatabase["+FIREBASE_DATABASE_REFERENCE+"]");
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    public void buscarDadosApiCartolaFC_mercadoDestaques(){

        long startTime = System.currentTimeMillis();
        String task = "MercadoDestaquesTaskRunner - buscarDadosApiCartolaFC_mercadoDestaques |" + dateFormat.format(new Date(startTime)) + "| -> ";

        try {

            ApiReturn apiReturn = sendRequestGET(URL);
            if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK){

                ArrayList<Map<String, Object>> response = gson.fromJson(apiReturn.getResponse(), new TypeToken<ArrayList<Map<String, Object>>>(){}.getType());

                if (!equalLists(response, mercadoDestaques)){

                    DatabaseReference databaseReference = firebaseService.getDatabaseReference(FIREBASE_DATABASE_REFERENCE);
                    Task<Void> firebaseSaveTask = databaseReference.setValue(response);

                    try {
                        Tasks.await(firebaseSaveTask);
                    } catch (ExecutionException e){
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("MercadoDestaquesTaskRunner - buscarDadosApiCartolaFC_mercadoDestaques | StatusCode -> " + apiReturn.getStatusCode() + " |");
            }

            System.out.println(task + String.valueOf(System.currentTimeMillis() - startTime) + " ms");

        } catch (Exception e){
            System.out.println("MercadoDestaquesTaskRunner - buscarDadosApiCartolaFC_mercadoDestaques | " + dateFormat.format(System.currentTimeMillis()) + "|");
            e.printStackTrace();
        }
    }
}