package com.can.vucutkitle;




import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.can.vucutkitle.database.DataBaseHandler;

import java.io.File;
import java.io.IOException;

public class dbyiaktar extends Activity {
    //private ProgressDialog progressDialog;
    Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.z_arkaplan);


//        progressDialog.setMessage("Its loading....");
//        progressDialog.setTitle("ProgressDialog bar example");
//        progressDialog.show();
//        progressDialog.setProgress(50);


        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

        if (!folder.exists()) folder.mkdirs();
        new LoadViewTask().execute();

    }


    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        //Before running code in separate thread
        @Override
        protected void onPreExecute() {

        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */

            DataBaseHandler myDbHelper = new DataBaseHandler(dbyiaktar.this);
            try {
                myDbHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Database is not created!");
            }
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }


            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Void result) {

            startActivity(new Intent(dbyiaktar.this, ilk_acilan_ekran.class));
finish();

        }



    }
    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
