package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import br.com.devgeek.cartolaparciais.model.Scouts;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import br.com.devgeek.cartolaparciais.util.ClubesUtil;
import br.com.devgeek.cartolaparciais.util.PosicoesJogadoresUtil;

/**
 * Created by geovannefduarte
 */
//https://android-arsenal.com/details/1/2464
public class ParciaisAtletasDoTimeFavoritoAdapter extends RecyclerView.Adapter<ParciaisAtletasDoTimeFavoritoAdapter.ViewHolder> {

    private static String TAG = "ParciaisAtletasDoTimeFavoritoAdapter";

    private Context context;
    private DecimalFormat formatoPontuacao;
    private List<AtletasPontuados> atletasPontuados;

    public ParciaisAtletasDoTimeFavoritoAdapter(Context context, List<AtletasPontuados> atletasPontuados){
        this.context = context;
        this.atletasPontuados = atletasPontuados;
        this.formatoPontuacao = new DecimalFormat(TimeFavorito.FORMATO_PONTUACAO);
    }

    @Override
    public ParciaisAtletasDoTimeFavoritoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_parciais_atletas_do_time, parent, false);

        return new ParciaisAtletasDoTimeFavoritoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParciaisAtletasDoTimeFavoritoAdapter.ViewHolder holder, int position){
        holder.setData( atletasPontuados.get( position ) );
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

        public ViewHolder(View itemView){
            super(itemView);

            fotoDoAtleta = (ImageView) itemView.findViewById(R.id.atleta);
            escudoDoAtleta = (ImageView) itemView.findViewById(R.id.escudo_atleta);
            atletaPosicao = (TextView) itemView.findViewById(R.id.atleta_posicao);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            nomeDoAtleta = (TextView) itemView.findViewById(R.id.nome_atleta);
            scoutsContent = (LinearLayout) itemView.findViewById(R.id.atleta_scouts_content);
        }

        private void setData(AtletasPontuados atleta){

            Picasso.with( context )
                    .load( atleta.getFoto().replace("_FORMATO","_140x140") )
                    .error( R.drawable.atleta )
                    .into( fotoDoAtleta );

            nomeDoAtleta.setText(atleta.getApelido());
            atletaPosicao.setText(PosicoesJogadoresUtil.getPosicaoNome(atleta.getPosicaoId()));
            escudoDoAtleta.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId(),"60x60"));

            pontuacao.setText("-.--");
            pontuacao.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));

            if (atleta.getPontuacao() != null){

                pontuacao.setText(formatoPontuacao.format(atleta.getPontuacao()));

                if (atleta.getPontuacao() > 0){
                    pontuacao.setTextColor(ContextCompat.getColor(context, R.color.cartoletaPositiva));
                } else if (atleta.getPontuacao() < 0){
                    pontuacao.setTextColor(ContextCompat.getColor(context, R.color.cartoletaNegativa));
                }
            }

            if (atleta.getScouts().size() > 0){

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


                    Button tag = new Button(context);
                    tag.setText(scoutText);
                    //tag.setStateListAnimator(null);
                    tag.setTransformationMethod(null);
                    tag.setMinWidth(0);  tag.setMinimumWidth(0);
                    tag.setMinHeight(0); tag.setMinimumHeight(0);
                    tag.setBackgroundResource(R.drawable.scoutbutton);
                    tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tag.getLayoutParams();
                    p.setMargins(leftMargin, 0, 0, 0);
                    tag.requestLayout();

                    scoutsContent.addView(tag);
                }
            }
        }
    }
}