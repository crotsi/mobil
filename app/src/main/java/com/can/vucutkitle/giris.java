package com.can.vucutkitle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.can.vucutkitle.database.sorgular;

import androidx.appcompat.app.AppCompatActivity;

public class giris extends AppCompatActivity {
    EditText e1, e2;
    Button b1;
    sorgular db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new sorgular(this);
        db.open();
        e1 =(EditText)findViewById(R.id.editText);
        e2 =(EditText)findViewById(R.id.editText2);
        b1 =(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean Chkemailpass = db.emailpassword(email, password);
                if (Chkemailpass == true) {
                    Toast.makeText(getApplicationContext(), "Giriþ Baþarýlý", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.activity_hesaplama);


                    //kuladi etiketli bir preferences nesnesi oluþturduk. Kullanýcý adýný tutacak istediðimiz yerde kullanacaðýz
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("kuladi", email);
                    editor.commit();



                    Intent modify_intent = new Intent(giris.this, MainActivity.class);
                    startActivity(modify_intent);
                    finish();

                } else
                    Toast.makeText(getApplicationContext(), "Email Adresi Ya Da Parola Hatalý!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
