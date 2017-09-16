package br.com.devgeek.cartolaparciais.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.api.model.ApiLogin;
import br.com.devgeek.cartolaparciais.api.model.ApiLogin_Payload;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int SERVICE_ID = 4728;

    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the following code to make the up arrow white:
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.entrar).setOnClickListener(view -> {

            retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://login.globo.com/")
                    .build();

            apiService = retrofit.create(ApiService.class);

            if (isNetworkAvailable(getApplicationContext())){

                ApiLogin_Payload payload = new ApiLogin_Payload("geovanne@devgeek.com.br", "G.d_101202", SERVICE_ID);

                try {

                    Observable<ApiLogin> fazerLoginNaGlobo = apiService.fazerLoginNaGlobo(payload);

                    fazerLoginNaGlobo.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorReturn((Throwable throwable) -> {
                                logErrorOnConsole(TAG, "fazerLoginNaGlobo.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                                return null; //empty object of the datatype
                            })
                            .subscribe(login -> {

                                Log.w(TAG, "fazerLoginNaGlobo: "+login.getUserMessage());

                            }, error -> {
                                try {
                                    if (error instanceof NullPointerException){
                                        logErrorOnConsole(TAG, "ApiLogin [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof HttpException){ // We had non-200 http error
                                        logErrorOnConsole(TAG, "ApiLogin [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else if (error instanceof IOException){ // A network error happened
                                        logErrorOnConsole(TAG, "ApiLogin [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                                    } else {
                                        logErrorOnConsole(TAG, "ApiLogin -> " + error.getMessage() + " / " + error.getClass(), error);
                                    }
                                } catch (Exception e){
                                    logErrorOnConsole(TAG, "ApiLogin -> " + error.getMessage() + " / " + error.getClass(), error);
                                }
                            });
                } catch (Exception e){
                    logErrorOnConsole(TAG, "Falha ao fazerLoginNaGlobo() -> "+e.getMessage(), e);
                }

            } else {
                Snackbar.make( view, "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
                logErrorOnConsole(TAG, "Sem conexão com a internet", null);
            }
        });
        //https://login.globo.com/api/authentication
        //{"payload":{"email":"geovanne@devgeek.com.br","password":"G.d_101202","serviceId":4728}}
    }

    private boolean isEmailValid(String email){
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password){
        //TODO: Replace this with your own logic
        return password.length() > 4;
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