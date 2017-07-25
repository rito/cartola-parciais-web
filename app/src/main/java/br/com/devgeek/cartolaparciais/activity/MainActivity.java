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
import android.util.Log;
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
import br.com.devgeek.cartolaparciais.api.ApiMercadoStatus;
import br.com.devgeek.cartolaparciais.api.ApiTimeSlug;
import br.com.devgeek.cartolaparciais.api.ApiTimeSlug_Atleta;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.MercadoStatus;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
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
                atualizarDados();
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
            atualizarDados();
        }
    }


    private void atualizarDados(){

        verificarMercadoStatus();
    }


    private void verificarMercadoStatus(){

        try {

            Observable<ApiMercadoStatus> verificarMercadoStatus = apiService.verificarMercadoStatus();

            verificarMercadoStatus.subscribeOn(Schedulers.newThread())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(apiMercadoStatus -> {

                                      if( apiMercadoStatus != null ){

                                          try {

                                              realm = Realm.getDefaultInstance();

                                              realm.executeTransaction(new Realm.Transaction(){
                                                  @Override
                                                  public void execute(Realm realm){

                                                      MercadoStatus mercadoStatus = new MercadoStatus(apiMercadoStatus);
                                                      MercadoStatus mercadoStatusOnRealm = realm.where(MercadoStatus.class).findFirst();

                                                      if (mercadoStatusOnRealm == null || !mercadoStatusOnRealm.equals(mercadoStatus)){

                                                          if (mercadoStatusOnRealm != null) mercadoStatusOnRealm.deleteFromRealm();
                                                          realm.copyToRealm(mercadoStatus);
                                                      }
                                                  }
                                              });

                                          } catch (Exception e){

                                              Log.e("VerificarMercadoStatus", e.getMessage());
                                              e.printStackTrace();

                                          } finally {
                                              if (realm != null) realm.close();
                                          }
                                      }

                                      buscarAtletasPontuados();
                                  });
        } catch (Exception e){
            Log.e("VerificarMercadoStatus", e.getMessage());
            e.printStackTrace();
            buscarAtletasPontuados();
        }
    }


    private void buscarAtletasPontuados(){

//        final Call<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();
//        buscarAtletasPontuados.enqueue(new Callback<ApiAtletasPontuados>(){
//            @Override
//            public void onResponse(Call<ApiAtletasPontuados> call, Response<ApiAtletasPontuados> response){
//
//                ApiAtletasPontuados atletasPontuadosEncontrados = response.body();
//
//                if( atletasPontuadosEncontrados != null ){
//                    atletasPontuadosEncontrados.getRodada();
//                }
//
//                atualizarParciaisTimesFavoritos(atletasPontuadosEncontrados);
//            }
//
//            @Override
//            public void onFailure(Call<ApiAtletasPontuados> call, Throwable t){
//                atualizarParciaisTimesFavoritos(null);
//            }
//        });

        try {

            Observable<ApiAtletasPontuados> buscarAtletasPontuados = apiService.buscarAtletasPontuados();

            buscarAtletasPontuados.subscribeOn(Schedulers.newThread())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(apiAtletasPontuados -> {

                                      if( apiAtletasPontuados != null ){

//                                          try {
//
//                                              realm = Realm.getDefaultInstance();
//
//                                              realm.executeTransaction(new Realm.Transaction(){
//                                                  @Override
//                                                  public void execute(Realm realm){
//
//                                                      int rodada = apiAtletasPontuados.getRodada();
//                                                      List<AtletasPontuados> atletasPontuadosNaRodada = null;
//                                                      if (apiAtletasPontuados.getAtletas() != null && apiAtletasPontuados.getAtletas().size() > 0){
//
//                                                          atletasPontuadosNaRodada = new ArrayList<AtletasPontuados>();
//                                                          for (Map.Entry<String, ApiAtletasPontuados_PontuacaoAtleta> entry : apiAtletasPontuados.getAtletas().entrySet()){
//
//                                                              if (entry.getKey() != null && !entry.getKey().toString().equals("") && entry.getValue() != null){
//
//                                                                  atletasPontuadosNaRodada.add(new AtletasPontuados(rodada, entry.getKey(), entry.getValue()));
//                                                              }
//                                                          }
//                                                      }
//
////                                                      RealmResults<AtletasPontuados> atletasPontuadosNaRodada01 = realm.where(AtletasPontuados.class).equalTo("rodada", rodada).findAll();
////                                                      if (atletasPontuadosNaRodada01 != null && atletasPontuadosNaRodada01.size() > 0){
////                                                          atletasPontuadosNaRodada01.deleteAllFromRealm();
////                                                      }
//
//
//                                                      List<AtletasPontuados> listaDeAtletasPontuadosNaRodada = new ArrayList<AtletasPontuados>();
//                                                      AtletasPontuadosPorRodada atletasPontuadosPorRodada  = realm.where(AtletasPontuadosPorRodada.class).findFirst();
//                                                      if (atletasPontuadosPorRodada != null){
//                                                          switch (rodada){
//                                                              case  1: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada01(); break;
//                                                              case  2: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada02(); break;
//                                                              case  3: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada03(); break;
//                                                              case  4: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada04(); break;
//                                                              case  5: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada05(); break;
//                                                              case  6: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada06(); break;
//                                                              case  7: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada07(); break;
//                                                              case  8: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada08(); break;
//                                                              case  9: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada09(); break;
//                                                              case 10: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada10(); break;
//                                                              case 11: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada11(); break;
//                                                              case 12: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada12(); break;
//                                                              case 13: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada13(); break;
//                                                              case 14: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada14(); break;
//                                                              case 15: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada15(); break;
//                                                              case 16: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada16(); break;
//                                                              case 17: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada17(); break;
//                                                              case 18: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada18(); break;
//                                                              case 19: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada19(); break;
//                                                              case 20: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada20(); break;
//                                                              case 21: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada21(); break;
//                                                              case 22: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada22(); break;
//                                                              case 23: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada23(); break;
//                                                              case 24: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada24(); break;
//                                                              case 25: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada25(); break;
//                                                              case 26: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada26(); break;
//                                                              case 27: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada27(); break;
//                                                              case 28: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada28(); break;
//                                                              case 29: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada29(); break;
//                                                              case 30: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada30(); break;
//                                                              case 31: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada31(); break;
//                                                              case 32: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada32(); break;
//                                                              case 33: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada33(); break;
//                                                              case 34: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada34(); break;
//                                                              case 35: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada35(); break;
//                                                              case 36: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada36(); break;
//                                                              case 37: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada37(); break;
//                                                              case 38: listaDeAtletasPontuadosNaRodada = atletasPontuadosPorRodada.getRodada38(); break;
//                                                              default: break;
//                                                          }
//                                                      }
//
//                                                      for (AtletasPontuados atletasPontuados : listaDeAtletasPontuadosNaRodada){
//                                                          atletasPontuados.deleteFromRealm();
//                                                      }
//
//                                                      if (atletasPontuadosNaRodada != null && atletasPontuadosNaRodada.size() > 0){
//                                                          switch (rodada){
//                                                              case  1: atletasPontuadosPorRodada.setRodada01(atletasPontuadosNaRodada); break;
//                                                              case  2: atletasPontuadosPorRodada.setRodada02(atletasPontuadosNaRodada); break;
//                                                              case  3: atletasPontuadosPorRodada.setRodada03(atletasPontuadosNaRodada); break;
//                                                              case  4: atletasPontuadosPorRodada.setRodada04(atletasPontuadosNaRodada); break;
//                                                              case  5: atletasPontuadosPorRodada.setRodada05(atletasPontuadosNaRodada); break;
//                                                              case  6: atletasPontuadosPorRodada.setRodada06(atletasPontuadosNaRodada); break;
//                                                              case  7: atletasPontuadosPorRodada.setRodada07(atletasPontuadosNaRodada); break;
//                                                              case  8: atletasPontuadosPorRodada.setRodada08(atletasPontuadosNaRodada); break;
//                                                              case  9: atletasPontuadosPorRodada.setRodada09(atletasPontuadosNaRodada); break;
//                                                              case 10: atletasPontuadosPorRodada.setRodada10(atletasPontuadosNaRodada); break;
//                                                              case 11: atletasPontuadosPorRodada.setRodada11(atletasPontuadosNaRodada); break;
//                                                              case 12: atletasPontuadosPorRodada.setRodada12(atletasPontuadosNaRodada); break;
//                                                              case 13: atletasPontuadosPorRodada.setRodada13(atletasPontuadosNaRodada); break;
//                                                              case 14: atletasPontuadosPorRodada.setRodada14(atletasPontuadosNaRodada); break;
//                                                              case 15: atletasPontuadosPorRodada.setRodada15(atletasPontuadosNaRodada); break;
//                                                              case 16: atletasPontuadosPorRodada.setRodada16(atletasPontuadosNaRodada); break;
//                                                              case 17: atletasPontuadosPorRodada.setRodada17(atletasPontuadosNaRodada); break;
//                                                              case 18: atletasPontuadosPorRodada.setRodada18(atletasPontuadosNaRodada); break;
//                                                              case 19: atletasPontuadosPorRodada.setRodada19(atletasPontuadosNaRodada); break;
//                                                              case 20: atletasPontuadosPorRodada.setRodada20(atletasPontuadosNaRodada); break;
//                                                              case 21: atletasPontuadosPorRodada.setRodada21(atletasPontuadosNaRodada); break;
//                                                              case 22: atletasPontuadosPorRodada.setRodada22(atletasPontuadosNaRodada); break;
//                                                              case 23: atletasPontuadosPorRodada.setRodada23(atletasPontuadosNaRodada); break;
//                                                              case 24: atletasPontuadosPorRodada.setRodada24(atletasPontuadosNaRodada); break;
//                                                              case 25: atletasPontuadosPorRodada.setRodada25(atletasPontuadosNaRodada); break;
//                                                              case 26: atletasPontuadosPorRodada.setRodada26(atletasPontuadosNaRodada); break;
//                                                              case 27: atletasPontuadosPorRodada.setRodada27(atletasPontuadosNaRodada); break;
//                                                              case 28: atletasPontuadosPorRodada.setRodada28(atletasPontuadosNaRodada); break;
//                                                              case 29: atletasPontuadosPorRodada.setRodada29(atletasPontuadosNaRodada); break;
//                                                              case 30: atletasPontuadosPorRodada.setRodada30(atletasPontuadosNaRodada); break;
//                                                              case 31: atletasPontuadosPorRodada.setRodada31(atletasPontuadosNaRodada); break;
//                                                              case 32: atletasPontuadosPorRodada.setRodada32(atletasPontuadosNaRodada); break;
//                                                              case 33: atletasPontuadosPorRodada.setRodada33(atletasPontuadosNaRodada); break;
//                                                              case 34: atletasPontuadosPorRodada.setRodada34(atletasPontuadosNaRodada); break;
//                                                              case 35: atletasPontuadosPorRodada.setRodada35(atletasPontuadosNaRodada); break;
//                                                              case 36: atletasPontuadosPorRodada.setRodada36(atletasPontuadosNaRodada); break;
//                                                              case 37: atletasPontuadosPorRodada.setRodada37(atletasPontuadosNaRodada); break;
//                                                              case 38: atletasPontuadosPorRodada.setRodada38(atletasPontuadosNaRodada); break;
//                                                              default: break;
//                                                          }
//                                                      }
//
//                                                      realm.copyToRealmOrUpdate(atletasPontuadosPorRodada);
//                                                  }
//                                              });
//
//                                          } catch (Exception e){
//
//                                              Log.e("BuscarAtletasPontuados", e.getMessage());
//                                              e.printStackTrace();
//
//                                          } finally {
//                                              if (realm != null) realm.close();
//                                          }
                                      }


                                      atualizarParciaisTimesFavoritos(apiAtletasPontuados);

                                  });
        } catch (Exception e){
            Log.e("BuscarAtletasPontuados", e.getMessage());
            e.printStackTrace();
            atualizarParciaisTimesFavoritos(null);
        }
    }

    private void atualizarParciaisTimesFavoritos(ApiAtletasPontuados atletasPontuadosEncontrados){

        try {

            realm = Realm.getDefaultInstance();

            List<TimeFavorito> timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

            if (timesFavoritos != null && timesFavoritos.size() > 0){

                for (TimeFavorito timeFavorito : timesFavoritos){

//                    if (timeFavorito.getAtletas() == null || timeFavorito.getAtletas().size() == 0){
//
//
//                    }
                    Observable<ApiTimeSlug> buscarTimeId = apiService.buscarTimeId(timeFavorito.getTimeId());

                    buscarTimeId.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(timeSlug -> {

                                    double pontuacao = 0.0, variacaoCartoletas = 0.0;
                                    timeFavorito.setAtletas(new RealmList<AtletasPontuados>());

                                    for (ApiTimeSlug_Atleta atleta : timeSlug.getAtletas()){

                                        if (atletasPontuadosEncontrados != null){

                                            if (atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())) != null){
                                                pontuacao += atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id())).getPontuacao();
                                                timeFavorito.getAtletas().add(new AtletasPontuados(String.valueOf(atleta.getAtleta_id()), atletasPontuadosEncontrados.getAtletas().get(String.valueOf(atleta.getAtleta_id()))));
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

                                            listaTimesFavoritos.clear();
                                            listaTimesFavoritos.addAll( timesFavoritos );
                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e){

                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                            refreshListaTimesFavoritos.setRefreshing(false);

                                        } finally {
                                            if (realm != null) realm.close();
                                            refreshListaTimesFavoritos.setRefreshing(false);
                                        }
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