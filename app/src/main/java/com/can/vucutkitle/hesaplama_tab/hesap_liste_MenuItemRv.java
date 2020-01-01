package com.can.vucutkitle.hesaplama_tab;

public class hesap_liste_MenuItemRv {


    private final String Id;
    private final String kilo,boy,ideal_kilo,tarih;


    public hesap_liste_MenuItemRv(String id, String kilo, String boy, String ideal_kilo, String tarih) {
        this.Id = id;
        this.kilo=kilo;
        this.boy=boy;
        this.ideal_kilo=ideal_kilo;
        this.tarih=tarih;
    }


    public String get_id() {
        return Id;
    }
    public String get_kilo() {return kilo;}
    public String get_ideal_kilo() {return ideal_kilo;}
    public String get_boy() {return boy;}
    public String get_tarih() {return tarih;}

}
