package com.can.vucutkitle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.can.vucutkitle.hesaplama_tab.hesaplama_activitiy;
import com.can.vucutkitle.servis.sayac;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    private RelativeLayout hesaplama, listeleme, benkimim, proje, sayfa1,sayfa2;
    TextView hosgeldin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String hafizadakikuladi = preferences.getString("kuladi", "bulamdým");

        hosgeldin = (TextView) view.findViewById(R.id.hosgeldin);
        hosgeldin.setText("Sayýn "+ hafizadakikuladi +" Hoþ geldiniz"); //bu satýr SharedPreferences kullamarak yazdýrýldý


        hesaplama = (RelativeLayout) view.findViewById(R.id.hesaplama);
        hesaplama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modify_intent = new Intent(getActivity(), hesaplama_activitiy.class);
                modify_intent.putExtra("sekme", "0");
                startActivity(modify_intent);
            }
        });

        listeleme = (RelativeLayout) view.findViewById(R.id.listeleme);
        listeleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modify_intent = new Intent(getActivity(), hesaplama_activitiy.class);
                modify_intent.putExtra("sekme", "1");
                startActivity(modify_intent);
            }
        });


        benkimim = (RelativeLayout) view.findViewById(R.id.benkimim);
        benkimim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        proje = (RelativeLayout) view.findViewById(R.id.proje);
        proje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        sayfa1 = (RelativeLayout) view.findViewById(R.id.sayfa1);
        sayfa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), sayac.class));



            }
        });
        sayfa2 = (RelativeLayout) view.findViewById(R.id.sayfa2);
        sayfa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        return view;
    }
}