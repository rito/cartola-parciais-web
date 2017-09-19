package br.com.devgeek.cartolaparciais.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.LoginActivity;
import br.com.devgeek.cartolaparciais.adapter.LigasAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.Liga;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.userGloboIsLogged;

/**
 * Created by geovannefduarte on 13/09/17.
 */
public class ParciaisLigasFragment extends Fragment {

    private static final String TAG = "ParciaisLigasFragment";

    private Realm realm;
    private ApiServiceImpl apiService;

    private RealmResults<Liga> ligas;
    private SwipeRefreshLayout refreshLigas;

    private Button fazerLogin;
    private LigasAdapter adapter;
    private RecyclerView recyclerView;
    private RealmChangeListener ligasListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(ligas);
            adapter.notifyDataSetChanged();
            setupLigasFragment();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        apiService = new ApiServiceImpl();
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_parciaisligas, container, false);


        Sort[] sortOrder = { Sort.DESCENDING, Sort.ASCENDING, Sort.ASCENDING };
        String[] sortColumns = { "tipoLiga", "nomeDaLiga", "descricaoDaLiga" };
        ligas = realm.where(Liga.class).findAllSortedAsync(sortColumns, sortOrder);


        refreshLigas = (SwipeRefreshLayout) view.findViewById(R.id.refreshLigas);
        refreshLigas.setOnRefreshListener(() -> atualizarDados());


        fazerLogin = (Button) view.findViewById(R.id.fazerLogin);
        fazerLogin.setOnClickListener(v -> {

            if (userGloboIsLogged()){

                Snackbar.make( v, "Você já está logado", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();

            } else {

                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
            }
        });


        // Configurar recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.listaDeLigas);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( getActivity() , mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new LigasAdapter( getActivity() , ligas );
        recyclerView.setAdapter( adapter );

        setupLigasFragment();


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados();
        ligas.addChangeListener(ligasListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        ligas.removeChangeListener(ligasListener);
    }


    private void atualizarDados(){

        apiService.atualizarParciais(   getContext(), true);
        apiService.atualizarLigas(      getContext(), true);
        apiService.atualizarPartidas(   getContext(), true);
        new Handler().postDelayed(() -> refreshLigas.setRefreshing(false), 850);
    }

    private void setupLigasFragment(){

        if (userGloboIsLogged() && ligas.size() > 0){

            fazerLogin.setVisibility(View.GONE);
            fazerLogin.setVisibility(View.INVISIBLE);
            refreshLigas.setVisibility(View.VISIBLE);

        } else {

            refreshLigas.setVisibility(View.GONE);
            refreshLigas.setVisibility(View.INVISIBLE);
            fazerLogin.setVisibility(View.VISIBLE);

        }
    }
}