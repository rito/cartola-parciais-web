package br.com.devgeek.cartolaparciais.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.LoginActivity;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.userGloboIsLogged;

/**
 * Created by geovannefduarte on 13/09/17.
 */
public class ParciaisLigasFragment extends Fragment {

    private static final String TAG = "ParciaisLigasFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_parciaisligas, container, false);

        if (userGloboIsLogged()){

            // GET -> api.cartolafc.globo.com/auth/ligas
            // Content-Type -> application/json
            // X-GLB-Token

        } else {

            Button fazerLogin = (Button) view.findViewById(R.id.fazerLogin);
            fazerLogin.setOnClickListener(v -> {

                if (userGloboIsLogged()){

                    Snackbar.make( view, "Você já está logado", Snackbar.LENGTH_SHORT ).setAction( "Action", null ).show();

                } else {

                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
                }
            });

        }

        return view;
    }


}