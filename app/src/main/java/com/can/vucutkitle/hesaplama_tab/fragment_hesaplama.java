package com.can.vucutkitle.hesaplama_tab;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.can.vucutkitle.R;
import com.can.vucutkitle.database.sorgular;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class fragment_hesaplama extends Fragment {
    View view;
    sorgular db;
    List<Object> mRecyclerViewItems = new ArrayList<>();
    RecyclerView rv;
    hesap_liste_MyAdapter adapter;
    hesap_liste_MenuItemRv menuItem;
    public fragment_hesaplama() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new sorgular(getActivity());
        db.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hesaplama_list, container, false);

        LinearLayout kizbaslik =(LinearLayout)view.findViewById(R.id.kizbaslik); kizbaslik.setVisibility(View.VISIBLE);


        rv = (RecyclerView) view.findViewById(R.id.mRecycler);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new hesap_liste_MyAdapter(getActivity(), mRecyclerViewItems, new hesap_liste_MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        rv.setAdapter(adapter);
        addMenuItemsFromJson();

        return view;
    }
    private void addMenuItemsFromJson() {
        //try {
        Cursor b = db.kilo_kayitlari_getir();

        String kilo="",boy="",ideal_kilo="",tarih="";
        int id=0;
        while (b.moveToNext()) {

            try {
                id = b.getInt(b.getColumnIndex("_id"));
                kilo = b.getString(b.getColumnIndex("kilo"));
                boy = b.getString(b.getColumnIndex("boy"));
                ideal_kilo = b.getString(b.getColumnIndex("ideal_kilo"));
                tarih = b.getString(b.getColumnIndex("tarih"));

            }catch (Exception e){}

            menuItem = new hesap_liste_MenuItemRv(String.valueOf(id),kilo,boy,ideal_kilo,tarih);
            mRecyclerViewItems.add(menuItem);
        }
        b.close();

//        } catch (Exception exception) {
//        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}