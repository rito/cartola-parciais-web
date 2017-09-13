package br.com.devgeek.cartolaparciais.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.devgeek.cartolaparciais.R;

/**
 * Created by geovannefduarte on 13/09/17.
 */
public class ParciaisLigasFragment extends Fragment {

    private static final String TAG = "ParciaisLigasFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_parciaisligas, container, false);



        return view;
    }
}