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
		//This has to be modified, we don't want to drop the table we want to update it
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_GENERATORS);
		//create tables again
		onCreate(db);
	}
		
}

