package com.hamdan.elaajmedicalapp.introductory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamdan.elaajmedicalapp.LoginRegister;
import com.hamdan.elaajmedicalapp.R;

public class OnBoardingFragment1 extends Fragment {
    TextView skip1;
    AppCompatButton getStarted1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.on_boarding1, null);

        skip1 = rootView.findViewById(R.id.skip);
        getStarted1 = rootView.findViewById(R.id.getStarted);

        skip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginRegister.class));
            }
        });
        getStarted1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginRegister.class));
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
}