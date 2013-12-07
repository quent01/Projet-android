package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import data.example.projet3a.sensor_adapter;
import data.example.projet3a.sensor_line;
import android.os.Bundle;
import android.app.Activity;
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        addItemsOnSpinner2();
    	addListenerOnButton();
    	
    	Typeface lovelo = Typeface.createFromAsset(getAssets(), "fonts/Lovelo Black.otf");
    	
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
    	
    	//we add all the lines
    	line=new sensor_line("hydrometrie", "km", 102.3f,R.drawable.help,true);
    	adapter.add(line);
    	
    	line=new sensor_line("autre", "unité", 2.3f,R.drawable.help,false);
    	adapter.add(line);

    	line=new sensor_line("autre", "unité", 10.3f,R.drawable.help,true);
    	adapter.add(line);
    	
    	list.setAdapter(adapter);
    }
    
    // add items into spinner dynamically (Locations)
    public void addItemsOnSpinner2() {
   
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


