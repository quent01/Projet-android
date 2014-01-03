package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseGenerators extends SQLiteOpenHelper{

	//Label table name
	private static final String TABLE_GENERATORS = "Generators";
	
	//labels Location Table Columns names
	private static final String KEY_ID = "Id";
	private static final String KEY_LOCATION_NAME = "Location_name";
	private static final String KEY_LATITUDE = "Latitude";
	private static final String KEY_LONGITUDE = "Longitude";
	
	private static final String CREATE_GENERATORS_BDD = "CREATE TABLE " + TABLE_GENERATORS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_LOCATION_NAME + " TEXT NOT NULL, "
			+ KEY_LATITUDE + " FLOAT, " + KEY_LONGITUDE + " FLOAT" +");";
	

	
	public DatabaseGenerators(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	
	//Creating generator table
	@Override
	public void onCreate(SQLiteDatabase db){
		//category table create query
		db.execSQL(CREATE_GENERATORS_BDD);
	}
	
	//Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
		//Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_GENERATORS);
		//create tables again
		onCreate(db);
	}
		
//	/**
//	 * inserting new table into lables table
//	 * */
//	public void insertLabel (String label) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//		ContentValues values = new ContentValues();
//		values.put(KEY_NAME, label);
//		
//		//inserting Row
//		db.insert(TABLE_NAME, null, values);
//		db.close();//closing database connection
//	}
//	 
//	/**
//	 * Getting all labels
//	 * Returns list of lables
//	 */
//	public List<String> getAllLabels(){
//		List<String> labels = new ArrayList<String>();
//		
//		//Select all Query
//		String selectQuery = "SELECT * FROM " + TABLE_NAME;
//		
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		
//		//looping through all rows and adding to list
//		if (cursor.moveToFirst()){
//			do {
//				labels.add(cursor.getString(1));
//			} while (cursor.moveToNext());
//		}
//		
//		//closing connection
//		cursor.close();
//		db.close();
//		
//		//returning labels
//		return labels;
//	}
}

