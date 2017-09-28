package br.com.devgeek.cartolaparciais.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.MainActivityViewPagerAdapter;
import br.com.devgeek.cartolaparciais.fragment.ParciaisJogadoresFragment;
import br.com.devgeek.cartolaparciais.fragment.ParciaisLigasFragment;
import br.com.devgeek.cartolaparciais.fragment.ParciaisTimesFragment;
import br.com.devgeek.cartolaparciais.fragment.PartidasFragment;
import br.com.devgeek.cartolaparciais.helper.BottomNavigationViewHelper;
import br.com.devgeek.cartolaparciais.helper.ZoomOutPageTransformer;

import static br.com.devgeek.cartolaparciais.R.id.action_adicionartimes;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private MainActivityViewPagerAdapter mPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private ViewPager mViewPager;

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
                    break;
                case R.id.tab_ligas:
                    setViewPager(1);
                    setToolbarTitle("Ligas");
                    break;
                case R.id.tab_jogadores:
                    setViewPager(2);
                    setToolbarTitle("Jogadores");
                    break;
                case R.id.tab_jogos:
                    setViewPager(3);
                    setToolbarTitle("Jogos");
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
        mPagerAdapter.addFragment(new ParciaisTimesFragment(), "Parciais");
        mPagerAdapter.addFragment(new ParciaisLigasFragment(), "Ligas");
        mPagerAdapter.addFragment(new ParciaisJogadoresFragment(), "Jogadores");
        mPagerAdapter.addFragment(new PartidasFragment(), "Jogos");
        viewPager.setAdapter(mPagerAdapter);

        viewPager.setOffscreenPageLimit(3);
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
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.tab_jogadores);
                        break;
                    case 3:
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
}