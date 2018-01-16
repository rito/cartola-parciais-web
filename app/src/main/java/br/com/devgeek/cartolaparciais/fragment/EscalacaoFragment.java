package br.com.devgeek.cartolaparciais.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.realm.Realm;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.parseAndSortAtletasPontuados;
import static br.com.devgeek.cartolaparciais.util.PosicoesJogadoresUtil.getTimeFormacaoAndShowInField;

/**
 * Created by geovannefduarte
 */
public class EscalacaoFragment extends Fragment {

    private static final String TAG = "EscalacaoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        Realm realm = null;
        View view = inflater.inflate(R.layout.fragment_escalacao, container, false);

        try {

            realm = Realm.getDefaultInstance();

            TimeFavorito timeFavorito = realm.copyFromRealm(realm.where(TimeFavorito.class).equalTo("timeDoUsuario", true).findFirst());

            if (timeFavorito != null && timeFavorito.getAtletas() != null) {

                List<AtletasPontuados> atletasPontuados = parseAndSortAtletasPontuados(new Gson(), timeFavorito.getAtletas());
                getTimeFormacaoAndShowInField(getActivity(), view, atletasPontuados, new DecimalFormat(TimeFavorito.FORMATO_PONTUACAO));
            }

        } catch (Exception e){

            logErrorOnConsole(TAG, "atletasPontuados do timeDoUsuario() -> "+e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        return view;
    }
}