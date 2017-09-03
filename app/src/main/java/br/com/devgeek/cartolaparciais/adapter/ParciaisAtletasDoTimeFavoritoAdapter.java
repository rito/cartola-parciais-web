package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ParciaisAtletasDoTimeFavoritoAdapter extends RecyclerView.Adapter<ParciaisAtletasDoTimeFavoritoAdapter.ViewHolder> {

    private static String TAG = "ParciaisAtletasDoTimeFavoritoAdapter";

    private Context context;
    private DecimalFormat formatoPontuacao;
    private List<AtletasPontuados> atletasPontuados;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pontuacao;
        TextView nomeDoAtleta;
        TextView atletaPosicao;
        ImageView fotoDoAtleta;
        ImageView escudoDoAtleta;
        TextView scouts;

        public ViewHolder(View itemView){
            super(itemView);

            fotoDoAtleta = (ImageView) itemView.findViewById(R.id.atleta);
            escudoDoAtleta = (ImageView) itemView.findViewById(R.id.escudo_atleta);
            atletaPosicao = (TextView) itemView.findViewById(R.id.atleta_posicao);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            nomeDoAtleta = (TextView) itemView.findViewById(R.id.nome_atleta);
            scouts = (TextView) itemView.findViewById(R.id.atleta_scouts);
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

                String htmlScouts = "";
                for (Scouts scout : atleta.getScouts()){

                    if (!htmlScouts.equals("")) htmlScouts += " ";
                    htmlScouts += scout.getScout();

                    if (scout.getQuantidade() > 1){
                        htmlScouts += "<sup>"+scout.getQuantidade()+"</sup>";
                    }
                }

                scouts.setText(Html.fromHtml(htmlScouts));
            } else {
                scouts.setText("");
            }
        }
    }

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
}