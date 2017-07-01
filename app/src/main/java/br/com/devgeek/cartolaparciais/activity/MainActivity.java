package br.com.devgeek.cartolaparciais.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisTimesAdapter;
import br.com.devgeek.cartolaparciais.tasks.BuscarAtletasPontuados;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    public static ViewPager viewPager;
    public static FloatingActionButton fab;
    private static Adapter atletasPontuadosAdapter;
    private static FragmentManager supportFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Realm.init(this);
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
//                .initialData(new Realm.Transaction(){
//                    @Override
//                    public void execute(Realm realm){
////                        realm.createObject(ViewSujeitoFormDTO.class);
//                    }})
//                .build();
//        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
//        Realm.setDefaultConfiguration(realmConfig);

        realm = Realm.getDefaultInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        supportFragmentManager = getSupportFragmentManager();
        setSupportActionBar(toolbar);


        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BuscarAtletasPontuados buscarAtletasPontuados = new BuscarAtletasPontuados();
                buscarAtletasPontuados.execute("MainActivity");

                Snackbar.make(view, "Atualizando parciais...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
    }

    public static void createViewPager(){

        atletasPontuadosAdapter = new Adapter(supportFragmentManager);
        atletasPontuadosAdapter.addFragment(new ParciaisTimesAdapter(), "ParciaisTimes");
        viewPager.setAdapter(atletasPontuadosAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position){
            return mFragmentList.get(position);
        }

        @Override
        public int getCount(){
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}