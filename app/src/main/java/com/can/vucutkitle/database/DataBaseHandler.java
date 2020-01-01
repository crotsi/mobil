package com.can.vucutkitle.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHandler extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH;
	private static String OLDDB_PATH;
	private static String DB_NAME = "database.db";

	private SQLiteDatabase myDataBase;

	private final Context myContext;
	public Context myContext2;
	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHandler(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
		myContext2=context;
		DB_PATH = context.getDatabasePath(DB_NAME).toString();
//		Log.e("path", DB_PATH);
	}

	// ==============================================================================

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

			boolean dbExist = checkDataBase();

			if (dbExist) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(myContext2.getApplicationContext());
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("veritabaniyeni", 0);
				editor.commit();
			} else {

				// By calling this method and empty database will be created into
				// the default system path
				// of your application so we are gonna be able to overwrite that
				// database with our database.
				this.getReadableDatabase();
				this.close();
				try {

					copyDataBase();

				} catch (IOException e) {

					throw new Error("Error copying database");

				}
			}

	}

	// ==============================================================================

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	// ==============================================================================

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open( DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	// ==============================================================================

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		OLDDB_PATH=myContext.getDatabasePath(OLDDB_NAME).toString();
//		String OLDmyPath = OLDDB_PATH;
//		OLDmyDataBase = SQLiteDatabase.openDatabase(OLDmyPath, null, SQLiteDatabase.OPEN_READONLY);
//
////		String query = "SELECT * FROM ayarlar";
////		Cursor cursor = OLDmyDataBase.rawQuery(query, null);
////		cursor.moveToFirst();
////
////
////		db.execSQL("update set ayarlar anne_isim="+cursor.getString(cursor.getColumnIndex("son_adet_tarih"))+" ");
//
//
//		db.execSQL("ATTACH DATABASE ? AS Old_DB", new String[]{OLDDB_PATH});
//		db.execSQL("INSERT INTO ayarlar SELECT * FROM Old_DB.ayarlar");

		//db.execSQL("update set ayarlar tanitim=1 where _id=1");

		if (oldVersion<2) {
//				String sql = "update ayarlar set reklam=1";
//				db.execSQL(sql);
		}
	}


}