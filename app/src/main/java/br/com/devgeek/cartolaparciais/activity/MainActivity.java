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
import br.com.devgeek.cartolaparciais.helper.BottomNavigationViewHelper;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

import static br.com.devgeek.cartolaparciais.R.id.action_adicionartimes;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private MainActivityViewPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

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







        // setup the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.containter);
        setupViewPager(mViewPager);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_parciais:
                    setViewPager(0);
                    break;
                case R.id.tab_ligas:
                    setViewPager(1);
                    break;
                case R.id.tab_jogadores:
                    setViewPager(2);
                    break;
                case R.id.tab_jogos:
                    setViewPager(3);
                    break;
            }
            return true;
        });
    }

    private void setupViewPager(ViewPager viewPager){
        mPagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new ParciaisTimesFragment(), "Parciais");
        mPagerAdapter.addFragment(new ParciaisTimesFragment(), "Ligas");
        mPagerAdapter.addFragment(new ParciaisTimesFragment(), "Jogadores");
        mPagerAdapter.addFragment(new ParciaisTimesFragment(), "Jogos");
        viewPager.setAdapter(mPagerAdapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
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