package br.com.devgeek.cartolaparciais.service.impl;

import static br.com.devgeek.cartolaparciais.util.HttpURLConnectionUtil.sendRequestGET;

import br.com.devgeek.cartolaparciais.controller.dto.ApiReturn;
import br.com.devgeek.cartolaparciais.service.FirebaseService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by geovannefduarte
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class FirebaseServiceImplTest {

    @Autowired
    private FirebaseService firebaseService;

    private static String URL_MERCADO_STATUS = "https://api.cartolafc.globo.com/mercado/status";
    private static String mercadoStatus = "";

    @Test
    public void testGetFirebaseDatabaseInstance() throws Exception {

        System.out.println("testFirebaseService() -> initiated");

        Long starTime = System.currentTimeMillis();

        ApiReturn apiReturn = sendRequestGET(URL_MERCADO_STATUS);
        if (apiReturn.getStatusCode() == HttpURLConnection.HTTP_OK){

            if (!mercadoStatus.equals(apiReturn.getResponse())){

                System.out.println(apiReturn.getResponse());


                DatabaseReference ref = firebaseService.getDatabaseReference("mercado/status");
                Map<String, Object> map = new Gson().fromJson(apiReturn.getResponse(), new TypeToken<HashMap<String, Object>>() {}.getType());
                Task<Void> task = ref.setValue(map);


                try {
                    Tasks.await(task);
                } catch (ExecutionException e){
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }


        System.out.println("buscarDadosDaApi() - tempo total -> "+(System.currentTimeMillis() - starTime));
    }
}