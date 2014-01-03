/**
 * Class that contain informations we need about a generator
 */

package com.example.projet3a;

public class Generator {
	private int idGenerator;
	private String location_name;
	private float latitude;
	private float longitude;
	
	public Generator(){
		idGenerator = 0;
		location_name = null;
		latitude = 0.00f;
		longitude = 0.00f;
	}
	
	public Generator(int idGenerator, String location_name,
			float latitude, float longitude) {
		
		super();
		this.idGenerator = idGenerator;
		this.location_name = location_name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//getters and setters
	public int getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(int idGenerator) {
		this.idGenerator = idGenerator;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public String toString(){
		return "ID : "+ idGenerator
				+ "\nLOCATION_NAME : " + location_name
				+ "\nLATITUDE : " + latitude
				+ "\nLONGTUDE : " + longitude;
	}
}
