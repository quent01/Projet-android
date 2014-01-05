package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.example.projet3a.JSONParser;
import data.example.projet3a.sensor_adapter;
import data.example.projet3a.sensor_line;
import android.R.string;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity{


//	private Button btnSubmit;
	
	private sensor_adapter sensor_adapter;
	private sensor_line sensor_line;
	
	private Generator generator;

	
	//ListView for sensordata display
	private ListView listView;
	//Spinner for the generators
	private Spinner spinner_locations;
	
	//	concerns the database
	private static int id_generator = 1;//a function must be done to determine the id of the generator
	
//JSON part of the code
	//url to make get the last data for all sensors of 1 generator
	//get is the method, test
	
	
	//url to get the information about generators (id, location_name, location(latitude, longitude))
//	private static String url_generators = "http://arduino.hostei.com/index.php/get/generators";
	
	
	//JSON node names : Tag for sensordata url
	private static final String TAG_SENSORS = "sensors";//the sensors json array
	private static final String TAG_UNIT = "unit";//the unit in which the sensor value is expressed 
	private static final String TAG_SENSOR_NAME = "sensor_name";
	private static final String TAG_VALUE = "value";
	private static final String TAG_DATE = "date";
	
	//JSON node name : Tags for generators url
	private static final String TAG_GENERATORS = "generators";//The generators array
	private static final String TAG_ID_GENERATOR = "id_generator";
	private static final String TAG_LOCATION_NAME = "name_location";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	
	//sensordata_array JSONArray
//	private JSONArray sensordata_array = null;
	//generators_array JSONArray
//	private JSONArray generators_array = null;
	
	
	//creation of the progress dialog bar ("data loading")
	protected ProgressDialog progress;
	@SuppressLint("HandlerLeak") final Handler progressHandler = new Handler(){
		public void handleMessage(Message msg){
			progress.setTitle("Processing...");
			progress.setMessage("Please wait.");
			progress.dismiss();
			listView.setAdapter(sensor_adapter);
		}
		
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        
        Typeface lovelo = Typeface.createFromAsset(getAssets(), "fonts/Lovelo Black.otf");
        
        addListenerOnButton();
        
    	//we change the font to "Lovelo"
    	TextView text_help_logo = (TextView) findViewById(R.id.text_help_logo);
    	text_help_logo.setTypeface(lovelo);
    	
    	TextView text_data = (TextView) findViewById(R.id.data_text);
    	text_data.setTypeface(lovelo);
    	
    	TextView text_state_logo = (TextView) findViewById(R.id.text_state);
    	text_state_logo.setTypeface(lovelo);
    	
    	TextView text_type = (TextView) findViewById(R.id.text_type);
    	text_type.setTypeface(lovelo);
    	
    	TextView text_value = (TextView) findViewById(R.id.text_value);
    	text_value.setTypeface(lovelo);
    	
    	//display all lines
    	sensor_adapter = new sensor_adapter(this, R.layout.data_display);
    	listView = (ListView) findViewById(R.id.data_display_list);
    	
    	//display of the progress bar
    	progress = ProgressDialog.show(this, null, "Data loading", true);
    	
    	
    	new AsyncTaskGetGenerators().execute();  
    	new AsyncTaskGetSensors().execute();
    	
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskGetGenerators extends AsyncTask<String, String, String> {
    	final String TAG = "AsyncTaskGetGenerators.java";
 
    	// set your json string url here
    	//url to get the information about generators (id, location_name, location(latitude, longitude))
    	private String url_generators = "http://arduino.hostei.com/index.php/get/generators";
    	
    	JSONArray generators_array = null;
    	private List<Generator> generatorList = new ArrayList<Generator>();
    	
    	@Override
    	protected void onPreExecute(){}
    	
    	@Override
    	protected String doInBackground(String... arg0){
 
        	try {
        		// Creating JSON Parser instance
            	JSONParser jParser_generator = new JSONParser();
            	
            	// We get the JSON string from URL
            	JSONObject json_generator = jParser_generator.getJSONFromUrl(url_generators);
        		
        		// Getting Array of sensordata_array
        	    generators_array = json_generator.getJSONArray(TAG_GENERATORS);
        	    
        	    // Looping through All Contacts
        	    for(int i = 0; i < generators_array.length(); i++){
        	        JSONObject o = generators_array.getJSONObject(i);
        	         
        	        generator = new Generator();
        	        generator.setIdGenerator(Integer.parseInt(o.getString(TAG_ID_GENERATOR)));
        	        generator.setLocation_name(o.getString(TAG_LOCATION_NAME));
        	        generator.setLatitude(Float.valueOf(o.getString(TAG_LATITUDE)));
        	        generator.setLongitude(Float.valueOf(o.getString(TAG_LONGITUDE)));
        	        generatorList.add(generator);
        	        
        	     // show the values in our logcat
                    Log.e(TAG, "id_generator: " + generator.getIdGenerator() 
                            + ", location_name: " + generator.getLocation_name()
                            + ", latitude: " + generator.getLatitude()
                            + ", longitude: "+ generator.getLongitude());
        	  	  	
        	        //We put data in a BDD to retrieve the id of each generator later
//        	        GeneratorsBDD generatorsBDD = new GeneratorsBDD(MainActivity.this);
//        	        generatorsBDD.open();
//        	        generatorsBDD.updateGenerator(generator.getIdGenerator(), generator);
//        	        generatorsBDD.close();
        	    }   	    
        	    //we indicate that the treatment is over
        	    progressHandler.sendMessage(progressHandler.obtainMessage());
        	
        	}catch (JSONException e) {
        	    e.printStackTrace();
        	}
        	
        	return null;
    	}
    	@Override
        protected void onPostExecute(String strFromDoInBg) {
    		//we build the spinner
			addItemsOnSpinner(generatorList);
    	}
    	
    }
    
    public class AsyncTaskGetSensors extends AsyncTask<String, String, String>{
    	final String TAG = "AsyncTaskGetSensors.java";
    	
    	private String url_sensorsdata = "http://arduino.hostei.com/index.php/get/"
    			+ id_generator +"/"		//id of the generator
    			+ "sensorsdata";		//name to obtain last datas for all sensors
    	
    	JSONArray sensordata_array = null;
    	private List<sensor_line> sensor_lineList = new ArrayList<sensor_line>();
    	
    	@Override
    	protected void onPreExecute(){}
    	
    	@Override
    	protected String doInBackground(String... arg0){
    		 
	    	try {
	    		// Creating JSON Parser instance
		    	JSONParser jParser_sensors = new JSONParser();
		    	
		    	// We get the JSON string from URL
		    	JSONObject json_sensors = jParser_sensors.getJSONFromUrl(url_sensorsdata);
		    	
	    		// Getting Array of sensordata_array
	    	    sensordata_array = json_sensors.getJSONArray(TAG_SENSORS);
	    	    
	    	    // Looping through All Contacts
	    	    for(int i = 0; i < sensordata_array.length(); i++){
	    	        JSONObject c = sensordata_array.getJSONObject(i);
	    	         
	    	        sensor_line = new sensor_line();
	    	        sensor_line.setIdImgSensor(R.drawable.help);
	    	        sensor_line.setSensorType(c.getString(TAG_SENSOR_NAME));
	    	        sensor_line.setSensorUnity(c.getString(TAG_UNIT));
	    	        sensor_line.setSensorValue(c.getString(TAG_VALUE));
	    	        sensor_line.setState(false);//to modify with a function that determine if a value is safe or not
	    	        sensor_lineList.add(sensor_line);
	    	        
	    	        //show the values in logcat
	    	        Log.e(TAG, "sensor_name: "+ sensor_line.getSensorType()
	    	        		+ ", sensor_unit: "+ sensor_line.getSensorUnity()
	    	        		+ ", sensor_value: "+ sensor_line.getSensorValue()
	    	        		+ ",sensor_state: " + sensor_line.isState());
	    	    }
	    	    //we indicate that the treatment is over
	    	    progressHandler.sendMessage(progressHandler.obtainMessage());
	    	
	    	}catch (JSONException e) {
	    	    e.printStackTrace();
	    	}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(String strFromDoInBg){
    		//we buil all the sensor line
    		addSensor_Lines(sensor_lineList);
    		
    	}
    	
    }
    
//   private void loadSpinnerData(GeneratorsBDD generatorsBDD){
//	  //Spinner drop down elements
//	  List<String> generators = generatorsBDD.getAllGenerators();
//	  
//	  //creating adapter for spinner
//	  ArrayAdapter<String> generatorAdapter = new ArrayAdapter<String>(this,
//			  android.R.layout.simple_spinner_item, generators);
//	  
//	  //Drop down layout style - list view with radio button
//	  generatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	  
//	  //attaching generators adapter to spinner
//	  spinner.setAdapter(generatorAdapter);
//	  
//   }
    
   	//add items into spinner
    public void addItemsOnSpinner(List<Generator> generatorList){
	  
    	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
    	List<String> list_location = new ArrayList<String>();
    	Log.i("\ngeneratorList.size = ", Integer.toString(generatorList.size()));
    	for (int i=0; i<generatorList.size();i++){
		   Generator generator = generatorList.get(i);
		   list_location.add(generator.getLocation_name());
		   Log.i("Boucle While : ",generator.getLocation_name());
    	}
	   
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
             android.R.layout.simple_spinner_item, list_location);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner_locations.setAdapter(dataAdapter);
    }
    
    //Display the data concerning sensors (type, value,state, etc)
    public void addSensor_Lines(List<sensor_line> sensor_lineList){
    	for(int i=0;i<sensor_lineList.size();i++){
    		sensor_line sensor_line = sensor_lineList.get(i);
    		sensor_adapter.add(sensor_line);
    	}
    }
    
    // add items into spinner dynamically (Locations)
//    public void addItemsOnSpinner(GeneratorsBDD generatorsBDD) {
//       	
//    	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
//        List<String> list_locations = new ArrayList<String>();
//        list_locations.add("location 1");
//        list_locations.add("location 2");
//        list_locations.add("location 3");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list_locations);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_locations.setAdapter(dataAdapter);
//	  	
//    }
   
    // get the selected dropdown list value
    public void addListenerOnButton() {
 
    	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
//  	btnSubmit = (Button) findViewById(R.id.btnSubmit);
   
    	/*btnSubmit.setOnClickListener(new OnClickListener() {
   
  	  	public void onClick(View v) {
   
  	    Toast.makeText(MainActivity.this,
  		"OnClickListener : " + 
                  "\nSpinner locations : "+ String.valueOf(spinner_locations.getSelectedItem()),
  			Toast.LENGTH_SHORT).show();
  	  }
   
  	});*/
    }
    
    

    


}


