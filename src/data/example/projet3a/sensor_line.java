package data.example.projet3a;


public class sensor_line {
	private String sensorType;
	private String sensorUnity;
	private String sensorValue;
	private int idImgSensor;
	private boolean state;
	
	
	public sensor_line() {
		sensorType=null;
		sensorUnity=null;
		sensorValue=null;
		idImgSensor=0;
		state=false;
	}
	
	
	public sensor_line(String sensorType, String sensorUnity,
			String sensorValue, int idImgSensor, boolean state) {
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
	public String getSensorValue() {
		return sensorValue;
	}
	public void setSensorValue(String string) {
		this.sensorValue = string;
	}
	public int getIdImgSensor() {
		return idImgSensor;
	}
	public void setIdImgSensor(int idImgSensor) {
		this.idImgSensor = idImgSensor;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	
	
	
}
