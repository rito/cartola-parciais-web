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
import br.com.devgeek.cartolaparciais.adapter.ParciaisJogadoresAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.userGloboIsLogged;

/**
 * Created by geovannefduarte
 */
public class ParciaisJogadoresFragment extends Fragment {

    private static final String TAG = "ParciaisJogadoresFragme";

    private Realm realm;
    private RealmResults<AtletasPontuados> listaAtletasPontuados;
    private ApiServiceImpl apiService;
    private ParciaisJogadoresAdapter adapter;
    private RealmChangeListener listaAtletasPontuadosListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(listaAtletasPontuados);
            adapter.notifyDataSetChanged();
        }
    };
    private SwipeRefreshLayout refreshListaTimesFavoritos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        View view  = inflater.inflate(R.layout.fragment_parciaisjogadores, container, false);


        realm = Realm.getDefaultInstance();
        Sort[] sortOrder = { Sort.DESCENDING, Sort.ASCENDING };
        String[] sortColumns = { "pontuacao", "apelido" };
        listaAtletasPontuados = realm.where(AtletasPontuados.class).isNotNull( "rodada" ).findAllSortedAsync(sortColumns, sortOrder);



        refreshListaTimesFavoritos = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaParciaisJogadores);
        refreshListaTimesFavoritos.setOnRefreshListener(() -> atualizarDadosSeHouverInternet());



        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaParciaisJogadores);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ParciaisJogadoresAdapter( getActivity(), listaAtletasPontuados, userGloboIsLogged());
        recyclerView.setAdapter( adapter );



        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados(true);
        listaAtletasPontuados.addChangeListener(listaAtletasPontuadosListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        listaAtletasPontuados.removeChangeListener(listaAtletasPontuadosListener);
    }

    private void atualizarDados(boolean checkTime){

        apiService.atualizarParciais(   getContext(), checkTime);
        apiService.atualizarLigas(      getContext(), checkTime);
        apiService.atualizarPartidas(   getContext(), checkTime);
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