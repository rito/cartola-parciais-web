package br.com.devgeek.cartolaparciais.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisTimesFavoritosAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.realm.Realm;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ParciaisTimesFragment extends Fragment {

    private static final String TAG = "ParciaisTimesFragment";

    private ApiServiceImpl apiService;
    private ParciaisTimesFavoritosAdapter adapter;
    private SwipeRefreshLayout refreshListaTimesFavoritos;
    private List<TimeFavorito> listaTimesFavoritos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        View view  = inflater.inflate(R.layout.fragment_parciaistimes, container, false);

        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTimesFavoritos);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( getActivity() , mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new ParciaisTimesFavoritosAdapter( getActivity() , listaTimesFavoritos );
        recyclerView.setAdapter( adapter );


        Realm realm = null;
        // Buscar times favoritos
        try {

            realm = Realm.getDefaultInstance();

            List<TimeFavorito> timesFavoritos = realm.copyFromRealm(realm.where(TimeFavorito.class).findAll());

            if (timesFavoritos != null && timesFavoritos.size() > 0){

                Collections.sort(timesFavoritos, (TimeFavorito t1, TimeFavorito t2) -> { // ordem inversa

                    if (t1.getPontuacao() != null && t2.getPontuacao() != null){
                        if (t1.getPontuacao() < t2.getPontuacao()) return 1;
                        if (t1.getPontuacao() > t2.getPontuacao()) return -1;
                    }

                    if (t1.getVariacaoCartoletas() != null && t2.getVariacaoCartoletas() != null){
                        if (t1.getVariacaoCartoletas() < t2.getVariacaoCartoletas()) return 1;
                        if (t1.getVariacaoCartoletas() > t2.getVariacaoCartoletas()) return -1;
                    }

                    return t1.getNomeDoTime().compareTo(t2.getNomeDoTime());
                });

                listaTimesFavoritos.clear();
                listaTimesFavoritos.addAll( timesFavoritos );
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e){

            e.printStackTrace();

        } finally {
            if (realm != null) realm.close();
            atualizarDados();
        }


        refreshListaTimesFavoritos = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaTimesFavoritos);
        refreshListaTimesFavoritos.setOnRefreshListener(() -> atualizarDados());

        return view;
    }

    private void atualizarDados(){

        apiService.verificarMercadoStatus();
        apiService.buscarAtletasPontuados();
        new Handler().postDelayed(() -> refreshListaTimesFavoritos.setRefreshing(false), 850);
    }
}