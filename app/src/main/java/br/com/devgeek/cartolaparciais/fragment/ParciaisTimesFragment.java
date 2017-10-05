package br.com.devgeek.cartolaparciais.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisTimesFavoritosAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.atualizarMercadoAndLigasAndPartidas;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ParciaisTimesFragment extends Fragment {

    private static final String TAG = "ParciaisTimesFragment";

    private Realm realm;
    private ApiServiceImpl apiService;
    private ParciaisTimesFavoritosAdapter adapter;
    private RealmResults<TimeFavorito> listaTimesFavoritos;
    private SwipeRefreshLayout refreshListaTimesFavoritos;
    private RealmChangeListener listaTimesFavoritosListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(listaTimesFavoritos);
            adapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        realm = Realm.getDefaultInstance();
        View view  = inflater.inflate(R.layout.fragment_parciaistimes, container, false);


        Sort[] sortOrder = { Sort.DESCENDING, Sort.DESCENDING, Sort.ASCENDING };
        String[] sortColumns = { "pontuacao", "variacaoCartoletas", "nomeDoTime" };
        listaTimesFavoritos = realm.where(TimeFavorito.class).equalTo("timeFavorito", true).findAllSortedAsync(sortColumns, sortOrder);


        refreshListaTimesFavoritos = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaTimesFavoritos);
        refreshListaTimesFavoritos.setOnRefreshListener(() -> atualizarDadosSeHouverInternet());


        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTimesFavoritos);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ParciaisTimesFavoritosAdapter( getActivity() , listaTimesFavoritos );
        recyclerView.setAdapter( adapter );


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados(true);
        listaTimesFavoritos.addChangeListener(listaTimesFavoritosListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        listaTimesFavoritos.removeChangeListener(listaTimesFavoritosListener);
    }

    private void atualizarDados(boolean checkTime){

        atualizarMercadoAndLigasAndPartidas(apiService, getContext(), checkTime);
        new Handler().postDelayed(() -> refreshListaTimesFavoritos.setRefreshing(false), 750);
    }

    private void atualizarDadosSeHouverInternet(){
        if (isNetworkAvailable(getActivity().getApplicationContext())){
            atualizarDados(false);
        } else {
            refreshListaTimesFavoritos.setRefreshing(false);
            Snackbar.make( getActivity().getWindow().getDecorView().findViewById( android.R.id.content ), "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
        }
    }
}