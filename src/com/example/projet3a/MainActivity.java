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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner spinner_locations;
	private Button btnSubmit;
	
	private sensor_adapter adapter;
	private sensor_line line;
	private ListView list;
	
	//	concerns the database
	private static String db_pwd = "test";
	
	private static int id_generator = 1;//a function must be done to determine the id of the generator
	
	
//JSON part of the code
	//url to make get the last data for all sensors of 1 generator
	//get is the method, test
	private static String url = "http://arduino.hostei.com/index.php/get/"
			+ db_pwd + "/"			//password to access the database
			+ id_generator +"/"		//id of the generator
			+ "sensorsdata";		//name to obtain last datas for all sensors
	
	//JSON node names
	private static final String TAG_SENSORS = "sensors";//the sensors json array
	private static final String TAG_UNIT = "unit";//the unit in which the sensor value is expressed 
	private static final String TAG_ID_GENERATOR = "id_generator";//change in location
	private static final String TAG_SENSOR_NAME = "sensor_name";
	private static final String TAG_VALUE = "value";
	private static final String TAG_DATE = "date";
	
	//data_array JSONArray
	private JSONArray data_array = null;
	
	//creation of the progress dialog bar ("data loading")
	protected ProgressDialog progress;
	final Handler progressHandler = new Handler(){
		public void handleMessage(Message msg){
			progress.setTitle("Processing...");
			progress.setMessage("Please wait.");
			progress.dismiss();
			list.setAdapter(adapter);
		}
		
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        
        Typeface lovelo = Typeface.createFromAsset(getAssets(), "fonts/Lovelo Black.otf");
        
        addItemsOnSpinner();
        
        addListenerOnButton();
    	
    	
    	
    	//change of font in "Lovelo"
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
    	adapter = new sensor_adapter(this, R.layout.data_display);
    	list = (ListView) findViewById(R.id.data_display_list);
    	
    	//display of the progress bar
    	progress = ProgressDialog.show(this, null, "Data loading", true);
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
		    	// Creating JSON Parser instance
		    	JSONParser jParser = new JSONParser();
		    	
		    	// getting JSON string from URL
		    	JSONObject json = jParser.getJSONFromUrl(url);//we get the json from the url
		    	 
		    	try {
		    		// Getting Array of data_array
		    	    data_array = json.getJSONArray(TAG_SENSORS);
		    	    
		    	 // looping through All Contacts
		    	    for(int i = 0; i < data_array.length(); i++){
		    	        JSONObject c = data_array.getJSONObject(i);
		    	         
		    	        line = new sensor_line();
		    	        line.setIdImgSensor(R.drawable.help);
		    	        line.setSensorType(c.getString(TAG_SENSOR_NAME));
		    	        line.setSensorUnity(c.getString(TAG_UNIT));
		    	        line.setSensorValue(c.getString(TAG_VALUE));
		    	        line.setState(false);//to modify with a function that determine if a value is safe or not
		    	    
		    	        adapter.add(line);
		    	    }
		    	    //we indicate that the treatment is over
		    	    progressHandler.sendMessage(progressHandler.obtainMessage());
		    	
		    	}catch (JSONException e) {
		    	    e.printStackTrace();
		    	}
			}
    	}).start();//begin the content of the run function
    }
    
    // add items into spinner dynamically (Locations)
    public void addItemsOnSpinner() {
   
	  	spinner_locations = (Spinner) findViewById(R.id.spinner_locations);
	  	List<String> list = new ArrayList<String>();
	  	list.add("location 1");
	  	list.add("location 2");
	  	list.add("location 3");
	  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	  		android.R.layout.simple_spinner_item, list);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spinner_locations.setAdapter(dataAdapter);
    }
   
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


