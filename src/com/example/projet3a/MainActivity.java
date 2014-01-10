package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import data.example.projet3a.JSONParser;
import data.example.projet3a.sensor_adapter;
import data.example.projet3a.sensor_line;

public class MainActivity extends FragmentActivity implements OnItemSelectedListener{
	
	private sensor_adapter sensor_adapter;
	private sensor_line sensor_line;
	
	private Generator generator;
	GeneratorsBDD generatorsBDD = new GeneratorsBDD(MainActivity.this);
	private String location_selected;

	//ListView for sensordata display
	private ListView listView;
	//Spinner for the generators
	private Spinner spinner_locations;
	//Wifi state
	private Web web = new Web();
	//	DataBase _ id_generator by default
	private static int id_generator = 1;	
	
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
        //sensorlineView = (View) findViewById(R.id.sensorline);
        Typeface lovelo = Typeface.createFromAsset(getAssets(), "fonts/Lovelo Black.otf");
        
        initializeActionBar();
        
        //addListenerOnButton();
        
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
    	
    	//Check wifi state
    	CheckWifiState();
    	
    	//display all lines
    	sensor_adapter = new sensor_adapter(this, R.layout.data_display);
    	listView = (ListView) findViewById(R.id.data_display_list);
    	
    	spinner_locations.setOnItemSelectedListener(this); 
    	listView.setAdapter(sensor_adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		
    	@Override
		public void onItemClick(AdapterView<?> parent, View  view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent t = new Intent(MainActivity.this, GraphActivity.class);
			//passer la sensorline choisi a l'autre activity
			startActivity(t);
		}
      });
    	
    	new AsyncTaskGetGenerators().execute();  
    	
    	// Google Map
    	GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.142, 178.431), 2));
    }
    
  //Creation of the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
        
    }
    
    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskGetGenerators extends AsyncTask<String, String, String> {
    	final String TAG = "AsyncTaskGetGenerators.java";
 
    	// set your json string url here
    	//URL to get the information about generators (id, location_name, location(latitude, longitude))
    	private String url_generators = "http://arduino.hostei.com/index.php/get/generators";
    	
    	JSONArray generators_array = null;    	
    	protected ProgressDialog progress_generators;
    	@Override
    	protected void onPreExecute(){
    		progress_generators = new ProgressDialog(MainActivity.this);
    		progress_generators.setMessage("Chargement des lieux...");
    		progress_generators.show();
    	}
    	
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
        	        generator.setIdGenerator(o.getInt(TAG_ID_GENERATOR));
        	        generator.setLocation_name(o.getString(TAG_LOCATION_NAME));
        	        generator.setLatitude(Float.valueOf(o.getString(TAG_LATITUDE)));
        	        generator.setLongitude(Float.valueOf(o.getString(TAG_LONGITUDE)));
        	        
        	        // show the values in our logcat
                    Log.i(TAG, "id_generator: " + generator.getIdGenerator() 
                            + ", location_name: " + generator.getLocation_name()
                            + ", latitude: " + generator.getLatitude()
                            + ", longitude: "+ generator.getLongitude());
        	  	  	
        	        // We put data in a BDD to retrieve the id of each generator later
        	        generatorsBDD.open();
        	        // We verify that there is no generator with the same name
        	        if(generatorsBDD.getGeneratorWithLocationName(generator.getLocation_name()) == null){
        	        	generatorsBDD.insertGenerator(generator);
        	        }
        	        else{	
        	        	generatorsBDD.updateGenerator(generator.getIdGenerator(), generator);
        	        }
        	        
        	        Log.i(TAG, "BDD_lines: " +generatorsBDD.getAllGenerators().size());
        	        generatorsBDD.close();
        	    }   	    
        	
        	}catch (JSONException e) {
        	    e.printStackTrace();
        	}
        	
        	return null;
    	}
    	@Override
        protected void onPostExecute(String strFromDoInBg) {
    		//we build the spinner
    		generatorsBDD.open();
    		loadSpinnerData(generatorsBDD);
    		generatorsBDD.close();
//			addItemsOnSpinner(generatorList);
//    		listView.setAdapter(sensor_adapter);
    		if(progress_generators.isShowing()){
    			progress_generators.dismiss();
    		}	
    	}
    	
    }
  
    //you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskGetSensors extends AsyncTask<String, String, String>{
    	final String TAG = "AsyncTaskGetSensors.java";
    	
    	JSONArray sensordata_array = null;
    	private List<sensor_line> sensor_lineList = new ArrayList<sensor_line>();
    	protected ProgressDialog progress_sensors;
    	@Override
    	protected void onPreExecute(){
    		progress_sensors = new ProgressDialog(MainActivity.this);
    		progress_sensors.setMessage("Chargement des capteurs...");
    		progress_sensors.show();
    	}
    	
    	@Override
    	protected String doInBackground(String... location_name){
    		//we retrieve the id of the location_name of the spinner in the database 
    		generatorsBDD.open();
        	generator = generatorsBDD.getGeneratorWithLocationName(location_selected);
        	id_generator = generator.getIdGenerator();
        	generatorsBDD.close();
        	Log.i("onItemSelected: ",""+ generator.getLocation_name()+" ,The id change to " + id_generator);
        	
        	final String url_sensorsdata = "http://arduino.hostei.com/index.php/get/"
        			+ id_generator +"/"		//id of the generator
        			+ "sensorsdata";		//name to obtain last data for all sensors
        	Log.i("url_sensordata: ",""+url_sensorsdata);
        	
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
	    	        sensor_line.setSensorType(c.getString(TAG_SENSOR_NAME));
	    	        sensor_line.setSensorUnity(c.getString(TAG_UNIT));
	    	        sensor_line.setSensorValue(c.getDouble(TAG_VALUE));
	    	        
	    	        // Change the state to true if value is ok, false if not
	    	        sensor_line.setState(sensor_line.isOk());
	    	        
	    	        //Application of an icon corresponding to the type of sensor
	    	        if(sensor_line.getSensorType().equals("Température"))
	    	        	sensor_line.setIdImgSensor(R.drawable.ic_temperature);
	    	        else if(sensor_line.getSensorType().equals("Humidité"))
	    	        	sensor_line.setIdImgSensor(R.drawable.ic_humidity);
	    	        else if(sensor_line.equals("Rotation moteur"))
	    	        	sensor_line.setIdImgSensor(R.drawable.help);
	    	        else if(sensor_line.equals("Rendement"))
	    	        	sensor_line.setIdImgSensor(R.drawable.help);
	    	        else
	    	        	sensor_line.setIdImgSensor(R.drawable.help);
	    	        
	    	        sensor_lineList.add(sensor_line);
	    	        
	    	        //Show the values in logcat
	    	        Log.i(TAG, "sensor_name: "+ sensor_line.getSensorType()
	    	        		+ ", sensor_unit: "+ sensor_line.getSensorUnity()
	    	        		+ ", sensor_value: "+ sensor_line.getSensorValue()
	    	        		+ ",sensor_state: " + sensor_line.getState());
	    	    }
	    	
	    	}catch (JSONException e) {
	    	    e.printStackTrace();
	    	}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(String strFromDoInBg){
    		//We build all the sensor line
    		addSensor_Lines(sensor_lineList);
    		listView.setAdapter(sensor_adapter);
    		if(progress_sensors.isShowing()){
    			progress_sensors.dismiss();
    		}
    		
    	}
    	
    }
   
   //We add items on spinners with a database 
   public void loadSpinnerData(GeneratorsBDD generatorsBDD){
//	  spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
	  List<String> list_location = generatorsBDD.getAllGenerators();
	  Log.i("\nlist_location.size = ", Integer.toString(list_location.size()));
	  //creating adapter for spinner
	  ArrayAdapter<String> generatorAdapter = new ArrayAdapter<String>(this,
			  android.R.layout.simple_spinner_item, list_location);
	  
	  //Drop down layout style - list view with radio button
	  generatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  
	  //attaching generators adapter to spinner
	  spinner_locations.setAdapter(generatorAdapter);
	  
   }
    
   	//Add items on spinner with a list of generator
   // This function is not used
    public void addItemsOnSpinner(List<Generator> generatorList){
	  
    	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
    	List<String> list_location = new ArrayList<String>();
    	
    	int size = generatorList.size();
    	Log.i("\ngeneratorList.size = ", Integer.toString(size));
    	
    	for (int i=0; i<size;i++){
		   Generator generator = generatorList.get(i);
		   list_location.add(generator.getLocation_name());
		   Log.i("Boucle While : ",generator.getLocation_name());
    	}
	   
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
             android.R.layout.simple_spinner_item, list_location);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner_locations.setAdapter(dataAdapter);
    }
    
    //Add the data concerning sensors (type, value,state, etc)
    public void addSensor_Lines(List<sensor_line> sensor_lineList){
    	// We remove all sensor_line
    	sensor_adapter.clear();
    	// We add the sensor line
    	int size = sensor_lineList.size();
    	for(int i=0;i<size;i++){
    		sensor_line sensor_line = sensor_lineList.get(i);
    		sensor_adapter.add(sensor_line);
    	}
    }
   
    // Get the selected dropdown list value
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
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
    	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
    	//generator = new Generator();
    	location_selected = parent.getItemAtPosition(pos).toString();
    	Log.i("onItemSelected: ",location_selected);
    	new AsyncTaskGetSensors().execute(location_selected);
    	
    }

    public void onNothingSelected(AdapterView<?> parent){
    	//to do
    }
    
    public void initializeActionBar(){
    	//Change the background color of the Action Bar
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header_color)));
        
        // We add the navigation up (button "<") 
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // We remove the title
        actionBar.setDisplayShowTitleEnabled(false);
        
     // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }
    
    public void CheckWifiState(){
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		int wifiState = wifiManager.getWifiState();
	
    	if(wifiState != WifiManager.WIFI_STATE_ENABLED){
    		Toast toast = Toast.makeText(getApplicationContext(), "Wifi inactif", Toast.LENGTH_LONG);
    		toast.show();
    	}
    	else{
    		Toast toast = Toast.makeText(getApplicationContext(), "Wifi Actif", Toast.LENGTH_LONG);
    		toast.show();
    		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    		String strBSSID = wifiInfo.getBSSID();
    		Log.i("ENI", "BSSID =" + strBSSID);
    		String strIP = String.valueOf(wifiInfo.getIpAddress());
    		Log.i("ENI", "Ip = " + strIP);
    		String strMac = wifiInfo.getMacAddress();
    		Log.i("ENI", "Mac = " + strMac);
    		
    	}
    }


}


