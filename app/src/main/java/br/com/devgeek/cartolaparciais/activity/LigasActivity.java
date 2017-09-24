package br.com.devgeek.cartolaparciais.activity;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.LigasActivityViewPagerAdapter;
import br.com.devgeek.cartolaparciais.fragment.LigasRodadaFragment;
import br.com.devgeek.cartolaparciais.helper.BottomNavigationViewHelper;
import br.com.devgeek.cartolaparciais.helper.NonSwipeableViewPager;
import br.com.devgeek.cartolaparciais.parcelable.LigasParcelable;

public class LigasActivity extends AppCompatActivity {

    private static final String TAG = "LigasActivity";
    private LigasParcelable dadosDaLiga = null;

    private LigasActivityViewPagerAdapter mPagerAdapter;
    private NonSwipeableViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) dadosDaLiga = bundle.getParcelable("dadosDaLiga");

        if (dadosDaLiga != null){

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ImageView flamula = (ImageView) findViewById(R.id.flamula);
            TextView nomeLiga = (TextView) findViewById(R.id.nome_liga);

            nomeLiga.setText(dadosDaLiga.getNomeDaLiga());
            Picasso.with( getApplicationContext() )
                    .load( dadosDaLiga.getUrlFlamula() )
                    .resize(140,140).centerCrop().noFade()
                    .error( R.drawable.arkenstone_fc )
                    .into( flamula );


            ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
            backButton.setOnClickListener((View v) -> finishActivityWithAnimation());
        }


        // setup the ViewPager
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


        BottomNavigationView tabs = (BottomNavigationView) findViewById(R.id.ligas_navigation);
        BottomNavigationViewHelper.disableShiftMode(tabs);

        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) tabs.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++){

            final View icon = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams iconLayoutParams = icon.getLayoutParams();
            iconLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, displayMetrics);
            iconLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, displayMetrics);
            icon.setLayoutParams(iconLayoutParams);

            final View smallLabel = menuView.getChildAt(i).findViewById(android.support.design.R.id.smallLabel);
            final ViewGroup.LayoutParams smallLabelLayoutParams = smallLabel.getLayoutParams();
            smallLabelLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            smallLabel.setLayoutParams(smallLabelLayoutParams);

            final View largeLabel = menuView.getChildAt(i).findViewById(android.support.design.R.id.largeLabel);
            final ViewGroup.LayoutParams largeLabelLayoutParams = largeLabel.getLayoutParams();
            largeLabelLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            largeLabel.setLayoutParams(largeLabelLayoutParams);
        }

        tabs.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_rodada:
                    setViewPager(0);
                    break;
                case R.id.tab_mes:
                    setViewPager(1);
                    break;
                case R.id.tab_turno:
                    setViewPager(2);
                    break;
                case R.id.tab_campeonato:
                    setViewPager(3);
                    break;
                case R.id.tab_cartoletas:
                    setViewPager(4);
                    break;
            }
            return true;
        });
    }

    private void setupViewPager(ViewPager viewPager){
        mPagerAdapter = new LigasActivityViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(LigasRodadaFragment.newInstance(dadosDaLiga.getLigaId()), "Rodada");
        mPagerAdapter.addFragment(LigasRodadaFragment.newInstance(dadosDaLiga.getLigaId()), "Mês");
        mPagerAdapter.addFragment(LigasRodadaFragment.newInstance(dadosDaLiga.getLigaId()), "Turno");
        mPagerAdapter.addFragment(LigasRodadaFragment.newInstance(dadosDaLiga.getLigaId()), "Campeonato");
        mPagerAdapter.addFragment(LigasRodadaFragment.newInstance(dadosDaLiga.getLigaId()), "Cartoletas");
        viewPager.setAdapter(mPagerAdapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber, false);
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