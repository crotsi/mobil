package com.can.vucutkitle.servis;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.can.vucutkitle.R;
import com.can.vucutkitle.database.sorgular;
import androidx.appcompat.app.AppCompatActivity;

public class sayac extends AppCompatActivity {
    int sayacnokta = 0;
    TextView sayac, sayac_durum;
    LinearLayout sayac_duraklat_line, sayac_devam_line, sayac_buton_gurubu, butonkineer;
    ImageButton btn_duraklat, btn_devam, sayac_bitir;
    MyMainReceiver myMainReceiver;
    Intent myIntent = null;
    String sayac_milis_degeri;
    LinearLayout acilir_menu_dialog;
    Button sayac_baslat;
    ListView lv;
    Cursor cursor;
    String[] from;
    SimpleCursorAdapter adapter;
    sorgular db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sayac ");
        setContentView(R.layout.sayac_uyku);
        db = new sorgular(com.can.vucutkitle.servis.sayac.this);
        db.open();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        if (preferences.getString("uyku_milis", "") != null || !preferences.getString("uyku_sure", "").equals("")) {
            sayac_milis_degeri = "0";

        } else {
            sayac_milis_degeri = preferences.getString("uyku_sure", "");
        }
        sayac_duraklat_line = (LinearLayout) findViewById(R.id.sayac_duraklat_line);
        sayac_buton_gurubu = (LinearLayout) findViewById(R.id.sayac_buton_gurubu);
        sayac_devam_line = (LinearLayout) findViewById(R.id.sayac_devam_line);
        sayac_baslat = (Button) findViewById(R.id.sayac_baslat);
        sayac_bitir = (ImageButton) findViewById(R.id.sayac_bitir);
        btn_duraklat = (ImageButton) findViewById(R.id.btn_duraklat);
        btn_devam = (ImageButton) findViewById(R.id.btn_devam);
        sayac = (TextView) findViewById(R.id.sayac);
        sayac_durum = (TextView) findViewById(R.id.sayac_taraf_durum);
        butonkineer = (LinearLayout) findViewById(R.id.butonkineer);
        acilir_menu_dialog = (LinearLayout) findViewById(R.id.butonkineer);
        sayac_buton_gurubu.setVisibility(View.VISIBLE);
        sayac.setVisibility(View.VISIBLE);

        if (!servisCalisiyormu()) {
            sayac_duraklat_line.setVisibility(View.GONE);
            sayac_devam_line.setVisibility(View.VISIBLE);

        } else {

            sayac_duraklat_line.setVisibility(View.VISIBLE);
            sayac_devam_line.setVisibility(View.GONE);
            sayac_milis_degeri = preferences.getString("sayac_sure", "");
            sayac_durum.setText("Bebek Uyuyor");
        }

