package br.com.devgeek.cartolaparciais.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.api.model.ApiTime;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.realm.Realm;

/**
 * Created by geovannefduarte
 */

public class BuscarTimesAdapter extends RecyclerView.Adapter<BuscarTimesAdapter.ViewHolder> {

    private Context context;
    private List<ApiTime> listaTimes;
    private final Map<String, Integer> backgroundColor = new HashMap<String, Integer>();

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView escudo;
        TextView nomeTime;
        TextView nomeCartoleiro;

        LikeButton favorito;

        RelativeLayout background;

        public ViewHolder(View itemView){
            super(itemView);

            escudo = (ImageView) itemView.findViewById(R.id.atleta);
            nomeTime = (TextView) itemView.findViewById(R.id.nome_time);
            nomeCartoleiro = (TextView) itemView.findViewById(R.id.nome_cartoleiro);

            favorito = (LikeButton) itemView.findViewById(R.id.favorito);

            background = (RelativeLayout) itemView.findViewById(R.id.parciais_background);
        }

        private void setData( ApiTime time , int backgroundColor ){

            Picasso.with( context )
                    .load( time.getUrlEscudoPng() )
                    .into( escudo );

            nomeTime.setText(time.getNomeDoTime());
            nomeCartoleiro.setText(time.getNomeDoCartoleiro());
            background.setBackgroundColor(backgroundColor);

            favorito.setOnLikeListener(new OnLikeListener(){

                Realm realm = null;

                @Override
                public void liked(LikeButton likeButton){

                    try {

                        realm =  Realm.getDefaultInstance();

                        realm.executeTransaction(new Realm.Transaction(){
                            @Override
                            public void execute(Realm realm){

                                realm.copyToRealmOrUpdate(new TimeFavorito(time));
                            }
                        });

                        Toast.makeText(context, time.getNomeDoTime()+" adicionado a lista de favoritos", Toast.LENGTH_SHORT).show();


                    } catch (Exception e){

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    } finally {
                        if (realm != null) realm.close();
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton){

                    try {

                        realm =  Realm.getDefaultInstance();

                        realm.executeTransaction(new Realm.Transaction(){
                            @Override
                            public void execute(Realm realm){

                                TimeFavorito timeFavorito = realm.where(TimeFavorito.class).equalTo("timeId", time.getTimeId()).findFirst();
                                timeFavorito.deleteFromRealm();
                            }
                        });

                        Toast.makeText(context, time.getNomeDoTime()+" removido da lista de favoritos", Toast.LENGTH_SHORT).show();


                    } catch (Exception e){

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    } finally {
                        if (realm != null) realm.close();
                    }
                }
            });

            Realm realm = null;

            try {


                realm =  Realm.getDefaultInstance();
                TimeFavorito timeFavorito = realm.where(TimeFavorito.class).equalTo("timeId", time.getTimeId()).findFirst();

                if (timeFavorito != null){

                    favorito.setLiked(true);

                } else {

                    favorito.setLiked(false);
                }

            } catch (Exception e){

                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } finally {
                if (realm != null) realm.close();
            }
        }
    }

    public BuscarTimesAdapter(Context context, List<ApiTime> listaTimes){
        this.context = context;
        this.listaTimes = listaTimes;
    }

    @Override
    public BuscarTimesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_buscartimes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuscarTimesAdapter.ViewHolder holder, int position){

        int backgroundColor = ContextCompat.getColor(context, R.color.bgColorOdd);

        if ((position % 2) == 0){
            backgroundColor = ContextCompat.getColor(context, R.color.bgColorEven);
        }

        holder.setData( listaTimes.get( position ), backgroundColor );
    }

    @Override
    public int getItemCount(){
        return listaTimes.size();
    }
}