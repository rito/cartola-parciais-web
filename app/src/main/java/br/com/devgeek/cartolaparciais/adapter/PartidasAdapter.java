package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.content.res.Resources;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.Partida;
import io.realm.RealmResults;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.logErrorOnConsole;
import static br.com.devgeek.cartolaparciais.util.ClubesUtil.getClubeAbreviada;
import static br.com.devgeek.cartolaparciais.util.ClubesUtil.getClubeEscudo;

/**
 * Created by geovannefduarte on 18/09/17.
 */
public class PartidasAdapter extends RecyclerView.Adapter<PartidasAdapter.ViewHolder> {

    private static final String TAG = "PartidasAdapter";

    private Context context;
    private RealmResults<Partida> partidas;
    private SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat layoutFormat = new SimpleDateFormat("dd/MM/yyyy' as 'HH'h'mm");
    //private SimpleDateFormat layoutFormat = new SimpleDateFormat("EEE' - 'dd/MM/yyyy' as 'HH'h'mm");


    public PartidasAdapter(Context context, RealmResults<Partida> partidas){
        this.context = context;
        update(partidas);
    }


    public void update(RealmResults<Partida> partidas){
        this.partidas = partidas;
    }

    @Override
    public PartidasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_jogos, parent, false);
        return new PartidasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidasAdapter.ViewHolder holder, int position){

        holder.setData( holder.itemView, partidas.get( position ) );
    }

    @Override
    public int getItemCount(){
        return partidas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout jogo_title;
        TextView rodada;


        RelativeLayout jogo_background;
        TextView jogo_hora;
        TextView jogo_local;

        ImageView clube_casa;
        TextView sigla_casa;
        TextView placar_casa;

        ImageView clube_visitante;
        TextView sigla_visitante;
        TextView placar_visitante;

        public ViewHolder(View itemView){
            super(itemView);

            jogo_title = (RelativeLayout) itemView.findViewById(R.id.jogo_dia);
            rodada = (TextView) itemView.findViewById(R.id.jogo_dia_completo);


            jogo_background = (RelativeLayout) itemView.findViewById(R.id.jogo_background);
            jogo_hora = (TextView) itemView.findViewById(R.id.jogo_hora);
            jogo_local = (TextView) itemView.findViewById(R.id.jogo_local);

            clube_casa = (ImageView) itemView.findViewById(R.id.clube_casa);
            sigla_casa = (TextView) itemView.findViewById(R.id.sigla_casa);
            placar_casa = (TextView) itemView.findViewById(R.id.placar_casa);

            clube_visitante = (ImageView) itemView.findViewById(R.id.clube_visitante);
            sigla_visitante = (TextView) itemView.findViewById(R.id.sigla_visitante);
            placar_visitante = (TextView) itemView.findViewById(R.id.placar_visitante);

        }

        private void setData(View holderView, Partida partida){

            Resources resources = context.getResources();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (partida.getTituloRodada() != null){

                jogo_background.setVisibility(View.GONE);
                jogo_background.setVisibility(View.INVISIBLE);
                jogo_title.setVisibility(View.VISIBLE);

                rodada.setText(partida.getTituloRodada());

                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);

            } else {

                jogo_title.setVisibility(View.GONE);
                jogo_title.setVisibility(View.INVISIBLE);
                jogo_background.setVisibility(View.VISIBLE);

                String horaPartidaFormatada = "";
                try {
                    horaPartidaFormatada = layoutFormat.format(apiFormat.parse(partida.getDataPartida()));
                } catch (ParseException e){
                    logErrorOnConsole(TAG, "Falha ao formatar horario da partida() -> "+e.getMessage(), e);
                }

                SpannableStringBuilder horaPartida = new SpannableStringBuilder(horaPartidaFormatada);
                horaPartida.setSpan(new RelativeSizeSpan(0.6f), 0, horaPartida.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                jogo_hora.setText(horaPartida);

                SpannableStringBuilder localPartida = new SpannableStringBuilder(partida.getLocal());
                localPartida.setSpan(new RelativeSizeSpan(0.7f), 0, localPartida.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                jogo_local.setText(localPartida);

                if (partida.getPlacarTimeCasa() == null){
                    placar_casa.setText("");
                } else {
                    placar_casa.setText(String.valueOf(partida.getPlacarTimeCasa()));
                }

                sigla_casa.setText(getClubeAbreviada(partida.getIdTimeCasa()));
                clube_casa.setImageResource(getClubeEscudo(partida.getIdTimeCasa()));

                if (partida.getPlacarTimeVisitante() == null){
                    placar_visitante.setText("");
                } else {
                    placar_visitante.setText(String.valueOf(partida.getPlacarTimeVisitante()));
                }

                sigla_visitante.setText(getClubeAbreviada(partida.getIdTimeVisitante()));
                clube_visitante.setImageResource(getClubeEscudo(partida.getIdTimeVisitante()));

                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, resources.getDisplayMetrics());
                params.height = height;
                holderView.setLayoutParams(params);
            }
        }
    }
}