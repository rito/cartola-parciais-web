package br.com.devgeek.cartolaparciais.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.ParciaisAtletasDoTimeNaLigaActivity;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.TimeLiga;
import br.com.devgeek.cartolaparciais.parcelable.ParciaisAtletasDoTimeParcelable;
import br.com.devgeek.cartolaparciais.util.PosicoesJogadoresUtil;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.padLeft;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.parseAndSortAtletasPontuados;

/**
 * Created by geovannefduarte on 24/09/17.
 */
public class LigasMesAdapter extends RecyclerView.Adapter<LigasMesAdapter.ViewHolder> {

    private static final String TAG = "LigasMesAdapter";

    private Gson gson;
    private Context context;
    private DecimalFormat formatoPontuacao;
    private List<TimeLiga> listaTimesDaLiga;


    public LigasMesAdapter(Context context, List<TimeLiga> listaTimesDaLiga){
        this.context = context;
        update(listaTimesDaLiga);
        this.formatoPontuacao = new DecimalFormat(TimeLiga.FORMATO_PONTUACAO);
        gson = new Gson();
    }

    public void update(List<TimeLiga> listaTimesDaLiga){
        this.listaTimesDaLiga = listaTimesDaLiga;
    }

    @Override
    public LigasMesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_ligas_rodada, parent, false);

        return new LigasMesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LigasMesAdapter.ViewHolder holder, int position){

        int backgroundColor = ContextCompat.getColor(context, R.color.bgColorUser);

        if (!listaTimesDaLiga.get( position ).isTimeDoUsuario()){
            if ((position % 2) == 0){
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorEven);
            } else {
                backgroundColor = ContextCompat.getColor(context, R.color.bgColorOdd);
            }
        }

        double pontuacaoParciaisPrimeiroTime = 0;
        if (listaTimesDaLiga.get( 0 ).getPontuacaoMes() != null) pontuacaoParciaisPrimeiroTime = listaTimesDaLiga.get( 0 ).getPontuacaoMes();
        holder.setData( listaTimesDaLiga.get( position ), backgroundColor, position, listaTimesDaLiga.get( 0 ).getPontuacaoMes(), pontuacaoParciaisPrimeiroTime);

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

        int margin1dp, jogadoresPontuados;

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

        private void setData(TimeLiga time , int backgroundColor, int position, double pontuacaoPrimeiroTime, double pontuacaoParciaisPrimeiroTime){

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

            if (time.getAtletas() != null && (time.getVariacaoCartoletas() == null || time.getVariacaoCartoletas() == 0)){

                jogadoresPontuados = 0;
                for (AtletasPontuados atleta : parseAndSortAtletasPontuados(gson, time.getAtletas())){
                    if (atleta.getPontuacao() != null && (atleta.getPosicaoId() != PosicoesJogadoresUtil.TECNICO || (atleta.getPosicaoId() == PosicoesJogadoresUtil.TECNICO && atleta.getPontuacao() != 0))){
                        jogadoresPontuados++;
                    }
                }

                Spanned concatenated;
                SpannableStringBuilder pontuacaoFormatada = new SpannableStringBuilder(padLeft(formatoPontuacao.format(time.getPontuacaoMes()),8));
                pontuacaoFormatada.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                pontuacaoFormatada.setSpan(new RelativeSizeSpan(0.9f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // https://stackoverflow.com/q/6612316

                if (time.getVariacaoCartoletas() == null || time.getVariacaoCartoletas() == 0){

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

                if (position == 0){
                    diferenca.setText("");
                } else {
                    String diferencaCalculada = "-"+formatoPontuacao.format(pontuacaoParciaisPrimeiroTime-time.getPontuacaoMes());
                    SpannableStringBuilder diferencaFormatada = new SpannableStringBuilder(diferencaCalculada.replaceAll("--","-"));
                    diferencaFormatada.setSpan(new RelativeSizeSpan(0.8f), 0, diferencaFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diferenca.setText(diferencaFormatada);
                }

            } else {

                if (time.getPontuacaoMes() == null){
                    pontuacao.setText("");
                } else {
                    SpannableStringBuilder pontuacaoFormatada = new SpannableStringBuilder(formatoPontuacao.format(time.getPontuacaoMes()));
                    pontuacaoFormatada.setSpan(new RelativeSizeSpan(0.9f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pontuacao.setText(pontuacaoFormatada);
                }

                if (position == 0){
                    diferenca.setText("");
                } else {
                    String diferencaCalculada = "-"+formatoPontuacao.format(pontuacaoPrimeiroTime-time.getPontuacaoMes());
                    SpannableStringBuilder diferencaFormatada = new SpannableStringBuilder(diferencaCalculada.replaceAll("--","-"));
                    diferencaFormatada.setSpan(new RelativeSizeSpan(0.8f), 0, diferencaFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    diferenca.setText(diferencaFormatada);
                }
            }
        }
    }
}