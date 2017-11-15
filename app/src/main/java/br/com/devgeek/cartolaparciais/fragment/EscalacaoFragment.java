package br.com.devgeek.cartolaparciais.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.util.ClubesUtil;
import io.realm.Realm;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.parseAndSortAtletasPontuados;

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
            List<AtletasPontuados> atletasPontuados = new ArrayList<>();
            TimeFavorito timeFavorito = realm.copyFromRealm(realm.where(TimeFavorito.class).equalTo("timeDoUsuario", true).findFirst());
            if (timeFavorito != null && timeFavorito.getAtletas() != null) {

                atletasPontuados = parseAndSortAtletasPontuados(new Gson(), timeFavorito.getAtletas());

                ImageView atacanteEsquerda = (ImageView) view.findViewById(R.id.atacanteEsquerda);
                Picasso.with( getActivity() ).load( atletasPontuados.get(0).getFoto().replace("_FORMATO","_140x140") ).noFade().into( atacanteEsquerda );

                ImageView atacanteEsquerdaEscudo = (ImageView) view.findViewById(R.id.atacanteEsquerdaEscudo);
                atacanteEsquerdaEscudo.setImageResource(ClubesUtil.getClubeEscudo(atletasPontuados.get(0).getClubeId()));
            }

        } catch (Exception e){

            logErrorOnConsole(TAG, "atletasPontuados do timeDoUsuario() -> "+e.getMessage(), e);

        } finally {
            if (realm != null) realm.close();
        }

        return view;
    }
}