package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.setData( atletasPontuados.get( position ) );
    }

    @Override
    public int getItemCount(){
        return atletasPontuados.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pontuacao;
        TextView cartoletas;
        TextView nomeDoAtleta;
        TextView atletaPosicao;
        ImageView fotoDoAtleta;
        ImageView escudoDoAtleta;
        LinearLayout scoutsContent;

        public ViewHolder(View itemView){
            super(itemView);

            fotoDoAtleta = (ImageView) itemView.findViewById(R.id.atleta);
            escudoDoAtleta = (ImageView) itemView.findViewById(R.id.escudo_atleta);
            atletaPosicao = (TextView) itemView.findViewById(R.id.atleta_posicao);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            cartoletas = (TextView) itemView.findViewById(R.id.atleta_cartoletas);
            nomeDoAtleta = (TextView) itemView.findViewById(R.id.nome_atleta);
            scoutsContent = (LinearLayout) itemView.findViewById(R.id.atleta_scouts_content);
        }

        private void setData(AtletasPontuados atleta){

            if (userGloboIsLogged && atleta.getFoto() != null){

                Picasso.with( context ).load( atleta.getFoto().replace("_FORMATO","_140x140") ).into( fotoDoAtleta );

            } else {

                Picasso.with( context ).load( R.drawable.atleta ).into( fotoDoAtleta );
            }

            nomeDoAtleta.setText(atleta.getApelido());
            atletaPosicao.setText(PosicoesJogadoresUtil.getPosicaoNome(atleta.getPosicaoId()));
            escudoDoAtleta.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));

            pontuacao.setText("-.--");
            pontuacao.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));

            if (atleta.getPontuacao() != null && (atleta.getPosicaoId() != PosicoesJogadoresUtil.TECNICO || (atleta.getPosicaoId() == PosicoesJogadoresUtil.TECNICO && atleta.getPontuacao() != 0))){

                pontuacao.setText(formatoPontuacao.format(atleta.getPontuacao()));

                if (atleta.getCartoletas() == null){
                    if (atleta.getPontuacao() > 0){
                        pontuacao.setTextColor(ContextCompat.getColor(context, R.color.cartoletaPositiva));
                    } else if (atleta.getPontuacao() < 0){
                        pontuacao.setTextColor(ContextCompat.getColor(context, R.color.cartoletaNegativa));
                    }
                }
            }

            if (scoutsContent.getChildCount() > 0) scoutsContent.removeAllViews();

            if (atleta.getCartoletas() == null && atleta.getScouts().size() == 0){

                cartoletas.setVisibility(View.GONE);
                cartoletas.setVisibility(View.INVISIBLE);

                scoutsContent.setVisibility(View.GONE);
                scoutsContent.setVisibility(View.INVISIBLE);

            } else {

                cartoletas.setVisibility(View.VISIBLE);
                scoutsContent.setVisibility(View.VISIBLE);
            }

            if (atleta.getCartoletas() != null){

                cartoletas.setText("C$ "+formatoCartoletas.format(atleta.getCartoletas()));
                if (atleta.getCartoletas() > 0){
                    cartoletas.setTextColor(ContextCompat.getColor(context, R.color.cartoletaPositiva));
                } else {
                    cartoletas.setTextColor(ContextCompat.getColor(context, R.color.cartoletaNegativa));
                }

                scoutsContent.setVisibility(View.GONE);
                scoutsContent.setVisibility(View.INVISIBLE);
            }

            if (atleta.getCartoletas() == null && atleta.getScouts().size() > 0){

                cartoletas.setVisibility(View.GONE);
                cartoletas.setVisibility(View.INVISIBLE);

                Resources resources = context.getResources();
                int leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, resources.getDisplayMetrics());

                for (Scouts scout : atleta.getScouts()){

                    SpannableStringBuilder scoutText;

                    if (scout.getQuantidade() > 1){
                        scoutText = new SpannableStringBuilder(scout.getScout()+scout.getQuantidade());

                        scoutText.setSpan(new SuperscriptSpan(), scout.getScout().length(), scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        scoutText.setSpan(new RelativeSizeSpan(0.6f), scout.getScout().length(), scoutText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    } else {
                        scoutText = new SpannableStringBuilder(scout.getScout());
                    }



                    TextView tag = new TextView(context);
                    tag.setText(scoutText);
                    tag.setTransformationMethod(null);
                    tag.setMinWidth(0);  tag.setMinimumWidth(0);
                    tag.setMinHeight(0); tag.setMinimumHeight(0);
                    tag.setBackgroundResource(R.drawable.scoutbutton);
                    tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tag.getLayoutParams();
                    p.setMargins(leftMargin, 0, 0, 0);
                    tag.requestLayout();

                    tag.setOnClickListener(v -> {
                        final Toast toast = Toast.makeText(context, getLegendaDosScouts(scout.getScout(), scout.getQuantidade()), Toast.LENGTH_SHORT); toast.show();
                        new Handler().postDelayed(() -> toast.cancel(), 1000);
                    } );

                    scoutsContent.addView(tag);
                }
            }
        }
    }
}