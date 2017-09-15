package br.com.devgeek.cartolaparciais.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.activity.LoginActivity;

/**
 * Created by geovannefduarte on 13/09/17.
 */
public class ParciaisLigasFragment extends Fragment {

    private static final String TAG = "ParciaisLigasFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_parciaisligas, container, false);

        Button fazerLogin = (Button) view.findViewById(R.id.fazerLogin);
        fazerLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right_to_left,R.anim.slide_in_left_to_right);
        });

        return view;
    }
}