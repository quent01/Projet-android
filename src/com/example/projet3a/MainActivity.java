package com.example.projet3a;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner spinner_locations;
	private Button btnSubmit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinner2();
    	addListenerOnButton();
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


