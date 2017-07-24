package br.com.devgeek.cartolaparciais.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;

public class ParciaisAtletasDoTimeActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = null;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.5f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 150;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private TextView nomeDoTime;
    private SimpleDraweeView avatar;
    private TextView nomeDoTimeTopBar;
    private TextView nomeDoCartoleiro;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-03-03 11:32:38 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(){

        appbar = (AppBarLayout) findViewById( R.id.appbar );
        collapsing = (CollapsingToolbarLayout) findViewById( R.id.collapsing );
        coverImage = (ImageView) findViewById( R.id.imageview_placeholder );
        framelayoutTitle = (FrameLayout) findViewById( R.id.framelayout_title );
        linearlayoutTitle = (LinearLayout) findViewById( R.id.linearlayout_title );
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        avatar = (SimpleDraweeView) findViewById(R.id.avatar);
        nomeDoTime = (TextView) findViewById( R.id.textView_nomeDoTime );
        nomeDoTimeTopBar = (TextView) findViewById( R.id.textView_nomeDoTimeTopBar );
        nomeDoCartoleiro = (TextView) findViewById( R.id.textView_nomeDoCartoleiro );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_parciais_atletas_do_time);
        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(nomeDoTimeTopBar, 0, View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Add the following code to make the up arrow white:
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) dadosParciaisAtletasDoTime = bundle.getParcelable("dadosParciaisAtletasDoTime");

        if (dadosParciaisAtletasDoTime != null){

            //set avatar and cover
            // coverImage.setImageResource(R.drawable.cover);
            Picasso.with( getApplicationContext() )
                    .load( dadosParciaisAtletasDoTime.getUrlEscudoPng() )
                    .into( avatar );

            nomeDoTime.setText(dadosParciaisAtletasDoTime.getNomeDoTime());
            nomeDoTimeTopBar.setText(dadosParciaisAtletasDoTime.getNomeDoTime());
            nomeDoCartoleiro.setText(dadosParciaisAtletasDoTime.getNomeDoCartoleiro());

//            // Configurar toolbar
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_parciaisAtletasDoTime);
//            setSupportActionBar(toolbar);
//
//            getSupportActionBar().setTitle(dadosParciaisAtletasDoTime.getNomeDoTime());
//
//            Picasso.with(getApplicationContext()).load( dadosParciaisAtletasDoTime.getUrlEscudoPng()).resizeDimen(84,105);
//
//
////            Bitmap logoImage = ((BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.arkenstone_fc)).getBitmap();
////            getSupportActionBar().setLogo(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(logoImage, 84, 105, false)));
////            getSupportActionBar().setDisplayUseLogoEnabled(true);
//
//            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_arrow_left_white));
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_parciais_atletas_do_time, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem parciaisDoTime = menu.findItem(R.id.menu_parciaisDoTime);
        parciaisDoTime.setTitle("123.45");
        // parciaisDoTime.setEnabled(false);

        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset){

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage){

        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR){

            if(!mIsTheTitleVisible){
                startAlphaAnimation(nomeDoTimeTopBar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible){
                startAlphaAnimation(nomeDoTimeTopBar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage){

        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS){

            if(mIsTheTitleContainerVisible){
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible){
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility){

        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
