package br.com.devgeek.cartolaparciais.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.LigasActivity;
import br.com.devgeek.cartolaparciais.model.Liga;
import br.com.devgeek.cartolaparciais.parcelable.LigasParcelable;
import io.realm.RealmResults;

/**
 * Created by geovannefduarte
 */
public class LigasAdapter extends RecyclerView.Adapter<LigasAdapter.ViewHolder> {

    private static final String TAG = "LigasAdapter";

    private Context context;
    private RealmResults<Liga> ligas;


    public LigasAdapter(Context context, RealmResults<Liga> ligas){
        this.context = context;
        update(ligas);
    }


    public void update(RealmResults<Liga> ligas){
        this.ligas = ligas;
    }


    @Override
    public LigasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_ligas, parent, false);
        return new LigasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LigasAdapter.ViewHolder holder, int position){

        holder.setData( holder.itemView, ligas.get( position ) );

        if (ligas.get( position ).getLigaId() < 0){

            holder.itemView.setOnClickListener(null);

        } else {

            holder.itemView.setOnClickListener((View v) -> {

                if (ligas.get( position ).getTipoLiga().equals( "Mata-Mata" ) ){

                    Snackbar.make(v, "Ligas Mata-Mata em breve", Snackbar.LENGTH_LONG).show();

                } else {

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    LigasParcelable dadosDaLiga = new LigasParcelable(ligas.get( position ).getLigaId(), ligas.get( position ).getSlug(), ligas.get( position ).getNomeDaLiga(), ligas.get( position ).getUrlFlamulaPng());

                    bundle.putParcelable("dadosDaLiga", dadosDaLiga);
                    intent.putExtras(bundle);
                    intent.setClass(context, LigasActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return ligas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout ligas_title;
        TextView tipo_liga;

        RelativeLayout ligas_background;
        ImageView flamula;
        TextView nome_liga;
        TextView descricao_liga;
        TextView ranking;
        TextView total_times_liga;

        public ViewHolder(View itemView){
            super(itemView);

            ligas_title = (RelativeLayout) itemView.findViewById(R.id.ligas_title);
            tipo_liga = (TextView) itemView.findViewById(R.id.tipo_liga);

            ligas_background = (RelativeLayout) itemView.findViewById(R.id.ligas_background);
            flamula = (ImageView) itemView.findViewById(R.id.flamula);
            nome_liga = (TextView) itemView.findViewById(R.id.nome_liga);
            descricao_liga = (TextView) itemView.findViewById(R.id.descricao_liga);
            ranking = (TextView) itemView.findViewById(R.id.ranking);
            total_times_liga = (TextView) itemView.findViewById(R.id.total_times_liga);
        }

        private void setData(View holderView, Liga liga){

            Resources resources = context.getResources();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (liga.getLigaId() < 0){

                ligas_background.setVisibility(View.GONE);
                ligas_background.setVisibility(View.INVISIBLE);
                ligas_title.setVisibility(View.VISIBLE);

                tipo_liga.setText(liga.getTipoLiga());

                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);

            } else {

                ligas_title.setVisibility(View.GONE);
                ligas_title.setVisibility(View.INVISIBLE);
                ligas_background.setVisibility(View.VISIBLE);

                SpannableStringBuilder nomeDaLiga = new SpannableStringBuilder(liga.getNomeDaLiga());
                nomeDaLiga.setSpan(new RelativeSizeSpan(0.9f), 0, nomeDaLiga.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                nome_liga.setText(nomeDaLiga);

                SpannableStringBuilder descricaoDaLiga = new SpannableStringBuilder(liga.getDescricaoDaLiga());
                descricaoDaLiga.setSpan(new RelativeSizeSpan(0.95f), 0, descricaoDaLiga.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                descricao_liga.setText(descricaoDaLiga);

                Picasso.with( context ).load( liga.getUrlFlamulaPng() ).noFade().into( flamula );

                if (liga.getRanking() != null){

                    SpannableStringBuilder rankingNaLiga = new SpannableStringBuilder( liga.getRanking()+"°" );
                    rankingNaLiga.setSpan(new RelativeSizeSpan(0.9f), 0, rankingNaLiga.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ranking.setText( rankingNaLiga );

                } else {
                    ranking.setText( "" );
                }

                if (liga.getTotalTimesLiga() != null){

                    SpannableStringBuilder totalTimesLiga = new SpannableStringBuilder( String.valueOf(liga.getTotalTimesLiga()) );
                    totalTimesLiga.setSpan(new RelativeSizeSpan(0.95f), 0, totalTimesLiga.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    total_times_liga.setText( totalTimesLiga );

                } else {
                    total_times_liga.setText( "" );
                }

                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);
            }
        }
    }
}