package br.com.devgeek.cartolaparciais.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisTimesFavoritosAdapter;
import br.com.devgeek.cartolaparciais.api.ApiAtletasPontuados;
import br.com.devgeek.cartolaparciais.api.ApiTimeSlug;
import br.com.devgeek.cartolaparciais.api.ApiTimeSlug_Atleta;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.R.id.action_adicionartimes;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private Realm realm = null;
    private ParciaisTimesFavoritosAdapter adapter;
    private SwipeRefreshLayout refreshListaTimesFavoritos;
    private List<TimeFavorito> listaTimesFavoritos = new ArrayList<TimeFavorito>();

    private Retrofit retrofit;
    private ApiService apiService;
    private Observable<ApiAtletasPontuados> atletasPontuados = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(2)               // Must be bumped when the schema changes
                .migration(realmMigration())    // Migration to run instead of throwing an exception
                .deleteRealmIfMigrationNeeded()
                .initialData(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
//                        realm.createObject(TimeFavorito.class);
                    }})
                .build();
//        Realm.deleteRealm(realmConfig);         // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);


        // Configurar toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Parciais");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));



        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaTimesFavoritos);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( this, mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new ParciaisTimesFavoritosAdapter( this, listaTimesFavoritos );
        recyclerView.setAdapter( adapter );




        refreshListaTimesFavoritos = (SwipeRefreshLayout) findViewById(R.id.refreshListaTimesFavoritos);
        refreshListaTimesFavoritos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                buscarAtletasPontuados();
            }
        });



        retrofit = new Retrofit.Builder()
                               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                               .addConverterFactory(GsonConverterFactory.create())
                               .baseUrl("https://api.cartolafc.globo.com/")
                               .build();

        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem adicionarTimes = menu.findItem(R.id.action_adicionartimes);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == action_adicionartimes){

            Intent intent = new Intent(getApplicationContext(), BuscarTimesLigasActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();

        // Buscar times favoritos
        try {

            realm = Realm.getDefaultInstance();

            List<TimeFavorito> timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

            if (timesFavoritos != null && timesFavoritos.size() > 0){

                Collections.sort(timesFavoritos, new Comparator<TimeFavorito>(){ // ordem inversa
                    public int compare(TimeFavorito t1, TimeFavorito t2){

                        if (t1.getPontuacao() != null && t2.getPontuacao() != null){
                            if (t1.getPontuacao() < t2.getPontuacao()) return 1;
                            if (t1.getPontuacao() > t2.getPontuacao()) return -1;
                        }

                        return 0;
                    }
                });

                listaTimesFavoritos.clear();
                listaTimesFavoritos.addAll( timesFavoritos );
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } finally {
            if (realm != null) realm.close();
            buscarAtletasPontuados();
        }
    }

    private void buscarAtletasPontuados(){

        final Call<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();
        buscarAtletasPontuados.enqueue(new Callback<ApiAtletasPontuados>(){
            @Override
            public void onResponse(Call<ApiAtletasPontuados> call, Response<ApiAtletasPontuados> response){

                ApiAtletasPontuados atletasPontuadosEncontrados = response.body();

                if( atletasPontuadosEncontrados != null ){
                    atletasPontuadosEncontrados.getRodada();
                }

                atualizarParciaisTimesFavoritos(atletasPontuadosEncontrados);
            }

            @Override
            public void onFailure(Call<ApiAtletasPontuados> call, Throwable t){
                atualizarParciaisTimesFavoritos(null);
            }
        });
    }

    private void atualizarParciaisTimesFavoritos(ApiAtletasPontuados atletasPontuadosEncontrados){

        try {

            realm = Realm.getDefaultInstance();

            List<TimeFavorito> timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

            if (timesFavoritos != null && timesFavoritos.size() > 0){

                for (TimeFavorito timeFavorito : timesFavoritos){

                    Observable<ApiTimeSlug> buscarTimeSlug = apiService.buscarTimeSlug(timeFavorito.getSlug());

                    buscarTimeSlug.subscribeOn(Schedulers.newThread())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(timeSlug -> {

                                      double pontuacao = 0.0, variacaoCartoletas = 0.0;

                                        for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                            if (atletasPontuadosEncontrados != null){

                                                if (atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())) != null){
                                                    pontuacao += atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())).getPontuacao();
                                                }
                                            } else {

                                                pontuacao += atleta.getPontos_num();
                                                variacaoCartoletas += atleta.getVariacao_num();
                                            }
                                        }

                                        timeFavorito.setPontuacao(pontuacao);
                                        timeFavorito.setVariacaoCartoletas(variacaoCartoletas);
//                                      Log.i(TAG, timeFavorito.getSlug()+" -> pontuacao ["+pontuacao+"] variacaoCartoletas ["+variacaoCartoletas+"]");

                                        if (timesFavoritos.get(timesFavoritos.size()-1).equals(timeFavorito)){

                                            try {

                                                realm = Realm.getDefaultInstance();
                                                Collections.sort(timesFavoritos, new Comparator<TimeFavorito>(){ // ordem inversa
                                                    public int compare(TimeFavorito t1, TimeFavorito t2){

                                                        if (t1.getPontuacao() != null && t2.getPontuacao() != null){
                                                            if (t1.getPontuacao() < t2.getPontuacao()) return 1;
                                                            if (t1.getPontuacao() > t2.getPontuacao()) return -1;
                                                        }

                                                        return 0;
                                                    }
                                                });

                                                for (int i=0; i<timesFavoritos.size(); i++){ timesFavoritos.get(i).setPosicao(i+1); }

                                                realm.executeTransaction(new Realm.Transaction(){
                                                    @Override
                                                    public void execute(Realm realm){

                                                        realm.copyToRealmOrUpdate(timesFavoritos);
                                                    }
                                                });
                                            } catch (Exception e){

                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                                refreshListaTimesFavoritos.setRefreshing(false);

                                            } finally {
                                                if (realm != null) realm.close();
                                                refreshListaTimesFavoritos.setRefreshing(false);
                                            }

                                            listaTimesFavoritos.clear();
                                            listaTimesFavoritos.addAll( timesFavoritos );
                                            adapter.notifyDataSetChanged();
                                        }
                                  });
                }
            }
        } catch (Exception e){

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            refreshListaTimesFavoritos.setRefreshing(false);

        } finally {
            if (realm != null) realm.close();
        }
    }

    private RealmMigration realmMigration(){

        return new RealmMigration(){
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion){

//                // DynamicRealm exposes an editable schema
//                RealmSchema schema = realm.getSchema();
//
//                // Migrate to version 1: Add a new class.
//                // Example:
//                // public Person extends RealmObject {
//                //     private String name;
//                //     private int age;
//                //     // getters and setters left out for brevity
//                // }
//                if (oldVersion == 0){
//                    schema.create("Person")
//                            .addField("name", String.class)
//                            .addField("age", int.class);
//                    oldVersion++;
//                }
//
//                // Migrate to version 2: Add a primary key + object references
//                // Example:
//                // public Person extends RealmObject {
//                //     private String name;
//                //     @PrimaryKey
//                //     private int age;
//                //     private Dog favoriteDog;
//                //     private RealmList<Dog> dogs;
//                //     // getters and setters left out for brevity
//                // }
//                if (oldVersion == 1){
//                    schema.get("Person")
//                            .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                            .addRealmObjectField("favoriteDog", schema.get("Dog"))
//                            .addRealmListField("dogs", schema.get("Dog"));
//                    oldVersion++;
//                }
            }
        };
    }
}