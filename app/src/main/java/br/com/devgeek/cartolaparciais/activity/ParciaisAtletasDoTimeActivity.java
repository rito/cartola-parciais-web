package br.com.devgeek.cartolaparciais.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisAtletasDoTimeFavoritoAdapter;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import io.realm.Realm;

public class ParciaisAtletasDoTimeActivity extends AppCompatActivity {

    private static String TAG = "ParciaisAtletasDoTime";
    private ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = null;

    private ParciaisAtletasDoTimeFavoritoAdapter adapter;
    private List<AtletasPontuados> atletasPontuados = new ArrayList<AtletasPontuados>();

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parciais_atletas_do_time);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) dadosParciaisAtletasDoTime = bundle.getParcelable("dadosParciaisAtletasDoTime");

        if (dadosParciaisAtletasDoTime != null){

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(dadosParciaisAtletasDoTime.getNomeDoTime());
            getSupportActionBar().setSubtitle(dadosParciaisAtletasDoTime.getNomeDoCartoleiro());
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

            // Add the following code to make the up arrow white:
            final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_back_material);
            upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            ImageView escudo = (ImageView) findViewById(R.id.escudo);
            TextView nomeTime = (TextView) findViewById(R.id.nome_time);
            TextView pontuacao = (TextView) findViewById(R.id.atleta_pontuacao);
            TextView nomeCartoleiro = (TextView) findViewById(R.id.nome_cartoleiro);



            nomeTime.setText(dadosParciaisAtletasDoTime.getNomeDoTime());
            pontuacao.setText(dadosParciaisAtletasDoTime.getParciaisDoTime());
            nomeCartoleiro.setText(dadosParciaisAtletasDoTime.getNomeDoCartoleiro());
//            Picasso.with( getApplicationContext() )
//                   .load( dadosParciaisAtletasDoTime.getUrlEscudoPng() )
//                   .into(new Target(){
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from){
//                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                            escudo.setImageDrawable(drawable);
//                        }
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable){
//                        }
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable){
//                        }
//                    });
            Picasso.with( getApplicationContext() )
                   .load( dadosParciaisAtletasDoTime.getUrlEscudoPng() )
                   .error( R.drawable.arkenstone_fc )
                   .into( escudo );



            buscarAtletas(dadosParciaisAtletasDoTime.getTimeId());

            // Configurar recyclerView
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaAtletasTimesFavoritos);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );
            recyclerView.setLayoutManager(mLayoutManager);

            DividerItemDecoration divider = new DividerItemDecoration( this, mLayoutManager.getOrientation() );
            recyclerView.addItemDecoration( divider );

            adapter = new ParciaisAtletasDoTimeFavoritoAdapter( this, atletasPontuados );
            recyclerView.setAdapter( adapter );
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_parciais_atletas_do_time, menu);
//        return true;
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu){
//
//        MenuItem parciaisDoTime = menu.findItem(R.id.menu_parciaisDoTime);
//        parciaisDoTime.setTitle("123.45");
//        // parciaisDoTime.setEnabled(false);
//
//        return true;
//    }

    private void buscarAtletas(Long timeId){

        Realm realm = null;

        try {

            realm = Realm.getDefaultInstance();

            TimeFavorito timeFavorito = realm.copyFromRealm(realm.where(TimeFavorito.class).equalTo("timeId", timeId).findFirst());

            if (timeFavorito != null){

                atletasPontuados = timeFavorito.getAtletas();

                Collections.sort(atletasPontuados, (AtletasPontuados t1, AtletasPontuados t2) -> {

                    if (t1.getPosicaoId() != null && t2.getPosicaoId() != null){
                        if (t1.getPosicaoId() < t2.getPosicaoId()) return -1;
                        if (t1.getPosicaoId() > t2.getPosicaoId()) return 1;
                    }

                    return t1.getApelido().compareTo(t2.getApelido());
                });
            }
        } catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } finally {
            if (realm != null) realm.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home:
                finishActivityWithAnimation();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        return true;
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
