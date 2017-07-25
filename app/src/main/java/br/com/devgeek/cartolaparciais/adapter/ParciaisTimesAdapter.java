package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.ParciaisTimes;
import io.realm.Realm;

/**
 * Created by geovannefduarte
 */

public class ParciaisTimesAdapter extends Fragment {

    private static Context applicationContext;
    private static Typeface montserratBold = null;
    private static Typeface montserratRegular = null;

    private static final String TAG = "ParciaisTimesAdapter";
    private static final Map<String, Integer> backgroundColor = new HashMap<String, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

//        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
//
//        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        return recyclerView;
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView posicao;
        public ImageView escudo;
        public TextView nomeTime;
        public TextView pontuacao;
        public TextView cartoletas;
        public TextView nomeCartoleiro;
        public RelativeLayout background;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent){

            super(inflater.inflate(R.layout.fragment_parciaistimes, parent, false));

            escudo = (ImageView) itemView.findViewById(R.id.atleta);
            posicao = (TextView) itemView.findViewById(R.id.posicao);
            nomeTime = (TextView) itemView.findViewById(R.id.nome_time);
            pontuacao = (TextView) itemView.findViewById(R.id.atleta_pontuacao);
            cartoletas = (TextView) itemView.findViewById(R.id.cartoletas);
            nomeCartoleiro = (TextView) itemView.findViewById(R.id.nome_cartoleiro);
            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        // Set numbers of List in RecyclerView.
//        private static final int LENGTH = 18;
//        private final String[] mPlaces;
//        private final String[] mPlaceDesc;
//        private final Drawable[] mPlaceAvators;

        List<ParciaisTimes> listaTimes = new ArrayList<ParciaisTimes>();
        DecimalFormat formatoPontuacao = new DecimalFormat(ParciaisTimes.FORMATO_PONTUACAO);
        DecimalFormat formatoCartoletas = new DecimalFormat(ParciaisTimes.FORMATO_CARTOLETAS);

        public ContentAdapter(Context context){

            applicationContext = context.getApplicationContext();
            montserratBold = Typeface.createFromAsset(applicationContext.getAssets(), "fonts/Montserrat-Medium.ttf");
            montserratRegular = Typeface.createFromAsset(applicationContext.getAssets(), "fonts/Montserrat-Regular.ttf");

            backgroundColor.put("even", ContextCompat.getColor(context, R.color.bgColorEven));
            backgroundColor.put("odd", ContextCompat.getColor(context, R.color.bgColorOdd));


            Realm realm = null;

            try {

                realm = Realm.getDefaultInstance();
                listaTimes = realm.where(ParciaisTimes.class).findAllSorted("posicao");

            } catch (Exception e){

                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (realm != null) realm.close();
            }




//            listaTimes.add(new ParciaisTimes("auto-pecas-santos-ec",   "auto-pecas-santos-ec",  1, "Auto Pecas Santos EC",  "P.H",              0.0, 0.0));
//            listaTimes.add(new ParciaisTimes("caiaponiaduartefc", 	    "caiaponiaduartefc", 	2, "CaiapôniaDuarteFC", 	"Genésio Duarte", 	0.0, 0.0));
//            listaTimes.add(new ParciaisTimes("sport-club-azanki", 	    "sport-club-azanki", 	3, "Sport¨Club Azanki", 	"Neto Azanki", 		0.0, 0.0));
//            listaTimes.add(new ParciaisTimes("dj-sportclub", 		    "dj-sportclub", 		4, "DJ SportClub", 			"Djonnathan Duarte",0.0, 0.0));
//            listaTimes.add(new ParciaisTimes("arkenstone-fc", 		    "arkenstone-fc", 		5, "Arkenstone-fc", 		"Geovanne Duarte", 	0.0, 0.0));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){

            holder.nomeTime.setTypeface(montserratBold);
            holder.pontuacao.setTypeface(montserratBold);
            holder.cartoletas.setTypeface(montserratRegular);
            holder.nomeCartoleiro.setTypeface(montserratRegular);

            if ((position % 2) == 0){
                holder.background.setBackgroundColor(backgroundColor.get("even"));
            }  else {
                holder.background.setBackgroundColor(backgroundColor.get("odd"));
            }


            holder.posicao.setText(String.valueOf(position+1));
            holder.nomeTime.setText(listaTimes.get(position).getNomeTime());
            holder.nomeCartoleiro.setText(listaTimes.get(position).getNomeCartoleiro());
            holder.pontuacao.setText(formatoPontuacao.format(listaTimes.get(position).getPontuacao()));
            holder.cartoletas.setText(formatoCartoletas.format(listaTimes.get(position).getVariacaoCartoletas()));

            if (listaTimes.get(position).getSlug().equals("arkenstone-fc")){
                holder.escudo.setImageResource(R.drawable.arkenstone_fc);
            } else if (listaTimes.get(position).getSlug().equals("dj-sportclub")){
                holder.escudo.setImageResource(R.drawable.dj_soccer_club);
            } else if (listaTimes.get(position).getSlug().equals("sport-club-azanki")){
                holder.escudo.setImageResource(R.drawable.sport_club_azanki);
            } else if (listaTimes.get(position).getSlug().equals("caiaponiaduartefc")){
                holder.escudo.setImageResource(R.drawable.caiaponiaduarte_fc);
            } else if (listaTimes.get(position).getSlug().equals("auto-pecas-santos-ec")){
                holder.escudo.setImageResource(R.drawable.auto_pecas_santos_ec);
            }
        }

        @Override
        public int getItemCount(){
            return listaTimes.size();
        }

//        public void update(){
//
//            Realm realm = null;
//
//            try {
//
//                realm = Realm.getDefaultInstance();
//                listaTimes = realm.where(ParciaisTimes.class).findAllSorted("posicao");
//
//            } catch (Exception e){
//                e.printStackTrace();
//            } finally {
//                realm.close();
//            }
//
//            notifyDataSetChanged();
//        }
    }
}