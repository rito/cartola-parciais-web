package br.com.devgeek.cartolaparciais.fragment;

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

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.LigasTurnoAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeLiga;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;

/**
 * Created by geovannefduarte on 27/09/17.
 */
public class LigasTurnoFragment extends Fragment {

    private static final String TAG = "LigasTurnoFragment";

    private long ligaId;

    private Realm realm;
    private ApiServiceImpl apiService;
    private LigasTurnoAdapter adapter;
    private RealmResults<TimeLiga> listaTimesDaLiga;
    private SwipeRefreshLayout refreshListaTimesDaLiga;
    private RealmChangeListener listaTimesDaLigaListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(listaTimesDaLiga);
            adapter.notifyDataSetChanged();
        }
    };

    public static LigasTurnoFragment newInstance(long ligaId){
        Bundle bundle = new Bundle();
        bundle.putLong("ligaId", ligaId);

        LigasTurnoFragment fragment = new LigasTurnoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle){
        if (bundle != null) {
            ligaId = bundle.getLong("ligaId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        readBundle(getArguments());
        apiService = new ApiServiceImpl();
        realm = Realm.getDefaultInstance();
        View view  = inflater.inflate(R.layout.fragment_ligastimes, container, false);


        Sort[] sortOrder = { Sort.DESCENDING, Sort.ASCENDING };
        String[] sortColumns = { "pontuacaoTurno", "nomeDoTime" };
        listaTimesDaLiga = realm.where(TimeLiga.class).equalTo("ligaId", ligaId).findAllSortedAsync(sortColumns, sortOrder);


        refreshListaTimesDaLiga = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaTimesDaLiga);
        refreshListaTimesDaLiga.setOnRefreshListener(() -> atualizarDadosSeHouverInternet());

        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTimesDaLiga);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( getActivity() , mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new LigasTurnoAdapter( getActivity() , listaTimesDaLiga );
        recyclerView.setAdapter( adapter );


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        atualizarDados(true);
        listaTimesDaLiga.addChangeListener(listaTimesDaLigaListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        listaTimesDaLiga.removeChangeListener(listaTimesDaLigaListener);
    }

    private void atualizarDados(boolean checkTime){

        apiService.atualizarParciais(   getContext(), checkTime);
        new Handler().postDelayed(() -> refreshListaTimesDaLiga.setRefreshing(false), 750);
    }

    private void atualizarDadosSeHouverInternet(){
        if (isNetworkAvailable(getActivity().getApplicationContext())){
            atualizarDados(false);
        } else {
            refreshListaTimesDaLiga.setRefreshing(false);
            Snackbar.make( getActivity().getWindow().getDecorView().findViewById( android.R.id.content ), "Sem conexão com a internet", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();
        }
    }
}