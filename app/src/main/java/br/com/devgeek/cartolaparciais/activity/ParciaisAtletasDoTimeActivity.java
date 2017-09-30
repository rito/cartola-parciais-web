package br.com.devgeek.cartolaparciais.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisAtletasDoTimeFavoritoAdapter;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import io.realm.Realm;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.parseAndSortAtletasPontuados;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.userGloboIsLogged;

public class ParciaisAtletasDoTimeActivity extends AppCompatActivity {

    private static String TAG = "ParciaisAtletasDoTime";
    private ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = null;

    private ParciaisAtletasDoTimeFavoritoAdapter adapter;
    private List<AtletasPontuados> atletasPontuados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parciais_atletas_do_time);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) dadosParciaisAtletasDoTime = bundle.getParcelable("dadosParciaisAtletasDoTime");

        if (dadosParciaisAtletasDoTime != null){

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
            backButton.setOnClickListener((View v) -> finishActivityWithAnimation());


            ImageView escudo = (ImageView) findViewById(R.id.escudo);
            TextView nomeTime = (TextView) findViewById(R.id.nome_time);
            TextView pontuacao = (TextView) findViewById(R.id.atleta_pontuacao);
            TextView nomeCartoleiro = (TextView) findViewById(R.id.nome_cartoleiro);

            nomeTime.setText(dadosParciaisAtletasDoTime.getNomeDoTime());
            pontuacao.setText(dadosParciaisAtletasDoTime.getParciaisDoTime());
            nomeCartoleiro.setText(dadosParciaisAtletasDoTime.getNomeDoCartoleiro());

            Picasso.with( getApplicationContext() )
                   .load( dadosParciaisAtletasDoTime.getUrlEscudoPng() )
                   .resize(140,140).centerCrop().noFade()
                   .error( R.drawable.arkenstone_fc )
                   .into( escudo );


            buscarAtletas(dadosParciaisAtletasDoTime.getTimeId());
        }

        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaAtletasTimesFavoritos);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ParciaisAtletasDoTimeFavoritoAdapter( this, atletasPontuados, userGloboIsLogged() );
        recyclerView.setAdapter( adapter );
    }

    private void buscarAtletas(Long timeId){

        Realm realm = null;

        try {

            realm = Realm.getDefaultInstance();

            TimeFavorito timeFavorito = realm.copyFromRealm(realm.where(TimeFavorito.class).equalTo("timeId", timeId).findFirst());

            if (timeFavorito != null){

                atletasPontuados = parseAndSortAtletasPontuados(new Gson(), timeFavorito.getAtletas());
            }
        } catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } finally {
            if (realm != null) realm.close();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishActivityWithAnimation();
    }

    private void finishActivityWithAnimation(){
        finish();
        overridePendingTransition(R.anim.slide_out_left_to_right, R.anim.slide_out_right_to_left);
    }
}
