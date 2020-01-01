package com.can.vucutkitle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.can.vucutkitle.database.sorgular;

import androidx.appcompat.app.AppCompatActivity;

public class kayit extends AppCompatActivity {
    sorgular db;
    EditText e1, e2, e3;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);





        db = new sorgular(kayit.this);
        db.open();
        e1 = (EditText) findViewById(R.id.kullanici_adi);
        e2 = (EditText) findViewById(R.id.sifre);
        e3 = (EditText) findViewById(R.id.sifre2);
        b1 = (Button) findViewById(R.id.kayit);
        b2 = (Button) findViewById(R.id.giris);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(kayit.this, giris.class);
                startActivity(i);
                finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if (s1.equals("") || s2.equals("") || s3.equals("")) {
                    Toast.makeText(getApplicationContext(), "Alanlar boþ", Toast.LENGTH_SHORT).show();
                } else {
                    if (s2.equals(s3)) {
                        Boolean chkemail = db.chkemail(s1);
                        if (chkemail == true) {
                            Boolean insert = db.insert(s1, s2);
                            if (insert == true) {
                                Toast.makeText(getApplicationContext(), "Baþarýyla Kaydedildi", Toast.LENGTH_SHORT).show();




                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Bu e-posta zaten var", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Parola eþleþmedi", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });











    }

}

