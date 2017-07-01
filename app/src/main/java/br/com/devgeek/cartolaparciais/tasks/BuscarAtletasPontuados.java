package br.com.devgeek.cartolaparciais.tasks;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.regex.Pattern;

import br.com.devgeek.cartolaparciais.activity.MainActivity;
import br.com.devgeek.cartolaparciais.model.ParciaisTimes;
import io.realm.Realm;

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

            realm.beginTransaction();
            realm.insertOrUpdate(new ParciaisTimes("auto-pecas-santos-ec",  "auto-pecas-santos-ec", 1, "Auto Pecas Santos EC",  "P.H",              0.0, 0.0));
            realm.insertOrUpdate(new ParciaisTimes("caiaponiaduartefc", 	"caiaponiaduartefc", 	2, "CaiapôniaDuarteFC", 	"Genésio Duarte", 	0.0, 0.0));
            realm.insertOrUpdate(new ParciaisTimes("sport-club-azanki", 	"sport-club-azanki", 	3, "Sport¨Club Azanki", 	"Neto Azanki", 		0.0, 0.0));
            realm.insertOrUpdate(new ParciaisTimes("dj-sportclub", 		    "dj-sportclub", 		4, "DJ SportClub", 			"Djonnathan Duarte",0.0, 0.0));
            realm.insertOrUpdate(new ParciaisTimes("arkenstone-fc", 		"arkenstone-fc", 		5, "Arkenstone-fc", 		"Geovanne Duarte", 	0.0, 0.0));
            realm.commitTransaction();

        } catch (Exception e){

            e.printStackTrace();
            result = e.getMessage();

        } finally {
            realm.close();
        }

        return result+"|"+params[0];
    }

    @Override
    protected void onPostExecute(String result){

        String resultado[] = result.split(Pattern.quote("|"));

        if (resultado[0].equals("OK")){

            if (resultado[1].equals("MainActivity")){

                MainActivity.createViewPager();

            } else {
                Log.w(TAG, "Dados atualizados");
            }

        } else {

            if (resultado[1].equals("MainActivity")){
                Snackbar.make(MainActivity.fab, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                Log.e(TAG, resultado[0]);
            }
        }
    }
}