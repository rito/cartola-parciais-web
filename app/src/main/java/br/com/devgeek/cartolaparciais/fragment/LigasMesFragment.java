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

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.adapter.LigasMesAdapter;
import br.com.devgeek.cartolaparciais.api.service.impl.ApiServiceImpl;
import br.com.devgeek.cartolaparciais.model.TimeLiga;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.isNetworkAvailable;
import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.setupAds;

/**
 * Created by geovannefduarte on 24/09/17.
 */
public class LigasMesFragment extends Fragment {

    private static final String TAG = "LigasMesFragment";

    private long ligaId;

    private Realm realm;
    private ApiServiceImpl apiService;
    private LigasMesAdapter adapter;
    private RealmResults<TimeLiga> listaTimesDaLiga;
    private SwipeRefreshLayout refreshListaTimesDaLiga;
    private RealmChangeListener listaTimesDaLigaListener = new RealmChangeListener(){
        @Override
        public void onChange(Object object){
            adapter.update(orderAdapterList(listaTimesDaLiga));
            adapter.notifyDataSetChanged();
        }
    };

    public static LigasMesFragment newInstance(long ligaId){
        Bundle bundle = new Bundle();
        bundle.putLong("ligaId", ligaId);

        LigasMesFragment fragment = new LigasMesFragment();
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
        View view  = inflater.inflate(R.layout.fragment_ligastimes_mes, container, false);


        listaTimesDaLiga = realm.where(TimeLiga.class).equalTo("ligaId", ligaId).findAllAsync();


        refreshListaTimesDaLiga = (SwipeRefreshLayout) view.findViewById(R.id.refreshListaTimesDaLiga);
        refreshListaTimesDaLiga.setOnRefreshListener(() -> atualizarDadosSeHouverInternet());

        // Configurar recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTimesDaLiga);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration( getActivity() , mLayoutManager.getOrientation() );
        recyclerView.addItemDecoration( divider );

        adapter = new LigasMesAdapter( getActivity() , orderAdapterList(listaTimesDaLiga) );
        recyclerView.setAdapter( adapter );



        setupAds(TAG, getContext(), realm, (AdView) view.findViewById(R.id.adView));
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

        apiService.atualizarParciais(   getContext(), checkTime, ligaId);
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

    private List<TimeLiga> orderAdapterList(RealmResults<TimeLiga> listaTimesDaLiga){

        List<TimeLiga> timesDaLiga = new ArrayList<>();

        if (listaTimesDaLiga != null && listaTimesDaLiga.size() > 0){

            timesDaLiga = realm.copyFromRealm(listaTimesDaLiga);

            for (TimeLiga time : timesDaLiga){
                if (time.getAtletas() != null && (time.getVariacaoCartoletas() == null || time.getVariacaoCartoletas() == 0)){
                    time.setPontuacaoMes(time.getPontuacaoMes()+time.getPontuacao());
                }
            }

            Collections.sort(timesDaLiga, (t1, t2) -> {

                if (t1.getPontuacaoMes() < t2.getPontuacaoMes()) return  1;
                if (t1.getPontuacaoMes() > t2.getPontuacaoMes()) return -1;

                if (t1.getVariacaoCartoletas() < t2.getVariacaoCartoletas()) return  1;
                if (t1.getVariacaoCartoletas() > t2.getVariacaoCartoletas()) return -1;

                return t1.getNomeDoTime().compareTo(t2.getNomeDoTime());
            });

            adapter.update(timesDaLiga);
        }

        return timesDaLiga;
    }
}