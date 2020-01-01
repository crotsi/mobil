package com.can.vucutkitle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.can.vucutkitle.hesaplama_tab.hesaplama_activitiy;
import com.can.vucutkitle.servis.sayac;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        //FloatingActionButton tanýmlayýp olay yarattýk
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));


            }
        });


        /// kaydedilen preferences de kuladi etiketli stringi aldýk. Menüdeki alanda kullandýk
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String hafizadakikuladi = preferences.getString("kuladi", "bulamdým");
        Toast.makeText(getApplicationContext(), "Hoþ Geldiniz Sayýn "+hafizadakikuladi, Toast.LENGTH_LONG).show();


        View headerView = navigationView.getHeaderView(0); //navigation header alanýný wiev olrak atadýk
        TextView navUsername = (TextView) headerView.findViewById(R.id.isimmenu); // oluþturulan wiev in içindeki text e ulaþtýk
        navUsername.setText(hafizadakikuladi); // texti deðiþtirdik
        /// kaydedilen preferences de kuladi etiketli stringi aldýk. Menüdeki alanda kullandýk
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        drawerr = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerr, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerr.setDrawerListener(toggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.listeleme) {
            Intent modify_intent = new Intent(MainActivity.this, hesaplama_activitiy.class);
            modify_intent.putExtra("sekme", "1");
            startActivity(modify_intent);

        }
        else if (id == R.id.kaydetme) {

            Intent modify_intent = new Intent(MainActivity.this, hesaplama_activitiy.class);
            modify_intent.putExtra("sekme", "0");
            startActivity(modify_intent);


        }
        else if (id == R.id.projehakkinda) {

            Intent modify_intent = new Intent(MainActivity.this, projehakkinda.class);
            startActivity(modify_intent);


        }
        else if (id == R.id.sayacmenu) {

            Intent modify_intent = new Intent(MainActivity.this, sayac.class);
            startActivity(modify_intent);


        }else if (id == R.id.benkimim) {

            Intent modify_intent = new Intent(MainActivity.this, sayac.class);
            startActivity(modify_intent);



        }
        drawerr.closeDrawers();
        return true;
    }
}
