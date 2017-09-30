package br.com.devgeek.cartolaparciais.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.UsuarioGlobo;
import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by geovannefduarte
 */
public class CartolaParciaisUtil {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void logErrorOnConsole(String tag, String error, Object exception){

        String errorMessage = "";
        errorMessage += "########################################################################\n";
        errorMessage += "##### ERROR ON -> [ " + tag + " ] " +                                  "\n";
        errorMessage += "########################################################################\n";
        errorMessage += "##### " + error +                                                      "\n";
        errorMessage += "########################################################################\n";

        Log.e(tag, errorMessage);
    }

    public static boolean userGloboIsLogged(){

        Realm realm = null;
        boolean userGloboIsLogged = false;

        try {

            realm = Realm.getDefaultInstance();
            UsuarioGlobo usuarioGlobo = realm.where(UsuarioGlobo.class).findFirst();

            if (usuarioGlobo != null && usuarioGlobo.getGlbId() != null) userGloboIsLogged = true;

        } catch (Exception e){

            logErrorOnConsole(TAG, e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        return userGloboIsLogged;
    }

    public static String getX_GLB_Token(){

        String token = null;
        Realm realm = null;

        try {

            realm = Realm.getDefaultInstance();
            UsuarioGlobo usuarioGlobo = realm.where(UsuarioGlobo.class).findFirst();

            if (usuarioGlobo != null && usuarioGlobo.getGlbId() != null) token = usuarioGlobo.getGlbId();

        } catch (Exception e){

            logErrorOnConsole(TAG, e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        return token;
    }

    public static List<AtletasPontuados> parseAndSortAtletasPontuados(Gson gson, String json){

        List<AtletasPontuados> atletasPontuados = gson.fromJson(json, new TypeToken<List<AtletasPontuados>>(){}.getType());
        Collections.sort(atletasPontuados, (AtletasPontuados t1, AtletasPontuados t2) -> {

            if (t1.getPosicaoId() != null && t2.getPosicaoId() != null){
                if (t1.getPosicaoId() < t2.getPosicaoId()) return -1;
                if (t1.getPosicaoId() > t2.getPosicaoId()) return 1;
            }

            return t1.getApelido().compareTo(t2.getApelido());
        });

        return atletasPontuados;
    }

    public static String padRight(String s, int n){
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n){
        return String.format("%1$" + n + "s", s);
    }
}