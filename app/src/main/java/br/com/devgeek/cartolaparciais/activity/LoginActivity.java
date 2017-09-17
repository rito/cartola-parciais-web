package br.com.devgeek.cartolaparciais.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.regex.Pattern;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.api.model.ApiLogin;
import br.com.devgeek.cartolaparciais.api.service.ApiService;
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
    private RequestBody body;
    private Retrofit retrofit;
    private ApiService apiService;
    private ProgressDialog progressDialog;
    private Observable<ApiLogin> fazerLoginNaGlobo;
    private EditText email;
    private EditText senha;

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

                body = RequestBody.create( okhttp3.MediaType.parse( "application/json; charset=utf-8" ), "{\"payload\":{\"email\":\""+email+"\",\"password\":\""+password+"\",\"serviceId\":"+SERVICE_ID+"}}" );
                fazerLoginNaGlobo = apiService.fazerLoginNaGlobo(body);

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

                                Realm realm = null;

                                try {

                                    // GET -> https://api.cartolafc.globo.com/auth/time
                                    // Content-Type -> application/json
                                    // X-GLB-Token

                                    realm = Realm.getDefaultInstance();
                                    UsuarioGlobo usuarioGlobo = new UsuarioGlobo(login.getGlbId());
                                    UsuarioGlobo usuarioGloboOnRealm = realm.where(UsuarioGlobo.class).findFirst();

                                    if (usuarioGloboOnRealm != null) usuarioGloboOnRealm.deleteFromRealm();
                                    realm.executeTransaction(realmTransaction -> realmTransaction.copyToRealmOrUpdate(usuarioGlobo));

                                    finishActivityWithAnimation();

                                } catch (Exception e){

                                    progressDialog.dismiss();
                                    logErrorOnConsole(TAG, e.getMessage(), e);
                                    Snackbar.make( view, login.getUserMessage(), Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

                                } finally {
                                    if (realm != null) realm.close();
                                }
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