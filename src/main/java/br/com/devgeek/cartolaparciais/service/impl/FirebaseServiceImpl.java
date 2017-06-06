package br.com.devgeek.cartolaparciais.service.impl;

import br.com.devgeek.cartolaparciais.CartolaParciaisWebApplication;
import br.com.devgeek.cartolaparciais.controller.dto.ApiReturn;
import br.com.devgeek.cartolaparciais.service.FirebaseService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static br.com.devgeek.cartolaparciais.util.HttpURLConnectionUtil.sendRequestGET;

/**
 * Created by geovannefduarte
 */
@Service
public class FirebaseServiceImpl implements FirebaseService {

    private static FirebaseOptions options = null;
    private static Properties firebaseProperties = new Properties();

    public FirebaseServiceImpl() throws IOException {

        String firebaseCertificatePath = System.getProperty("user.dir")+"/target/classes/firebase-key/";

        InputStream firebaseConfig = new FileInputStream(firebaseCertificatePath + "firebase-config.properties");
        firebaseProperties.load(firebaseConfig);


        if (options == null){

            options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(new FileInputStream(firebaseCertificatePath + "firebase-adminsdk.json")))
                    .setDatabaseUrl(firebaseProperties.getProperty("firebase.url"))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    @Override
    public String getFirebaseUrl(){
        return firebaseProperties.getProperty("firebase.url");
    }

    @Override
    public String getFirebaseDatabaseSecret(){
        return firebaseProperties.getProperty("firebase.database.secret");
    }

    @Override
    public DatabaseReference getDatabaseReference(String pathReference){

        if (pathReference == null)
            return FirebaseDatabase.getInstance().getReference();

        return FirebaseDatabase.getInstance().getReference(pathReference);
    }

    @Override
    public DatabaseReference getDatabaseReferenceFromUrl(String url){
        return FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }
}