package br.com.devgeek.cartolaparciais.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
}