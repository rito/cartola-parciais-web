package br.com.devgeek.cartolaparciais.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.BuscarTimesAdapter;
import br.com.devgeek.cartolaparciais.api.model.ApiTime;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

public class BuscarTimesLigasActivity extends AppCompatActivity {

    private static String TAG = "BuscarTimesLigasAct";

    private EditText nomeTime;
    private BuscarTimesAdapter adapter;
    private List<ApiTime> listaTimes = new ArrayList<>();

    private Retrofit retrofit;
    private ApiService apiService;
    private Observable<List<ApiTime>> timesBuscados = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_times_ligas);


        // Configurar toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buscar times favoritos");


        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaTimes);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( this, mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new BuscarTimesAdapter( this, listaTimes );
        recyclerView.setAdapter( adapter );

        // Configurar busca de times
        retrofit = new Retrofit.Builder()
                               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                               .addConverterFactory(GsonConverterFactory.create())
                               .baseUrl("https://api.cartolafc.globo.com/")
                               .build();

        apiService = retrofit.create(ApiService.class);

        nomeTime = (EditText) findViewById(R.id.editText_nomeTime);
        nomeTime.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence searchText, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void afterTextChanged(Editable editable){

                if (isNetworkAvailable(getApplicationContext())){

                    String nomeTimeParaBuscar = nomeTime.getText().toString().toLowerCase(Locale.getDefault());

                    timesBuscados = apiService.buscarTimes(nomeTimeParaBuscar);

                    timesBuscados.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorReturn((Throwable throwable) -> {
                                logErrorOnConsole(TAG, "buscarTimes.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                                return null; //empty object of the datatype
                            })
                            .subscribe(timesEncontrados -> {

                                listaTimes.clear();
                                listaTimes.addAll( timesEncontrados );
                                adapter.notifyDataSetChanged();

                            }, error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        logErrorOnConsole(TAG, "ApiTime [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiTime [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiTime [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiTime -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiTime -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });
                }
            }
        });
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