package br.com.devgeek.cartolaparciais.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.ParciaisAtletasDoTimeActivity;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;

/**
 * Created by geovannefduarte
 */

public class ParciaisTimesFavoritosAdapter extends RecyclerView.Adapter<ParciaisTimesFavoritosAdapter.ViewHolder> {

    private static String TAG = "ParciaisTimesFavoritos";

    private Context context;
    private DecimalFormat formatoPontuacao;
    private DecimalFormat formatoCartoletas;
    private List<TimeFavorito> listaTimesFavoritos;
    private final Map<String, Integer> backgroundColor = new HashMap<String, Integer>();

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView posicao;
        ImageView escudo;
        TextView nomeTime;
        TextView pontuacao;
        TextView cartoletas;
        TextView nomeCartoleiro;
        RelativeLayout background;

        public ViewHolder(View itemView){
            super(itemView);

            escudo = (ImageView) itemView.findViewById(R.id.atleta);
            posicao = (TextView) itemView.findViewById(R.id.posicao);
            nomeTime = (TextView) itemView.findViewById(R.id.nome_time);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            cartoletas = (TextView) itemView.findViewById(R.id.cartoletas);
            nomeCartoleiro = (TextView) itemView.findViewById(R.id.nome_cartoleiro);
            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);
        }

        private void setData( TimeFavorito timeFavorito , int backgroundColor ){

            Picasso.with( context )
                    .load( timeFavorito.getUrlEscudoPng() )
                    .error( R.drawable.arkenstone_fc )
                    .into( escudo );

            nomeTime.setText(timeFavorito.getNomeDoTime());
            nomeCartoleiro.setText(timeFavorito.getNomeDoCartoleiro());
            background.setBackgroundColor(backgroundColor);

            if (timeFavorito.getPosicao() == null){
                posicao.setText("");
            } else {
                posicao.setText(String.valueOf(timeFavorito.getPosicao()));
            }

            if (timeFavorito.getPontuacao() == null){
                pontuacao.setText("");
            } else {
                pontuacao.setText(formatoPontuacao.format(timeFavorito.getPontuacao()));
            }

            if (timeFavorito.getVariacaoCartoletas() == null){
                cartoletas.setText("");
            } else {
                cartoletas.setText("C$ "+formatoCartoletas.format(timeFavorito.getVariacaoCartoletas()));
            }
        }
    }

    public ParciaisTimesFavoritosAdapter(Context context, List<TimeFavorito> listaTimesFavoritos){
        this.context = context;
        this.listaTimesFavoritos = listaTimesFavoritos;
        this.formatoPontuacao = new DecimalFormat(TimeFavorito.FORMATO_PONTUACAO);
        this.formatoCartoletas = new DecimalFormat(TimeFavorito.FORMATO_CARTOLETAS);
    }

    @Override
    public ParciaisTimesFavoritosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_parciaistimes, parent, false);

        return new ParciaisTimesFavoritosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParciaisTimesFavoritosAdapter.ViewHolder holder, int position){

        int backgroundColor = ContextCompat.getColor(context, R.color.bgColorOdd);

        if ((position % 2) == 0){
            backgroundColor = ContextCompat.getColor(context, R.color.bgColorEven);
        }

        holder.setData( listaTimesFavoritos.get( position ), backgroundColor );

        holder.itemView.setOnClickListener((View v) -> {

            Log.i(TAG, "width: "+holder.escudo.getWidth()+" | height: "+holder.escudo.getHeight());
            Log.i(TAG, "measuredWidth: "+holder.escudo.getMeasuredWidth()+" | measuredHeight: "+holder.escudo.getMeasuredHeight());

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = new ParciaisAtletasDoTimeParcelable(listaTimesFavoritos.get( position ).getTimeId(), listaTimesFavoritos.get( position ).getNomeDoTime(), listaTimesFavoritos.get( position ).getUrlEscudoPng(), listaTimesFavoritos.get( position ).getNomeDoCartoleiro());

            bundle.putParcelable("dadosParciaisAtletasDoTime", dadosParciaisAtletasDoTime);
            intent.putExtras(bundle);
            intent.setClass(context, ParciaisAtletasDoTimeActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
        });
    }

    @Override
    public int getItemCount(){
        return listaTimesFavoritos.size();
    }
}