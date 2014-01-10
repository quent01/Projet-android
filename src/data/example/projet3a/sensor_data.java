package data.example.projet3a;

public class sensor_data {
	private String id_data;
	private double value;
	private String date;
	
	public sensor_data()
	{
		setId_data(null);
		setValue(0.0);
		setDate(null);
	}
	
	public sensor_data(String id_data, double value, String date)
	{
		this.setId_data(id_data);
		this.setValue(value);
		this.setDate(date);
	}

	public String getId_data() {
		return id_data;
	}

	public void setId_data(String id_data) {
		this.id_data = id_data;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
