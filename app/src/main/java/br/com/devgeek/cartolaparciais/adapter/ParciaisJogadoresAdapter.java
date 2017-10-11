package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.Scouts;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.util.ClubesUtil;
import br.com.devgeek.cartolaparciais.util.PosicoesJogadoresUtil;
import io.realm.RealmResults;

import static br.com.devgeek.cartolaparciais.model.Scouts.getLegendaDosScouts;

/**
 * Created by geovannefduarte
 */

public class ParciaisJogadoresAdapter extends RecyclerView.Adapter<ParciaisJogadoresAdapter.ViewHolder> {

    private static final String TAG = "ParciaisJogadoresAdapte";
    private static Toast toast = null;
    private Context context;
    private boolean userGloboIsLogged;
    private DecimalFormat formatoPontuacao;
    private DecimalFormat formatoCartoletas;
    private RealmResults<AtletasPontuados> atletasPontuados;

    public ParciaisJogadoresAdapter(Context context, RealmResults<AtletasPontuados> atletasPontuados, boolean userGloboIsLogged){
        this.context = context;
        update(atletasPontuados);
        this.userGloboIsLogged = userGloboIsLogged;
        this.formatoPontuacao = new DecimalFormat(TimeFavorito.FORMATO_PONTUACAO);
        this.formatoCartoletas = new DecimalFormat(TimeFavorito.FORMATO_CARTOLETAS);
    }

    public void update(RealmResults<AtletasPontuados> atletasPontuados){
        this.atletasPontuados = atletasPontuados;
    }

    @Override
    public ParciaisJogadoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_parciais_atletas_do_time, parent, false);
        return new ParciaisJogadoresAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParciaisJogadoresAdapter.ViewHolder holder, int position){
        holder.setData( atletasPontuados.get( position ), position );
    }

    @Override
    public int getItemCount(){
        return atletasPontuados.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pontuacao;
        TextView nomeDoAtleta;
        TextView atletaPosicao;
        ImageView fotoDoAtleta;
        ImageView escudoDoAtleta;
        LinearLayout scoutsContent;
        RelativeLayout background;

        Resources resources;
        int margin1dp;

        public ViewHolder(View itemView){
            super(itemView);

            fotoDoAtleta = (ImageView) itemView.findViewById(R.id.atleta);
            escudoDoAtleta = (ImageView) itemView.findViewById(R.id.escudo_atleta);
            atletaPosicao = (TextView) itemView.findViewById(R.id.atleta_posicao);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            nomeDoAtleta = (TextView) itemView.findViewById(R.id.nome_atleta);
            scoutsContent = (LinearLayout) itemView.findViewById(R.id.atleta_scouts_content);
            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);

            resources = context.getResources();
            margin1dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
        }

        private void setData(AtletasPontuados atleta, int position){

            ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) background.getLayoutParams();
            if (position == 0){ margins.setMargins(margin1dp, margin1dp, margin1dp, margin1dp); }
            else { margins.setMargins(margin1dp, 0, margin1dp, margin1dp); }
            background.requestLayout();

            if (userGloboIsLogged && atleta.getFoto() != null){

                Picasso.with( context ).load( atleta.getFoto().replace("_FORMATO","_140x140") ).noFade().into( fotoDoAtleta );
                escudoDoAtleta.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));

            } else {

                Picasso.with( context ).load( R.drawable.atleta ).noFade().into( fotoDoAtleta );
            }

            SpannableStringBuilder nomeAtleta = new SpannableStringBuilder(atleta.getApelido());
            nomeAtleta.setSpan(new RelativeSizeSpan(0.9f), 0, nomeAtleta.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nomeDoAtleta.setText(nomeAtleta);

            SpannableStringBuilder posicaoNome = new SpannableStringBuilder(PosicoesJogadoresUtil.getPosicaoNome(atleta.getPosicaoId()));
            posicaoNome.setSpan(new RelativeSizeSpan(0.95f), 0, posicaoNome.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            atletaPosicao.setText(posicaoNome);

            pontuacao.setText("-.--");
            pontuacao.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));

            if (atleta.getPontuacao() != null && (atleta.getPosicaoId() != PosicoesJogadoresUtil.TECNICO || (atleta.getPosicaoId() == PosicoesJogadoresUtil.TECNICO && atleta.getPontuacao() != 0))){

                Spanned concatenated;
                SpannableStringBuilder pontuacaoFormatada = new SpannableStringBuilder(formatoPontuacao.format(atleta.getPontuacao()));
                pontuacaoFormatada.setSpan(new RelativeSizeSpan(1.2f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (atleta.getPontuacao() > 0){
                    pontuacaoFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaPositiva)), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else if (atleta.getPontuacao() < 0){
                    pontuacaoFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaNegativa)), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                if (atleta.getCartoletas() != null){

                    SpannableStringBuilder cartoletasFormatada = new SpannableStringBuilder("C$ "+formatoCartoletas.format(atleta.getCartoletas()));
                    cartoletasFormatada.setSpan(new RelativeSizeSpan(0.65f), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cartoletasFormatada.setSpan(new RelativeSizeSpan(0.95f), 3, cartoletasFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (atleta.getCartoletas() > 0){
                        cartoletasFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaPositiva)), 0, cartoletasFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    } else if (atleta.getCartoletas() < 0){
                        cartoletasFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaNegativa)), 0, cartoletasFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }

                    SpannableStringBuilder separador = new SpannableStringBuilder(" \u2022 ");
                    separador.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.textDark)), 0, separador.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    concatenated = (Spanned) TextUtils.concat(cartoletasFormatada,separador,pontuacaoFormatada);

                } else {
                    concatenated = (Spanned) TextUtils.concat(pontuacaoFormatada);
                }

                SpannableStringBuilder result = new SpannableStringBuilder(concatenated);
                pontuacao.setText(result, TextView.BufferType.SPANNABLE);
            }

            if (scoutsContent.getChildCount() > 0) scoutsContent.removeAllViews();

            if (atleta.getCartoletas() == null && atleta.getScouts().size() > 0){

                //Resources resources = context.getResources();
                //int leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, resources.getDisplayMetrics());

                for (Scouts scout : atleta.getScouts()){

                    SpannableStringBuilder scoutText;

                    if (scout.getQuantidade() > 1){
                        scoutText = new SpannableStringBuilder(scout.getScout()+scout.getQuantidade());

                        scoutText.setSpan(new SuperscriptSpan(), scout.getScout().length(), scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        scoutText.setSpan(new RelativeSizeSpan(0.85f), 0, scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        scoutText.setSpan(new RelativeSizeSpan(0.75f), scout.getScout().length(), scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    } else {
                        scoutText = new SpannableStringBuilder(scout.getScout());
                        scoutText.setSpan(new RelativeSizeSpan(0.85f), 0, scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }



                    TextView tag = new TextView(context);
                    tag.setText(scoutText);
                    tag.setTransformationMethod(null);
                    tag.setMinWidth(0);  tag.setMinimumWidth(0);
                    tag.setMinHeight(0); tag.setMinimumHeight(0);
                    tag.setBackgroundResource(R.drawable.scoutbutton);
                    tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tag.getLayoutParams();
                    p.setMargins(0, 0, 0, 0);
                    tag.requestLayout();

                    tag.setOnClickListener(v -> {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(context, getLegendaDosScouts(scout.getScout(), scout.getQuantidade()), Toast.LENGTH_SHORT); toast.show();
                    } );

                    scoutsContent.addView(tag);
                }
            }
        }
    }
}