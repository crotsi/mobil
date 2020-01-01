package com.can.vucutkitle.hesaplama_tab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.can.vucutkitle.R;
import com.can.vucutkitle.database.sorgular;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class hesaplama_kayit_Fragment extends Fragment {

    private EditText editText;
    private TextView boy_tv,durum_tv,ideal_tv,kilo_tv;
    private SeekBar seekBar;
    private RadioGroup radioGroup;
    private boolean erkekmi = true;
    private double boy = 1.71;
    private int kilo = 80;
    sorgular db;
    private RadioGroup.OnCheckedChangeListener radioGroupOlayIsleyicisi = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.erkek)
                erkekmi = true;
            else if (checkedId == R.id.kadin)
                erkekmi = false;

            //guncelle();
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarOlayIsleyicisi = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            kilo = 30 + progress;
            guncelle();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private TextWatcher editTextOlayIsleyicisi = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try{
                boy = Double.parseDouble(s.toString())/100.0;

            }catch (NumberFormatException e){
                boy = 0.0;
            }

            guncelle();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = new sorgular(getActivity());
        db.open();
        View root = inflater.inflate(R.layout.hesaplama_kayit_fragment, container, false);







        editText = root.findViewById(R.id.editText);
        boy_tv = (TextView) root.findViewById(R.id.boy_tv);
        durum_tv = (TextView) root.findViewById(R.id.durum_tv);
        boy_tv = (TextView) root.findViewById(R.id.boy_tv);
        ideal_tv = (TextView) root.findViewById(R.id.ideal_tv);
        kilo_tv = (TextView) root.findViewById(R.id.kilo_tv);
        radioGroup = root.findViewById(R.id.radioGroup);
        seekBar = (SeekBar) root.findViewById(R.id.seekBar);

        editText.addTextChangedListener(editTextOlayIsleyicisi);
        seekBar.setOnSeekBarChangeListener(seekBarOlayIsleyicisi);
        radioGroup.setOnCheckedChangeListener(radioGroupOlayIsleyicisi);

        guncelle();


        final Button btn_tum_kayitlar = (Button) root.findViewById(R.id.kaydet);
        btn_tum_kayitlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

//You can change "yyyyMMdd_HHmmss as per your requirement

                String tarih = sdf.format(new Date());


                Toast.makeText(getActivity(), String.valueOf(tarih+"-"+kilo_tv.getText().toString()+"-"+boy_tv.getText().toString()+"-"+ideal_tv.getText().toString()), Toast.LENGTH_SHORT).show();


                Boolean eklemeislemi = db.kiloekle(tarih, kilo_tv.getText().toString(),boy_tv.getText().toString(),ideal_tv.getText().toString());
if (eklemeislemi== true) {
    Intent modify_intent = new Intent(getActivity(), hesaplama_activitiy.class);
    modify_intent.putExtra("sekme", "1");
    startActivity(modify_intent);
}
else Toast.makeText(getActivity(), "Kayýt iþemini yapamadým", Toast.LENGTH_SHORT).show();

            }
        });


        return root;
    }

    private void guncelle() {
        kilo_tv.setText(String.valueOf(kilo) + " kg");
        boy_tv.setText(String.valueOf(boy) + " m");

        int ideal_kiloErkek = (int) (50 + 2.3 * (boy * 100 * 0.4 - 60));
        int ideal_kiloKadin = (int) (45.5 + 2.3 * (boy * 100 * 0.4 - 60));
        double vki = kilo / (boy * boy);

        if (erkekmi){
            ideal_tv.setText(String.valueOf(ideal_kiloErkek));
            if (vki <= 20.7){
                durum_tv.setBackgroundResource(R.color.zayif);
                durum_tv.setText(R.string.zayif);
            }else if (20.7 < vki && vki <= 26.4){
                durum_tv.setBackgroundResource(R.color.durum_ideal);
                durum_tv.setText(R.string.ideal);
            }else if (26.4 < vki && vki <= 27.8){
                durum_tv.setBackgroundResource(R.color.durum_normalden_fazla);
                durum_tv.setText(R.string.normalden_fazla);
            }else if (27.8 < vki && vki <= 31.1){
                durum_tv.setBackgroundResource(R.color.durum_fazla_kilolu);
                durum_tv.setText(R.string.fazla_kilolu);
            }else if (31.1 < vki && vki <= 34.9){
                durum_tv.setBackgroundResource(R.color.durum_obez);
                durum_tv.setText(R.string.obez);
            }else {
                durum_tv.setBackgroundResource(R.color.durum_doktora);
                durum_tv.setText(R.string.doktora);
            }
        }else{
            ideal_tv.setText(String.valueOf(ideal_kiloKadin));
            if (vki <= 19.1){
                durum_tv.setBackgroundResource(R.color.zayif);
                durum_tv.setText(R.string.zayif);
            }else if (19.1 < vki && vki <= 25.8){
                durum_tv.setBackgroundResource(R.color.durum_ideal);
                durum_tv.setText(R.string.ideal);
            }else if (25.8 < vki && vki <= 27.3){
                durum_tv.setBackgroundResource(R.color.durum_normalden_fazla);
                durum_tv.setText(R.string.normalden_fazla);
            }else if (27.3 < vki && vki <= 32.3){
                durum_tv.setBackgroundResource(R.color.durum_fazla_kilolu);
                durum_tv.setText(R.string.fazla_kilolu);
            }else if (32.3 < vki && vki <= 34.9){
                durum_tv.setBackgroundResource(R.color.durum_obez);
                durum_tv.setText(R.string.obez);
            }else {
                durum_tv.setBackgroundResource(R.color.durum_doktora);
                durum_tv.setText(R.string.doktora);
            }
        }
    }

}