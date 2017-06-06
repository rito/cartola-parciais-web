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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.mapsAreEqual;
import static br.com.devgeek.cartolaparciais.util.HttpURLConnectionUtil.sendRequestGET;

/**
 * Created by geovannefduarte
 */
@Component
public class AtletasPontuadosTaskRunner {

    private static final int TEN_SECONDS = 10000;
    private static final String FIREBASE_DATABASE_REFERENCE = "atletas/pontuados";
    private static final String URL = "https://api.cartolafc.globo.com/atletas/pontuados";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private FirebaseService firebaseService;

    private static Map<String, Object> atletasPontuados = null;

    @Autowired
    public AtletasPontuadosTaskRunner(FirebaseService firebaseService){

        try {

            this.firebaseService = firebaseService;

             ApiReturn apiReturn = sendRequestGET(firebaseService.getFirebaseUrl()+"/"+FIREBASE_DATABASE_REFERENCE+".json?auth="+firebaseService.getFirebaseDatabaseSecret());

            if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK && !apiReturn.getResponse().equals("null")){

                atletasPontuados = new Gson().fromJson(apiReturn.getResponse(), new TypeToken<HashMap<String, Object>>(){}.getType());
            }

        } catch (Exception e){
            System.out.println("getDataFromFirebaseDatabase["+FIREBASE_DATABASE_REFERENCE+"]");
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = TEN_SECONDS)
    public void buscarDadosApiCartolaFC_atletasPontuados(){

        long startTime = System.currentTimeMillis();
        String task = "AtletasPontuadosTaskRunner - buscarDadosApiCartolaFC_atletasPontuados |" + dateFormat.format(new Date(startTime)) + "| -> ";

        try {

            ApiReturn apiReturn = sendRequestGET(URL);

            if (apiReturn.getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT){

                if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK){

                    Map<String, Object> response = new Gson().fromJson(apiReturn.getResponse(), new TypeToken<HashMap<String, Object>>(){}.getType());

                    if (!mapsAreEqual(response, atletasPontuados)){

                        DatabaseReference databaseReference = firebaseService.getDatabaseReference(FIREBASE_DATABASE_REFERENCE);
                        Task<Void> firebaseSaveTask = databaseReference.setValue(response);

                        try {
                            Tasks.await(firebaseSaveTask);
                        } catch (ExecutionException e){
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("AtletasPontuadosTaskRunner - buscarDadosApiCartolaFC_atletasPontuados | StatusCode -> " + apiReturn.getStatusCode() + " |");
                }
            }

            System.out.println(task + String.valueOf(System.currentTimeMillis() - startTime) + " ms");

        } catch (Exception e){
            System.out.println("AtletasPontuadosTaskRunner - buscarDadosApiCartolaFC_atletasPontuados | " + dateFormat.format(System.currentTimeMillis()) + "|");
            e.printStackTrace();
        }
    }
}