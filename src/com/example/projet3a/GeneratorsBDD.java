package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GeneratorsBDD {
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "database.db";
	
	private static final String TABLE_GENERATORS = "Generators";
	private static final String KEY_ID = "Id";
	private static final int NUM_KEY_ID = 0;
	private static final String KEY_LOCATION_NAME = "Location_name";
	private static final int NUM_KEY_LOCATION_NAME = 1;
	private static final String KEY_LATITUDE = "Latitude";
	private static final int NUM_KEY_LATITUDE = 2;
	private static final String KEY_LONGITUDE = "Longitude";
	private static final int NUM_KEY_LONGITUDE = 3;
	
	private SQLiteDatabase bdd;
	
	private DatabaseGenerators databaseGenerators;
	
	public GeneratorsBDD(Context context){
		//we create the BDD ans its table
		databaseGenerators = new DatabaseGenerators((Context) context, NOM_BDD, null, VERSION_BDD);
	}
	
	public void open(){
		//we open the BDD in writing
		bdd = databaseGenerators.getWritableDatabase();
	}
	
	public void close(){
		//we close the access to the BDD
		bdd.close();
	}
	
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public long insertGenerator(Generator generator){
		//creation of a ContentValues
		ContentValues values = new ContentValues();
		//we add a value associated with a key which is the name of the columns in which we want to put the value
		values.put(KEY_ID, generator.getIdGenerator());
		values.put(KEY_LOCATION_NAME, generator.getLocation_name());
		values.put(KEY_LATITUDE, generator.getLatitude());
		values.put(KEY_LONGITUDE, generator.getLongitude());
		//we insert the object in the database via the ContentValues
		return bdd.insert(TABLE_GENERATORS, null, values);
	}
	
	public int updateGenerator(int id, Generator generator){
		//we indicate the generator to update with the id
		ContentValues values = new ContentValues();
		values.put(KEY_LOCATION_NAME, generator.getLocation_name());
		values.put(KEY_LATITUDE, generator.getLatitude());
		values.put(KEY_LONGITUDE, generator.getLongitude());
		return bdd.update(TABLE_GENERATORS, values, KEY_ID + "=" +id, null);
	}
	
	public int removeGeneratorWithID(int id){
		return bdd.delete(TABLE_GENERATORS, KEY_ID + "=" + id, null);
	}
	
	public Generator getGeneratorWithLocationName(String location_name){
		Cursor c = bdd.query(TABLE_GENERATORS, new String[] {KEY_ID, KEY_LOCATION_NAME, KEY_LATITUDE, KEY_LONGITUDE},
				KEY_LOCATION_NAME + "LIKE \"" + location_name + "\"", null, null, null, null);
		return cursorToGenerator(c);
	}
	
	public Generator cursorToGenerator(Cursor c){
		//if no element were returned in the request we return null
		if (c.getCount() == 0)
			return null;
		
		//if not we go to the first element
		c.moveToFirst();
		//we create the generator
		Generator generator = new Generator();
		generator.setIdGenerator(NUM_KEY_ID);
		generator.setLocation_name(KEY_LOCATION_NAME);
		generator.setLatitude(NUM_KEY_LATITUDE);
		generator.setLongitude(NUM_KEY_LONGITUDE);
		
		//we close the cursor
		c.close();
		
		//we return the generator
		return generator;
	}
	
	public List<String> getAllGenerators(){
		List<String> generators = new ArrayList<String>();
		//Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_GENERATORS;
		
		SQLiteDatabase db = databaseGenerators.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		//looping through all row and adding to list
		if (cursor.moveToFirst()){
			do {
				generators.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		//closing connection
		cursor.close();
		db.close();
		//returning generators
		return generators;
	}
}

