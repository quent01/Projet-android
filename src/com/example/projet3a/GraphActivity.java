package com.example.projet3a;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.example.projet3a.JSONParser;
import data.example.projet3a.sensor_data;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GraphActivity extends Activity {
	GraphicalView mChart;
	private static final String TAG_SENSORS = "data";//The sensor array
	private static final String TAG_ID_DATA = "id_data";//The sensor array
	private static final String TAG_VALUE = "value";//The sensor array
	private static final String TAG_DATE = "date";//The sensor array
	List<sensor_data> sensor_dataList;
	private sensor_data sensor_data;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
 
        initializeActionBar();
       
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

        // Get data via the key
        new AsyncTaskGetSensorsData().execute();
        //double tab[] = { 2000,2500,2700,2100,2800};
        //openChart(tab);
        }

    }
    private void openChart(double[] data_values, String[] date){
   	 
        int count = data_values.length;
        Date[] dt = new Date[count];
        for(int i=0;i<count;i++){
            //GregorianCalendar gc = new GregorianCalendar(2014, 01, 01, 01+i, 00);
            try {
				dt[i] = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date[i]);
				//dt[i] = gc.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Log.e("openChart.java", ""+dt[i]);
        }

        double[] views = data_values;
 
        // Creating TimeSeries for Visits
        //TimeSeries visitsSeries = new TimeSeries("Visits");
 
        // Creating TimeSeries for Views
        TimeSeries viewsSeries = new TimeSeries("Capteur");
 
        // Adding data to Visits and Views Series
        for(int i=0;i<dt.length;i++){
            
            viewsSeries.add(dt[i],views[i]);
        }
 
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
 
        // Adding Visits Series to the dataset
        //dataset.addSeries(visitsSeries);
 
        // Adding Visits Series to dataset
        dataset.addSeries(viewsSeries);
 
        // Creating XYSeriesRenderer to customize visitsSeries
        /*XYSeriesRenderer visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.WHITE);
        visitsRenderer.setPointStyle(PointStyle.CIRCLE);
        visitsRenderer.setFillPoints(true);
        visitsRenderer.setLineWidth(2);
        visitsRenderer.setDisplayChartValues(true);*/
 
        // Creating XYSeriesRenderer to customize viewsSeries
        XYSeriesRenderer viewsRenderer = new XYSeriesRenderer();
        viewsRenderer.setColor(Color.BLUE);
        viewsRenderer.setPointStyle(PointStyle.CIRCLE);
        viewsRenderer.setFillPoints(true);
        viewsRenderer.setLineWidth(2);
        viewsRenderer.setDisplayChartValues(true);
 
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
 
        multiRenderer.setChartTitle("Evolution du capteur");
        multiRenderer.setXTitle("Temps");
        multiRenderer.setYTitle("Type de données");
        multiRenderer.setZoomButtonsVisible(true);
 
        // Adding visitsRenderer and viewsRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        //multiRenderer.addSeriesRenderer(visitsRenderer);
        multiRenderer.addSeriesRenderer(viewsRenderer);
 
        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
 
        // Creating a Time Chart
        mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd MMMM yyyy (H)");
 
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(10);
        
 
        // Setting a click event listener for the graph
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Format formatter = new SimpleDateFormat("dd MMMM yyyy");
 
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
 
                if (seriesSelection != null) {
                    int seriesIndex = seriesSelection.getSeriesIndex();
                    String selectedSeries="Value";
   
                    // Getting the clicked Date ( x value )
                    long clickedDateSeconds = (long) seriesSelection.getXValue();
                    Date clickedDate = new Date(clickedDateSeconds);
                    String strDate = formatter.format(clickedDate);
 
                    // Getting the y value
                    double amount = (double) seriesSelection.getValue();
 
                    // Displaying Toast Message
                    Toast.makeText(
                        getBaseContext(),
                        selectedSeries + " on "  + strDate + " : " + amount ,
                        Toast.LENGTH_SHORT).show();
                    }
                }
            });
 
            // Adding the Line Chart to the LinearLayout
            chartContainer.addView(mChart);
    }
  //Creation of the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public void initializeActionBar(){
    	//Change the background color of the Action Bar
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header_color)));
        
        //We add the navigation up (button "<") 
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //We remove the title
        actionBar.setDisplayShowTitleEnabled(false);
        
     // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }
    
    public class AsyncTaskGetSensorsData extends AsyncTask<String, String, String>{
    	final String TAG = "AsyncTaskGetSensorsData.java";
    	int id_generator = 1;
    	int id_sensor = 2;
    	JSONArray sensor_data_array = null;
    	ProgressDialog progress_sensor_data;
    	double[] data_values  = new double[50];
    	String[] data_dates  = new String[50];
    	
    	protected void onPreExecute(){
    		progress_sensor_data = new ProgressDialog(GraphActivity.this);
    		progress_sensor_data.setMessage("Chargement des données...");
    		progress_sensor_data.show();
    	}
    	
    	@Override
    	protected String doInBackground(String... location_name){
        	final String url_sensorsdata = "http://arduino.hostei.com/index.php/get/"
        			+ id_generator +"/"		//id of the generator
        			+ id_sensor;		//name to obtain last datas for all sensors
        	
        	try {
	    		// Creating JSON Parser instance
		    	JSONParser jParser_sensor_data = new JSONParser();
		    	
		    	// We get the JSON string from URL
		    	JSONObject json_sensors = jParser_sensor_data.getJSONFromUrl(url_sensorsdata);
		    	
	    		// Getting Array of sensordata_array
		    	sensor_data_array = json_sensors.getJSONArray(TAG_SENSORS);
	    	    
	    	    // Looping through All Contacts
	    	    for(int i = 0; i < sensor_data_array.length(); i++){
	    	        JSONObject c = sensor_data_array.getJSONObject(i);
	    	         
	    	        sensor_data = new sensor_data();
	    	        sensor_data.setId_data(c.getString(TAG_ID_DATA));
	    	        sensor_data.setValue(c.getDouble(TAG_VALUE));
	    	        sensor_data.setDate(c.getString(TAG_DATE));
	    	        sensor_dataList = new ArrayList<sensor_data>();
					sensor_dataList.add(sensor_data);
	    	        
	    	        //show the values in logcat
	    	        Log.e(TAG, "sensor_name: "+ sensor_data.getId_data()
	    	        		+ ", Value: "+ sensor_data.getValue()
	    	        		+ ",Data: " + sensor_data.getDate());
	    	        
	    	        data_values[i]=sensor_data.getValue();
	    	        data_dates[i]=sensor_data.getDate();
	    	    }
	    	    //we indicate that the treatment is over
//	    	    progressHandler.sendMessage(progressHandler.obtainMessage());
	    	}catch (JSONException e) {
	    	    e.printStackTrace();
	    	}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(String strFromDoInBg){

				openChart(data_values, data_dates);

			if(progress_sensor_data.isShowing()){
    			progress_sensor_data.dismiss();
    		}
    	}
    }
    
}