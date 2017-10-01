package br.com.devgeek.cartolaparciais.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.ParciaisAtletasDoTimeActivity;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import br.com.devgeek.cartolaparciais.util.PosicoesJogadoresUtil;
import io.realm.RealmResults;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.padLeft;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.parseAndSortAtletasPontuados;

/**
 * Created by geovannefduarte
 */
public class ParciaisTimesFavoritosAdapter extends RecyclerView.Adapter<ParciaisTimesFavoritosAdapter.ViewHolder> {

    private static String TAG = "ParciaisTimesFavoritos";

    private Gson gson;
    private Context context;
    private DecimalFormat formatoPontuacao;
    private DecimalFormat formatoCartoletas;
    private RealmResults<TimeFavorito> listaTimesFavoritos;

    public ParciaisTimesFavoritosAdapter(Context context, RealmResults<TimeFavorito> listaTimesFavoritos){
        this.context = context;
        update(listaTimesFavoritos);
        this.formatoPontuacao = new DecimalFormat(TimeFavorito.FORMATO_PONTUACAO);
        this.formatoCartoletas = new DecimalFormat(TimeFavorito.FORMATO_CARTOLETAS);
        gson = new Gson();
    }

    public void update(RealmResults<TimeFavorito> listaTimesFavoritos){
        this.listaTimesFavoritos = listaTimesFavoritos;
    }

    @Override
    public ParciaisTimesFavoritosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_parciaistimes, parent, false);

        return new ParciaisTimesFavoritosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParciaisTimesFavoritosAdapter.ViewHolder holder, int position){

        int backgroundColor = ContextCompat.getColor(context, R.color.bgColorUser);

        if (!listaTimesFavoritos.get( position ).isTimeDoUsuario()){
            if ((position % 2) == 0){
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorEven);
            } else {
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorOdd);
            }
        }

        holder.setData( listaTimesFavoritos.get( position ), backgroundColor, position );

        holder.itemView.setOnClickListener((View v) -> {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            String pontuacaoFormatada = "";
            if (listaTimesFavoritos.get( position ).getPontuacao() != null){
                pontuacaoFormatada = formatoPontuacao.format(listaTimesFavoritos.get( position ).getPontuacao());
            }
            ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = new ParciaisAtletasDoTimeParcelable(listaTimesFavoritos.get( position ).getTimeId(), listaTimesFavoritos.get( position ).getNomeDoTime(), listaTimesFavoritos.get( position ).getUrlEscudoPng(), listaTimesFavoritos.get( position ).getNomeDoCartoleiro(), pontuacaoFormatada);

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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView posicao;
        ImageView escudo;
        TextView nomeTime;
        TextView pontuacao;
        TextView cartoletas;
        TextView nomeCartoleiro;
        RelativeLayout background;

        Resources resources;
        int margin1dp, jogadoresPontuados;

        public ViewHolder(View itemView){
            super(itemView);

            escudo = (ImageView) itemView.findViewById(R.id.atleta);
            posicao = (TextView) itemView.findViewById(R.id.posicao);
            nomeTime = (TextView) itemView.findViewById(R.id.nome_time);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            cartoletas = (TextView) itemView.findViewById(R.id.cartoletas);
            nomeCartoleiro = (TextView) itemView.findViewById(R.id.nome_cartoleiro);
            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);

            resources = context.getResources();
            margin1dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
        }

        private void setData( TimeFavorito timeFavorito , int backgroundColor, int position){

            Picasso.with( context )
                    .load( timeFavorito.getUrlEscudoPng() )
                    .resize(140,140).centerCrop().noFade()
                    .error( R.drawable.arkenstone_fc )
                    .into( escudo );

            SpannableStringBuilder nomeDoTime = new SpannableStringBuilder(timeFavorito.getNomeDoTime());
            nomeDoTime.setSpan(new RelativeSizeSpan(0.9f), 0, nomeDoTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nomeTime.setText(nomeDoTime);

            SpannableStringBuilder nomeDoCartoleiro = new SpannableStringBuilder(timeFavorito.getNomeDoCartoleiro());
            nomeDoCartoleiro.setSpan(new RelativeSizeSpan(0.95f), 0, nomeDoCartoleiro.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nomeCartoleiro.setText(nomeDoCartoleiro);


            background.setBackgroundColor(backgroundColor);

            ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) background.getLayoutParams();
            if (position == 0){ margins.setMargins(margin1dp, margin1dp, margin1dp, margin1dp); }
            else { margins.setMargins(margin1dp, 0, margin1dp, margin1dp); }
            background.requestLayout();

            posicao.setText(String.valueOf(position+1));

            if (timeFavorito.getVariacaoCartoletas() == null || timeFavorito.getVariacaoCartoletas() == 0){
                cartoletas.setText("");

                jogadoresPontuados = 0;
                for (AtletasPontuados atleta : parseAndSortAtletasPontuados(gson, timeFavorito.getAtletas())){
                    if (atleta.getPontuacao() != null && (atleta.getPosicaoId() != PosicoesJogadoresUtil.TECNICO || (atleta.getPosicaoId() == PosicoesJogadoresUtil.TECNICO && atleta.getPontuacao() != 0))){
                        jogadoresPontuados++;
                    }
                }
            } else {

                SpannableStringBuilder cartoletasFormatada = new SpannableStringBuilder("C$ "+formatoCartoletas.format(timeFavorito.getVariacaoCartoletas()));
                cartoletasFormatada.setSpan(new RelativeSizeSpan(0.65f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cartoletasFormatada.setSpan(new RelativeSizeSpan(0.90f), 3, cartoletasFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cartoletas.setText(cartoletasFormatada);
                if (timeFavorito.getVariacaoCartoletas() > 0){
                    cartoletas.setTextColor(ContextCompat.getColor(context, R.color.cartoletaPositiva));
                } else {
                    cartoletas.setTextColor(ContextCompat.getColor(context, R.color.cartoletaNegativa));
                }
            }

            if (timeFavorito.getPontuacao() == null){
                pontuacao.setText("");
            } else {

                Spanned concatenated;
                SpannableStringBuilder pontuacaoFormatada = new SpannableStringBuilder(padLeft(formatoPontuacao.format(timeFavorito.getPontuacao()),8));
                pontuacaoFormatada.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                pontuacaoFormatada.setSpan(new RelativeSizeSpan(0.9f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // https://stackoverflow.com/q/6612316

                if (timeFavorito.getVariacaoCartoletas() == null || timeFavorito.getVariacaoCartoletas() == 0){

                    SpannableStringBuilder jogadores = new SpannableStringBuilder(jogadoresPontuados+"/12");
                    jogadores.setSpan(new TextAppearanceSpan(context, android.R.style.TextAppearance_DeviceDefault_Small), 0, jogadores.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    jogadores.setSpan(new StyleSpan(Typeface.NORMAL), 0, jogadores.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    jogadores.setSpan(new RelativeSizeSpan(0.65f), 0, jogadores.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    concatenated = (Spanned) TextUtils.concat(jogadores,pontuacaoFormatada);

                } else {
                    concatenated = (Spanned) TextUtils.concat(pontuacaoFormatada);
                }

                SpannableStringBuilder result = new SpannableStringBuilder(concatenated);
                pontuacao.setText(result, TextView.BufferType.SPANNABLE);
            }
        }
    }
}