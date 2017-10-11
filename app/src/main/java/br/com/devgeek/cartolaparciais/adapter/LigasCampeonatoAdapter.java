package br.com.devgeek.cartolaparciais.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.ParciaisAtletasDoTimeNaLigaActivity;
import br.com.devgeek.cartolaparciais.model.TimeLiga;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import io.realm.RealmResults;

/**
 * Created by geovannefduarte on 27/09/17.
 */
public class LigasCampeonatoAdapter extends RecyclerView.Adapter<LigasCampeonatoAdapter.ViewHolder> {

    private static final String TAG = "LigasCampeonatoAdapter";

    private Context context;
    private DecimalFormat formatoPontuacao;
    private RealmResults<TimeLiga> listaTimesDaLiga;

    public LigasCampeonatoAdapter(Context context, RealmResults<TimeLiga> listaTimesDaLiga){
        this.context = context;
        update(listaTimesDaLiga);
        this.formatoPontuacao = new DecimalFormat(TimeLiga.FORMATO_PONTUACAO);
    }

    public void update(RealmResults<TimeLiga> listaTimesDaLiga){
        this.listaTimesDaLiga = listaTimesDaLiga;
    }

    @Override
    public LigasCampeonatoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_ligas_rodada, parent, false);

        return new LigasCampeonatoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LigasCampeonatoAdapter.ViewHolder holder, int position){

        int backgroundColor = ContextCompat.getColor(context, R.color.bgColorUser);

        if (!listaTimesDaLiga.get( position ).isTimeDoUsuario()){
            if ((position % 2) == 0){
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorEven);
            } else {
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorOdd);
            }
        }

        holder.setData( listaTimesDaLiga.get( position ), backgroundColor, position, listaTimesDaLiga.get( 0 ).getPontuacaoCampeonato());

        holder.itemView.setOnClickListener((View v) -> {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            String pontuacaoFormatada = "";
            if (listaTimesDaLiga.get( position ).getPontuacao() != null){
                pontuacaoFormatada = formatoPontuacao.format(listaTimesDaLiga.get( position ).getPontuacao());
            }

            ParciaisAtletasDoTimeParcelable dadosParciaisAtletasDoTime = new ParciaisAtletasDoTimeParcelable(listaTimesDaLiga.get( position ).getLigaId(), listaTimesDaLiga.get( position ).getTimeId(), listaTimesDaLiga.get( position ).getNomeDoTime(), listaTimesDaLiga.get( position ).getUrlEscudoPng(), listaTimesDaLiga.get( position ).getNomeDoCartoleiro(), pontuacaoFormatada);

            bundle.putParcelable("dadosParciaisAtletasDoTime", dadosParciaisAtletasDoTime);
            intent.putExtras(bundle);
            intent.setClass(context, ParciaisAtletasDoTimeNaLigaActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
        });
    }

    @Override
    public int getItemCount(){
        return listaTimesDaLiga.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView posicao;
        ImageView escudo;
        TextView nomeTime;
        TextView pontuacao;
        TextView diferenca;
        TextView nomeCartoleiro;
        RelativeLayout background;

        public ViewHolder(View itemView){
            super(itemView);

            escudo = (ImageView) itemView.findViewById(R.id.atleta);
            posicao = (TextView) itemView.findViewById(R.id.posicao);
            nomeTime = (TextView) itemView.findViewById(R.id.nome_time);
            pontuacao = (TextView) itemView.findViewById(R.id.pontuacao);
            diferenca = (TextView) itemView.findViewById(R.id.diferenca);
            nomeCartoleiro = (TextView) itemView.findViewById(R.id.nome_cartoleiro);
            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);
        }

        private void setData(TimeLiga time , int backgroundColor, int position, double pontuacaoPrimeiroTime){

            Picasso.with( context )
                    .load( time.getUrlEscudoPng() )
                    .resize(140,140).centerCrop().noFade()
                    .error( R.drawable.clube )
                    .into( escudo );

            background.setBackgroundColor(backgroundColor);

            SpannableStringBuilder nomeDoTime = new SpannableStringBuilder(time.getNomeDoTime());
            nomeDoTime.setSpan(new RelativeSizeSpan(0.9f), 0, nomeDoTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nomeTime.setText(nomeDoTime);

            SpannableStringBuilder nomeDoCartoleiro = new SpannableStringBuilder(time.getNomeDoCartoleiro());
            nomeDoCartoleiro.setSpan(new RelativeSizeSpan(0.95f), 0, nomeDoCartoleiro.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nomeCartoleiro.setText(nomeDoCartoleiro);

            SpannableStringBuilder posicaoDoTime = new SpannableStringBuilder(String.valueOf(position+1));
            posicaoDoTime.setSpan(new RelativeSizeSpan(0.85f), 0, posicaoDoTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            posicao.setText(posicaoDoTime);

            if (time.getPontuacaoRodada() == null){
                pontuacao.setText("");
            } else {
                SpannableStringBuilder pontuacaoFormatada = new SpannableStringBuilder(formatoPontuacao.format(time.getPontuacaoCampeonato()));
                pontuacaoFormatada.setSpan(new RelativeSizeSpan(0.9f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                pontuacao.setText(pontuacaoFormatada);
            }

            if (position == 0){
                diferenca.setText("");
            } else {
                String diferencaCalculada = "-"+formatoPontuacao.format(pontuacaoPrimeiroTime-time.getPontuacaoCampeonato());
                SpannableStringBuilder diferencaFormatada = new SpannableStringBuilder(diferencaCalculada.replaceAll("--","-"));
                diferencaFormatada.setSpan(new RelativeSizeSpan(0.8f), 0, diferencaFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                diferenca.setText(diferencaFormatada);
            }
        }
    }
}