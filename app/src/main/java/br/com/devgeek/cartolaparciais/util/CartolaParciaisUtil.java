package br.com.devgeek.cartolaparciais.util;

import android.animation.StateListAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.model.UsuarioGlobo;
import io.realm.Realm;

import static android.content.ContentValues.TAG;

/**
 * Created by geovannefduarte
 */
public class CartolaParciaisUtil {

    public static void atualizarMercadoAndLigasAndPartidas(ApiServiceImpl apiService, Context context, boolean checkTime){

        apiService.atualizarMercado(context);
        apiService.atualizarLigas(context, checkTime);
        apiService.atualizarPartidas(context, checkTime);
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean showAds(Context context, Realm realm){

        if (Boolean.valueOf(Settings.System.getString(context.getContentResolver(), "firebase.test.lab")))
            return false;

        TimeFavorito timeFavoritoEspecial = realm.where(TimeFavorito.class).equalTo("timeFavorito", true).equalTo("timeDoUsuario", true).findFirst();
        if (timeFavoritoEspecial == null || (timeFavoritoEspecial != null && (timeFavoritoEspecial.getTimeId() != 11937168 && // bicudoo F.C - Luiz
                                                                              timeFavoritoEspecial.getTimeId() != 69575280 &&
                                                                              timeFavoritoEspecial.getTimeId() != 14912740 &&
                                                                              timeFavoritoEspecial.getTimeId() != 1515887))){
            return true;
        }

        return false;
    }

    public static void setupAds(String tag, Context context, Realm realm, AdView adView){
        // Configurar AdMob
//        AdView adView = new AdView(getActivity());
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(AD_MOB_ID);
        try {

            if (showAds(context, realm)){
                adView.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
                adView.loadAd(adRequest);
            } else {
                adView.setVisibility(View.GONE);
            }
        } catch (Exception e){
            logErrorOnConsole(tag, "Falha ao mostrar adMob() -> "+e.getMessage(), e);
        }

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

        List<AtletasPontuados> atletasPontuados;

        if (json == null) {

            atletasPontuados = new ArrayList<>();

        } else {

            atletasPontuados = gson.fromJson(json, new TypeToken<List<AtletasPontuados>>(){}.getType());

            Collections.sort(atletasPontuados, (AtletasPontuados t1, AtletasPontuados t2) -> {

                if (t1.getPosicaoId() != null && t2.getPosicaoId() != null){
                    if (t1.getPosicaoId() < t2.getPosicaoId()) return -1;
                    if (t1.getPosicaoId() > t2.getPosicaoId()) return 1;
                }

                return t1.getApelido().compareTo(t2.getApelido());
            });
        }

        return atletasPontuados;
    }

    public static String padRight(String s, int n){
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n){
        return String.format("%1$" + n + "s", s);
    }

    public static void checkBuildVersionAndSetStateListAnimator(View view, StateListAnimator stateListAnimator){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            view.setStateListAnimator(stateListAnimator);
        }
    }

    public static void reduceLabelSize(Button button, float proportion){

        SpannableStringBuilder text = new SpannableStringBuilder(button.getText());
        text.setSpan(new RelativeSizeSpan(proportion), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(text);
    }

    public static void reduceLabelSize(TextView view, float proportion){

        SpannableStringBuilder text = new SpannableStringBuilder(view.getText());
        text.setSpan(new RelativeSizeSpan(proportion), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(text);
    }
}