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
import br.com.devgeek.cartolaparciais.adapter.PartidasAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.Partida;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;

/**
 * Created by geovannefduarte on 18/09/17.
 */
public class PartidasFragment extends Fragment {

    private static final String TAG = "PartidasFragment";

    private Realm realm;
    private ApiServiceImpl apiService;

    private RealmResults<Partida> partidas;
    private SwipeRefreshLayout refreshPartidas;

    private PartidasAdapter adapter;
    private RecyclerView recyclerView;
    private RealmChangeListener partidasListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(partidas);
            adapter.notifyDataSetChanged();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);


        Sort[] sortOrder = { Sort.DESCENDING, Sort.DESCENDING, Sort.ASCENDING, Sort.ASCENDING };
        String[] sortColumns = { "rodada", "tituloRodada", "dataPartida", "local" };
        partidas = realm.where(Partida.class).findAllSortedAsync(sortColumns, sortOrder);
//        partidas = realm.where(Partida.class).isNotNull( "tituloRodada" ).findAllAsync();


        refreshPartidas = (SwipeRefreshLayout) view.findViewById(R.id.refreshPartidas);
        refreshPartidas.setOnRefreshListener(() -> atualizarDadosSeHouverInternet());


        // Configurar recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.listaDePartidas);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new PartidasAdapter( getActivity() , partidas );
        recyclerView.setAdapter( adapter );

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados(true);
        partidas.addChangeListener(partidasListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        partidas.removeChangeListener(partidasListener);
    }

    private void atualizarDados(boolean checkTime){

        apiService.atualizarParciais(   getContext(), checkTime);
        apiService.atualizarLigas(      getContext(), checkTime);
        apiService.atualizarPartidas(   getContext(), checkTime);
        new Handler().postDelayed(() -> refreshPartidas.setRefreshing(false), 750);
    }

    private void atualizarDadosSeHouverInternet(){
        if (isNetworkAvailable(getActivity().getApplicationContext())){
            atualizarDados(false);
        } else {
            refreshPartidas.setRefreshing(false);
            Snackbar.make( getActivity().getWindow().getDecorView().findViewById( android.R.id.content ), "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
        }
    }
}