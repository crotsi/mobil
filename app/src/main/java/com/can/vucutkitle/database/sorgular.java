package com.can.vucutkitle.database;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class sorgular {

    private Context contextsayfa;
    public static Resources resources;
    private SQLiteDatabase database;
    private sorgular db_genel;
    private DataBaseHandler dbHandler;

    // ==============================================================================

    public sorgular(Context context) {

        dbHandler = new DataBaseHandler(context);
        this.contextsayfa = context;



        try {

            dbHandler.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }
        try {

            dbHandler.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }


    }

    public void open() throws SQLException {
        database = dbHandler.getWritableDatabase();

    }

    public boolean kiloekle(String tarih, String kilo, String boy, String ideal_kilo) {
        open();
        ContentValues values = new ContentValues();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tarih", tarih);
        contentValues.put("kilo", kilo);
        contentValues.put("boy", boy);
        contentValues.put("ideal_kilo", ideal_kilo);
        long ins = database.insert("kitle",null, contentValues);
        if(ins == -1) return false;
        else return true;
    }

    public boolean insert(String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = database.insert("user", null,contentValues);

        if(ins == -1) return false;
        else return true;

    }



    public Boolean chkemail(String email) {
        Cursor cursor = database.rawQuery("Select * from user where email=?", new String[]{email});
        if(cursor.getCount() > 0) return false;
        else return true;
    }

    public Boolean emailpassword(String email, String password) {
        Cursor cursor = database.rawQuery("select * from user where email=? and password=?", new String[] {email, password});
        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public Cursor kilo_kayitlari_getir() {
        String query;
        int donen_deg;
        query = "select      * " +
                "from        kitle " +
                " order by _id DESC";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;

    }
    public void sayici_bilgilerini_kaydet(Context context, String tur, String milis) {
        long baslangicfark = Long.parseLong(zaman_getir_simdiki("milis")) - Long.parseLong(milis);
        if (baslangicfark < 120000) baslangicfark = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tur, true);
        editor.putBoolean(tur + "_duraklat", false);
        editor.putString(tur + "_sure", "0");
        editor.putString(tur + "_milis", milis);
        editor.putString(tur + "_fark", String.valueOf(baslangicfark));
        editor.putString(tur + "_sayici","sayici");
        editor.commit();

    }
    public String sayac_bilgi_bas_getir(Context context, String tur) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (preferences.contains(tur)) {
            return preferences.getString(tur + "_milis", "");
        } else {

            return tur + " için baslangýç kaydý açýlmamýþ";
        }

    }

    public void veri_kayit_uyku(long tarih_bas, long tarih_bit, long sure) {
        open();
        ContentValues values = new ContentValues();
        values.put("sayac_bas", tarih_bas);
        values.put("sayac_bit", tarih_bit);
        values.put("sure", sure);
        database.insert("sayac", null, values);
    }

    public void sayac_bilgi_bitir(Context context, String tur) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tur, false);
        editor.putBoolean(tur + "_duraklat", false);

        editor.commit();
    }

    public void sayac_duraklat(Context context, String tur, String milis) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tur + "_sure", milis);
        editor.putBoolean(tur + "_duraklat", true);
        editor.commit();
    }

    public void sayac_duraklat_devam(Context context, String tur) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tur + "_duraklat", false);
        editor.commit();
    }
    public Cursor KayitGetir() {
        String query = "SELECT * FROM sayac order by _id DESC";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor;
    }
    public String zaman_getir_simdiki(String format) {
        String deger = "";
        int mYear, mMonth, mDay, mHour, mMinute, mSecond;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);

        if (format.equals("tarih")) {
            deger = checkDigit(mDay) + "." + checkDigit(mMonth) + "." + mYear;
        } else if (format.equals("saat")) {
            deger = checkDigit(mHour) + ":" + checkDigit(mMinute);
        } else if (format.equals("saniye")) {
            deger = checkDigit(mHour) + ":" + checkDigit(mMinute) + ":" + checkDigit(mSecond);
        } else if (format.equals("herikisi")) {
            deger = checkDigit(mDay) + "." + checkDigit(mMonth) + "." + mYear + " " + checkDigit(mHour) + ":" + checkDigit(mMinute) + ":" + checkDigit(mSecond);
        } else if (format.equals("milis")) {
            String tam_tarih = checkDigit(mDay) + "." + checkDigit(mMonth) + "." + mYear + " " + checkDigit(mHour) + ":" + checkDigit(mMinute) + ":" + checkDigit(mSecond);
            DateTimeFormatter dTFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
            DateTime dt = dTFormatter.parseDateTime(tam_tarih);
            deger = String.valueOf((dt.getMillis()));
        } else if (format.equals("yil")) {
            deger = String.valueOf(mYear);
        } else if (format.equals("ay")) {
            deger = String.valueOf(mMonth);
        } else if (format.equals("gun")) {
            deger = String.valueOf(mDay);
        } else {
            deger = "Format türü algýlanamadý";
        }
        return deger;
    }
    public String tarih_to_milis(String tarih) {
        String deger = "";

        DateTimeFormatter dTFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
        DateTime dt = dTFormatter.parseDateTime(tarih);
        deger = String.valueOf((dt.getMillis()));

        return deger;
    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}



