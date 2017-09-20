package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.Liga;
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

                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);

            } else {

                ligas_title.setVisibility(View.GONE);
                ligas_title.setVisibility(View.INVISIBLE);
                ligas_background.setVisibility(View.VISIBLE);

                Picasso.with( context ).load( liga.getUrlFlamulaPng() ).noFade().into( flamula );
                nome_liga.setText( liga.getNomeDaLiga() );
                descricao_liga.setText( liga.getDescricaoDaLiga() );

                if (liga.getRanking() != null){
                    ranking.setText( liga.getRanking()+"°" );
                } else {
                    ranking.setText( "" );
                }

                if (liga.getTotalTimesLiga() != null){
                    total_times_liga.setText( String.valueOf(liga.getTotalTimesLiga()) );
                } else {
                    total_times_liga.setText( "" );
                }


                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);
            }
        }
    }
}