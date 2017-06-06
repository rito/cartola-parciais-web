package br.com.devgeek.cartolaparciais.service;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by geovannefduarte
 */
public interface FirebaseService {

    public String getFirebaseUrl();
    public String getFirebaseDatabaseSecret();
    public DatabaseReference getDatabaseReference(String pathReference);
    public DatabaseReference getDatabaseReferenceFromUrl(String url);
}