        if (preferences.contains("sayac_duraklat") && preferences.getBoolean("sayac_duraklat", true)) {

            Toast.makeText(getApplicationContext(), "duraklatýldý", Toast.LENGTH_SHORT).show();
            btn_duraklat.setVisibility(View.GONE);
            btn_devam.setVisibility(View.VISIBLE);
            sayac_degeri_yaz();
        } else {
            btn_duraklat.setVisibility(View.VISIBLE);
            btn_devam.setVisibility(View.GONE);
        }
        sayac_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.sayici_bilgilerini_kaydet(com.can.vucutkitle.servis.sayac.this, "sayac", db.zaman_getir_simdiki("milis"));
                startService();
                sayac_duraklat_line.setVisibility(View.VISIBLE);
                sayac_devam_line.setVisibility(View.GONE);
                sayac_durum.setText("sayac çaliþýyor");
            }
        });
        sayac_bitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopService();
                sayac_duraklat_line.setVisibility(View.GONE);
                sayac_devam_line.setVisibility(View.VISIBLE);
                sayac_durum.setText("");
                db.veri_kayit_uyku(Long.parseLong(db.sayac_bilgi_bas_getir(com.can.vucutkitle.servis.sayac.this, "sayac")),
                        Long.parseLong(db.zaman_getir_simdiki("milis")),
                        Long.parseLong(sayac_milis_degeri) / 1000);
                sayacnokta = 5;
                db.sayac_bilgi_bitir(com.can.vucutkitle.servis.sayac.this, "sayac");
                cursor = db.KayitGetir();
                from = new String[]{"_id", "sure"};
                int[] to = new int[]{R.id.member_id, R.id.member_name};
                adapter = new SimpleCursorAdapter(
                        com.can.vucutkitle.servis.sayac.this, R.layout.z_list_view_yazilar, cursor, from, to);
                lv.setAdapter(adapter);
                sayac_durum.setText("sayac durdu");


            }
        });
        btn_duraklat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //stopService();
                Intent intent = new Intent();
                intent.setAction(sayac_servis.ACTION_MSG_TO_SERVICE);
                intent.putExtra(sayac_servis.KEY_MSG_TO_SERVICE, "ddd");
                sendBroadcast(intent);

                db.sayac_duraklat(com.can.vucutkitle.servis.sayac.this, "sayac", sayac_milis_degeri);
                btn_duraklat.setVisibility(View.GONE);
                btn_devam.setVisibility(View.VISIBLE);

            }
        });
        btn_devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startService();

                Intent intent = new Intent();
                intent.setAction(sayac_servis.ACTION_BASLAT);
                intent.putExtra(sayac_servis.KEY_MSG_TO_SERVICE, "ddd");
                sendBroadcast(intent);
                db.sayac_duraklat_devam(com.can.vucutkitle.servis.sayac.this, "sayac");
                btn_duraklat.setVisibility(View.VISIBLE);
                btn_devam.setVisibility(View.GONE);

            }
        });
         lv = (ListView) findViewById(R.id.memberList_id);
         cursor = db.KayitGetir();
         from = new String[]{"_id", "sure"};
        int[] to = new int[]{R.id.member_id, R.id.member_name};

        adapter = new SimpleCursorAdapter(
                com.can.vucutkitle.servis.sayac.this, R.layout.z_list_view_yazilar, cursor, from, to);
        lv.setAdapter(adapter);
    }

    public boolean servisCalisiyormu() {//Servis Çalýþýyor mu kontrol eden fonksiyon

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (sayac_servis.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startService() {
        myIntent = new Intent(com.can.vucutkitle.servis.sayac.this, sayac_servis.class);
        startService(myIntent);
    }

    private void stopService() {
        if (myIntent != null) {
            stopService(myIntent);
        }
        myIntent = null;
    }

    @Override
    protected void onStart() {
        myIntent = new Intent(com.can.vucutkitle.servis.sayac.this, sayac_servis.class);
        myMainReceiver = new MyMainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(sayac_servis.ACTION_UPDATE_CNT);
        intentFilter.addAction(sayac_servis.ACTION_UPDATE_MSG);
        intentFilter.addAction(sayac_servis.ACTION_UPDATE_CR);
        intentFilter.addAction(sayac_servis.ACTION_DURAKLAT);
        intentFilter.addAction(sayac_servis.ACTION_DURDUR);
        registerReceiver(myMainReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myMainReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService();
    }

    private class MyMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(sayac_servis.ACTION_UPDATE_CR)) {
                sayac_milis_degeri = intent.getStringExtra(sayac_servis.KEY_INT_FROM_SERVICE);
                sayac_degeri_yaz();

            } else if (sayac_servis.ACTION_DURDUR.equals(action)) {
                Toast.makeText(com.can.vucutkitle.servis.sayac.this, "duraklat", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(com.can.vucutkitle.servis.sayac.this, String.valueOf(action), Toast.LENGTH_SHORT).show();
            }
            if (sayacnokta < 3) {
                sayac_durum.setText(sayac_durum.getText() + ".");
                sayacnokta++;
            } else {
                sayacnokta = 0;
                sayac_durum.setText(sayac_durum.getText().toString().replace(".", ""));
            }
        }
    }

    public void sayac_degeri_yaz() {
        long elapsedMillis = Long.parseLong(sayac_milis_degeri);
        String saat = "";
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        if (hours == 0) {
            saat = "";
        } else {
            saat = checkDigit(hours) + ":";
        }
        sayac.setText(String.valueOf(saat + checkDigit(minutes) + ":" + checkDigit(seconds)));
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

