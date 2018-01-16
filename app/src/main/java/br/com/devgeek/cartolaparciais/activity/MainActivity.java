package br.com.devgeek.cartolaparciais.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.onesignal.OneSignal;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.MainActivityViewPagerAdapter;
import br.com.devgeek.cartolaparciais.fragment.EscalacaoFragment;
import br.com.devgeek.cartolaparciais.fragment.ParciaisJogadoresFragment;
import br.com.devgeek.cartolaparciais.fragment.ParciaisLigasFragment;
import br.com.devgeek.cartolaparciais.fragment.ParciaisTimesFragment;
import br.com.devgeek.cartolaparciais.fragment.PartidasFragment;
import br.com.devgeek.cartolaparciais.helper.BottomNavigationViewHelper;
import br.com.devgeek.cartolaparciais.helper.ZoomOutPageTransformer;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.checkBuildVersionAndSetStateListAnimator;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.reduceLabelSize;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private static Map<String, Boolean> tags = new HashMap<>();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private MainActivityViewPagerAdapter mPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private MenuItem adicionarTimes;
    private ViewPager mViewPager;

    private static void getOneSignalTags(){

        try {

            tags = new HashMap<>();
            tags.put("gol",         false);
            tags.put("assistencia", false);
            tags.put("penalty",     false);
            tags.put("inicio",      false);
            tags.put("termino",     false);
            tags.put("substituicao",false);
            tags.put("cartao_a",    false);
            tags.put("cartao_v",    false);

            OneSignal.getTags(tagsAvailable -> {

                if (tagsAvailable != null && tagsAvailable.length() > 0){

                    try {
                        for(int i = 0; i<tagsAvailable.names().length(); i++){
                                 if (tagsAvailable.names().getString(i).equals("gol"))          tags.put("gol",         true);
                            else if (tagsAvailable.names().getString(i).equals("assistencia"))  tags.put("assistencia", true);
                            else if (tagsAvailable.names().getString(i).equals("penalty"))      tags.put("penalty",     true);
                            else if (tagsAvailable.names().getString(i).equals("inicio"))       tags.put("inicio",      true);
                            else if (tagsAvailable.names().getString(i).equals("termino"))      tags.put("termino",     true);
                            else if (tagsAvailable.names().getString(i).equals("substituicao")) tags.put("substituicao",true);
                            else if (tagsAvailable.names().getString(i).equals("cartao_a"))     tags.put("cartao_a",    true);
                            else if (tagsAvailable.names().getString(i).equals("cartao_v"))     tags.put("cartao_v",    true);
                        }
                    } catch (JSONException e){
                        logErrorOnConsole(TAG, "OneSignal.getTags() -> "+e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e){
            logErrorOnConsole(TAG, "getOneSignalTags() -> "+e.getMessage(), e);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getOneSignalTags();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Configurar toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle("Parciais");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));



        // setup the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        // trocar bottom navigation por esta =D
        // https://android-arsenal.com/details/1/4817
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // http://blog.iamsuleiman.com/quick-return-pattern-with-android-design-support-library/
        // bottomNavigationView.setBehaviorTranslationEnabled(true); https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_parciais:
                    setViewPager(0);
                    setToolbarTitle("Parciais");
                    if (adicionarTimes != null) adicionarTimes.setVisible(true);
                    break;
                case R.id.tab_ligas:
                    setViewPager(1);
                    setToolbarTitle("Ligas");
                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
                    break;
//                case R.id.tab_jogadores:
//                    setViewPager(2);
//                    setToolbarTitle("Jogadores");
//                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
//                    break;
//                case R.id.tab_jogos:
//                    setViewPager(3);
//                    setToolbarTitle("Jogos");
//                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
//                    break;
                case R.id.tab_escalacao:
                    setViewPager(2);
                    setToolbarTitle("Escalação");
                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
                    break;
                case R.id.tab_jogadores:
                    setViewPager(3);
                    setToolbarTitle("Jogadores");
                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
                    break;
                case R.id.tab_jogos:
                    setViewPager(4);
                    setToolbarTitle("Jogos");
                    if (adicionarTimes != null) adicionarTimes.setVisible(false);
                    break;
            }
            return true;
        });
    }

    private void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void setupViewPager(ViewPager viewPager){
        mPagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new ParciaisTimesFragment(),     "Parciais");
        mPagerAdapter.addFragment(new ParciaisLigasFragment(),     "Ligas");
        mPagerAdapter.addFragment(new EscalacaoFragment(),         "Escalação");
        mPagerAdapter.addFragment(new ParciaisJogadoresFragment(), "Jogadores");
        mPagerAdapter.addFragment(new PartidasFragment(),          "Jogos");
        viewPager.setAdapter(mPagerAdapter);

        //viewPager.setOffscreenPageLimit(3);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(final int i, final float v, final int i2){}
            @Override
            public void onPageSelected(final int i){
                switch (i){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.tab_parciais);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.tab_ligas);
                        break;
//                    case 2:
//                        bottomNavigationView.setSelectedItemId(R.id.tab_jogadores);
//                        break;
//                    case 3:
//                        bottomNavigationView.setSelectedItemId(R.id.tab_jogos);
//                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.tab_escalacao);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.tab_jogadores);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.tab_jogos);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(final int i){}
        });
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber, false);
    }

    @Override
    public void onBackPressed(){
        if (mViewPager.getCurrentItem() == 0){
            super.onBackPressed();
            //finish();
        } else {
            setViewPager(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        adicionarTimes = menu.findItem(R.id.action_adicionartimes);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_adicionartimes){

            Intent intent = new Intent(getApplicationContext(), BuscarTimesLigasActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_in_left_to_right);

            return true;

        } else if (id == R.id.action_notifications){

            LayoutInflater inflater = LayoutInflater.from(this);
            View promptsView = inflater.inflate(R.layout.popup_notifications, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);

            AlertDialog alertDialog = alertDialogBuilder.create();

            final Button dialogOk = (Button) promptsView.findViewById(R.id.ok);
            reduceLabelSize(dialogOk, 0.8f);
            checkBuildVersionAndSetStateListAnimator(dialogOk, null);
            dialogOk.setOnClickListener(view -> {
                alertDialog.dismiss();
                getOneSignalTags();
            });

            Switch gol = (Switch) promptsView.findViewById(R.id.gol);
            reduceLabelSize(gol, 0.9f); gol.setChecked(tags.get("gol"));
            gol.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("gol", "gol");
                    } else {
                        OneSignal.deleteTag("gol");
                    }
                }
            });

            Switch assistencia = (Switch) promptsView.findViewById(R.id.assistencia);
            reduceLabelSize(assistencia, 0.9f); assistencia.setChecked(tags.get("assistencia"));
            assistencia.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("assistencia", "assistencia");
                    } else {
                        OneSignal.deleteTag("assistencia");
                    }
                }
            });

            Switch penalty = (Switch) promptsView.findViewById(R.id.penalty);
            reduceLabelSize(penalty, 0.9f); penalty.setChecked(tags.get("penalty"));
            penalty.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("penalty", "penalty");
                    } else {
                        OneSignal.deleteTag("penalty");
                    }
                }
            });

            Switch inicio = (Switch) promptsView.findViewById(R.id.inicio);
            reduceLabelSize(inicio, 0.9f); inicio.setChecked(tags.get("inicio"));
            inicio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("inicio", "inicio");
                    } else {
                        OneSignal.deleteTag("inicio");
                    }
                }
            });

            Switch termino = (Switch) promptsView.findViewById(R.id.termino);
            reduceLabelSize(termino, 0.9f); termino.setChecked(tags.get("termino"));
            termino.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("termino", "termino");
                    } else {
                        OneSignal.deleteTag("termino");
                    }
                }
            });

            Switch substituicao = (Switch) promptsView.findViewById(R.id.substituicao);
            reduceLabelSize(substituicao, 0.9f); substituicao.setChecked(tags.get("substituicao"));
            substituicao.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("substituicao", "substituicao");
                    } else {
                        OneSignal.deleteTag("substituicao");
                    }
                }
            });

            Switch cartao_a = (Switch) promptsView.findViewById(R.id.cartao_a);
            reduceLabelSize(cartao_a, 0.9f); cartao_a.setChecked(tags.get("cartao_a"));
            cartao_a.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("cartao_a", "cartao_a");
                    } else {
                        OneSignal.deleteTag("cartao_a");
                    }
                }
            });

            Switch cartao_v = (Switch) promptsView.findViewById(R.id.cartao_v);
            reduceLabelSize(cartao_v, 0.9f); cartao_v.setChecked(tags.get("cartao_v"));
            cartao_v.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Boolean.valueOf(Settings.System.getString(getApplicationContext().getContentResolver(), "firebase.test.lab")) == false){
                    if (isChecked){
                        OneSignal.sendTag("cartao_v", "cartao_v");
                    } else {
                        OneSignal.deleteTag("cartao_v");
                    }
                }
            });

            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}