package br.com.devgeek.cartolaparciais.fragment;

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

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.ParciaisTimesFavoritosAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeFavorito;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by geovannefduarte on 09/09/17.
 */
public class ParciaisTimesFragment extends Fragment {

    private static final String TAG = "ParciaisTimesFragment";

    private Realm realm;
    private RealmResults<TimeFavorito> listaTimesFavoritos;
    private RealmChangeListener listaTimesFavoritosListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(listaTimesFavoritos);
            adapter.notifyDataSetChanged();
        }
    };

    private ApiServiceImpl apiService;
    private ParciaisTimesFavoritosAdapter adapter;
    private SwipeRefreshLayout refreshListaTimesFavoritos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        View view  = inflater.inflate(R.layout.fragment_parciaistimes, container, false);


        realm = Realm.getDefaultInstance();
        Sort[] sortOrder = { Sort.DESCENDING, Sort.DESCENDING, Sort.ASCENDING };
        String[] sortColumns = { "pontuacao", "variacaoCartoletas", "nomeDoTime" };
        listaTimesFavoritos = realm.where(TimeFavorito.class).findAllSortedAsync(sortColumns, sortOrder);


        refreshListaTimesFavoritos = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaTimesFavoritos);
        refreshListaTimesFavoritos.setOnRefreshListener(() -> atualizarDados());


        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTimesFavoritos);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( getActivity() , mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new ParciaisTimesFavoritosAdapter( getActivity() , listaTimesFavoritos );
        recyclerView.setAdapter( adapter );


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados();
        listaTimesFavoritos.addChangeListener(listaTimesFavoritosListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        listaTimesFavoritos.removeChangeListener(listaTimesFavoritosListener);
    }


    private void atualizarDados(){

        apiService.atualizarParciais(getContext(), true);
        new Handler().postDelayed(() -> refreshListaTimesFavoritos.setRefreshing(false), 850);
    }
}