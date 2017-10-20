package br.com.devgeek.cartolaparciais.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.regex.Pattern;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.api.model.ApiAuthTime;
import br.com.devgeek.cartolaparciais.api.model.ApiLogin;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.model.UsuarioGlobo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;

public class LoginActivity extends AppCompatActivity {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private static final String TAG = "LoginActivity";
    private static final int SERVICE_ID = 4728;
    private Retrofit retrofit;
    private ApiService apiService;
    private ProgressDialog progressDialog;

    private RequestBody bodyApiLogin;
    private Observable<ApiLogin> fazerLoginNaGlobo;
    private Observable<ApiAuthTime> informacoesDoTimeLogado;

    private EditText email;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener((View v) -> finishActivityWithAnimation());


//        // Add the following code to make the up arrow white:
//        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_back_material);
//        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://login.globo.com/")
                .build();

        apiService = retrofit.create(ApiService.class);

        email = (EditText) findViewById(R.id.editText_email);
        senha = (EditText) findViewById(R.id.editText_senha);

        findViewById(R.id.entrar).setOnClickListener(view -> validarEmailESenha(view) );
    }

    private void validarEmailESenha(View view){

        String emailInformado = email.getText().toString();
        String senhaInformada = senha.getText().toString();
        if (isEmailValid(emailInformado) && isPasswordValid(senhaInformada)){
            fazerLoginNaGlobo(view, emailInformado.trim(), senhaInformada);
        } else {
            Snackbar.make( view, "Informe seu email e senha corretamente", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();
        }
    }

    private boolean isEmailValid(String email){
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean isPasswordValid(String password){
        return password.length() > 0;
    }

    private void fazerLoginNaGlobo(View view, String email, String password){

        if (isNetworkAvailable(getApplicationContext())){

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(null);
            progressDialog.setTitle(null);
            progressDialog.show();

            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_dialog);

            try {

                bodyApiLogin = RequestBody.create( okhttp3.MediaType.parse( ApiService.APPLICATION_JSON ), "{\"payload\":{\"email\":\""+email+"\",\"password\":\""+password+"\",\"serviceId\":"+SERVICE_ID+"}}" );
                fazerLoginNaGlobo = apiService.fazerLoginNaGlobo(bodyApiLogin);

                fazerLoginNaGlobo.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn((Throwable throwable) -> {

                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")){

                                progressDialog.dismiss();
                                Snackbar.make( view, "Seu e-mail ou senha estão incorretos", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

                            } else {

                                progressDialog.dismiss();
                                Snackbar.make( view, throwable.getMessage(), Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
                            }

                            logErrorOnConsole(TAG, "fazerLoginNaGlobo.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                            return null; //empty object of the datatype
                        })
                        .subscribe(login -> {

                            if (login.getGlbId() == null || login.getGlbId().equals("")){

                                progressDialog.dismiss();
                                Snackbar.make( view, login.getUserMessage(), Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

                            } else {

                                saveX_GLB_TokenOnRealm(view, login);

                            }
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
                            } progressDialog.dismiss();
                        });
            } catch (Exception e){
                logErrorOnConsole(TAG, "Falha ao fazerLoginNaGlobo() -> "+e.getMessage(), e);
                progressDialog.dismiss();
            }
        } else {
            Snackbar.make( view, "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
            logErrorOnConsole(TAG, "Sem conexão com a internet", null);
        }
    }

    private void saveX_GLB_TokenOnRealm(View view, ApiLogin login){

        Realm realm = null;

        try {

            realm = Realm.getDefaultInstance();
            UsuarioGlobo usuarioGlobo = new UsuarioGlobo(login.getGlbId());
            UsuarioGlobo usuarioGloboOnRealm = realm.where(UsuarioGlobo.class).findFirst();

            if (usuarioGloboOnRealm != null) usuarioGloboOnRealm.deleteFromRealm();
            realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(usuarioGlobo));

            buscarInformacoesDoTimeLogado(view, login);

        } catch (Exception e){

            progressDialog.dismiss();
            logErrorOnConsole(TAG, e.getMessage(), e);
            Snackbar.make( view, login.getUserMessage(), Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

        } finally {
            if (realm != null) realm.close();
        }
    }

    private void buscarInformacoesDoTimeLogado(View view, ApiLogin login){

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.cartolafc.globo.com/")
                .build();

        apiService = retrofit.create(ApiService.class);

        informacoesDoTimeLogado = apiService.informacoesDoTimeLogado(login.getGlbId());

        informacoesDoTimeLogado.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn((Throwable throwable) -> {
                    logErrorOnConsole(TAG, "buscarInformacoesDoTimeLogado.onErrorReturn()  -> "+throwable.getMessage(), throwable);
                    return null; //empty object of the datatype
                })
                .subscribe(authTime -> {

                    Realm realm = null;

                    try {

                        TimeFavorito timeFavorito = null;
                        realm = Realm.getDefaultInstance();

                        try {
                            timeFavorito = realm.copyFromRealm(realm.where(TimeFavorito.class).equalTo("timeId", authTime.getTime().getTimeId()).findFirst());
                        } catch (Exception e){
                            if (!e.getMessage().equals("Null objects cannot be copied from Realm."))
                                logErrorOnConsole(TAG, e.getMessage(), e);
                        }

                        if (timeFavorito != null){

                            timeFavorito.setTimeDoUsuario(true);
                            saveTimeFavoritoToRealm(realm, timeFavorito);

                        } else {

                            timeFavorito = new TimeFavorito(authTime.getTime(), true, true);
                            saveTimeFavoritoToRealm(realm, timeFavorito);
                        }

                        ApiServiceImpl apiServiceImpl = new ApiServiceImpl();
                        apiServiceImpl.atualizarLigas(getApplicationContext(), false);
                        apiServiceImpl.atualizarParciais(getApplicationContext(), false, null);

                        new Handler().postDelayed(() -> finishActivityWithAnimation(), 1250);

                    } catch (Exception e){

                        progressDialog.dismiss();
                        logErrorOnConsole(TAG, e.getMessage(), e);
                        Snackbar.make( view, login.getUserMessage(), Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();
                        finishActivityWithAnimation();

                    } finally {
                        if (realm != null) realm.close();
                    }

                }, error -> {
                    try {
                        if (error instanceof NullPointerException){
                            logErrorOnConsole(TAG, "ApiAuthTime [ NullPointerException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                        } else if (error instanceof HttpException){ // We had non-200 http error
                            logErrorOnConsole(TAG, "ApiAuthTime [ HttpException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                        } else if (error instanceof IOException){ // A network error happened
                            logErrorOnConsole(TAG, "ApiAuthTime [ IOException ] -> " + error.getMessage() + " / " + error.getClass(), error);
                        } else {
                            logErrorOnConsole(TAG, "ApiAuthTime -> " + error.getMessage() + " / " + error.getClass(), error);
                        }
                    } catch (Exception e){
                        logErrorOnConsole(TAG, "ApiAuthTime -> " + error.getMessage() + " / " + error.getClass(), error);
                    } progressDialog.dismiss();
                    finishActivityWithAnimation();
                });
    }

    private void saveTimeFavoritoToRealm(Realm realm, TimeFavorito timeFavorito){
        realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(timeFavorito));
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