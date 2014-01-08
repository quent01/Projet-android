/*
 * This class permit to build the object which will permit
 * to display data of a generator, line by line  
 */


package data.example.projet3a;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import android.R;


public class sensor_line{
	private String sensorType;
	private String sensorUnity;
	private double sensorValue;
	private int idImgSensor;
	private boolean state;
	
	public sensor_line() {
		sensorType=null;
		sensorUnity=null;
		sensorValue=0.0f;
		idImgSensor=0;
		state=false;
	}
	
	public sensor_line(String sensorType, String sensorUnity,
			Float sensorValue, int idImgSensor, boolean state) {
		super();
		this.sensorType = sensorType;
		this.sensorUnity = sensorUnity;
		this.sensorValue = sensorValue;
		this.idImgSensor = idImgSensor;
		this.state = state;
	}

	//getters and setters
	public String getSensorType() {
		return sensorType;
	}
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	public String getSensorUnity() {
		return sensorUnity;
	}
	public void setSensorUnity(String sensorUnity) {
		this.sensorUnity = sensorUnity;
	}
	public double getSensorValue() {
		return sensorValue;
	}
	public void setSensorValue(double d) {
		this.sensorValue = d;
	}
	public int getIdImgSensor() {
		return idImgSensor;
	}
	public void setIdImgSensor(int idImgSensor) {
		this.idImgSensor = idImgSensor;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	/**
	 * function that tell if a the sensorvalue of a sensorline  is
	 * in a correct range of values. 
	 * @return 
	 */
	public boolean isOk(){
		String Humidity = "Humidité";
		String Temperature = "Température";
		String RotationMotor = "Rotation moteur";
		String Rendement = "Rendement";
		
		float minHumidity = 50.5f, maxHumidity = 100.0f;// 			%		change the value
		float minTemperature = 50.5f, maxTemperature = 100.0f;	//	°C		change the value
		float minRotationMotor = 50.5f, maxRotationMotor = 100.0f;//tr/min	change the value
		float minRendement = 50.5f, maxRendement = 100.0f;//		%		change the value
		
		String sensortype = this.getSensorType();
		double sensorvalue = this.getSensorValue();
		
		state = false;
		if(sensortype.equals(Temperature)){
			if(sensorvalue > minTemperature && sensorvalue < maxTemperature){state = true;}
		}
		else if(sensortype.equals(Humidity)){
			if(sensorvalue > minHumidity && sensorvalue < maxHumidity){state = true;}
		}
		else if(sensortype.equals(RotationMotor)){
			if(sensorvalue > minRotationMotor && sensorvalue < maxRotationMotor){state = true;}
		}
		else if(sensortype.equals(Rendement)){
			if(sensorvalue > minRendement && sensorvalue < maxRendement){state = true;}
		}

		return state;
	}
	
	
}
