package data.example.projet3a;

import com.example.projet3a.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//class to create à line of the main screen (sensor type, value and state)

public class sensor_adapter extends ArrayAdapter<sensor_line>{
	
	private Context context;
	
	public sensor_adapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context=context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View result = convertView;
		
		if(convertView == null){
			result = LayoutInflater.from(getContext()).inflate(R.layout.data_display, parent, false);
		}
		Typeface lovelo = Typeface.createFromAsset(context.getAssets(), "fonts/Lovelo Black.otf");
		Typeface lato_light = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Lig.otf");
		
		//Mettre en place les elements
		//Récupère l'item voulu
		sensor_line line = getItem(position);

		//SET SENSORTYPE
		TextView sensortype = (TextView) result.findViewById(R.id.sensortype);
		//we set the value in the TextView
		sensortype.setText(line.getSensorType());
		//we change the font
		sensortype.setTypeface(lato_light);
		
		//SET SENSOR_VALUE
		TextView sensorvalue = (TextView) result.findViewById(R.id.sensor_value);
		//we set the value in the TextView
		sensorvalue.setText(String.valueOf(line.getSensorValue()));//conversion float to string
		//we change the font
    	sensorvalue.setTypeface(lovelo);
		
		
		//SET SENSOR_UNITY
		TextView sensor_unity = (TextView) result.findViewById(R.id.sensor_unity);
		//we set the value in the TextView
		sensor_unity.setText(line.getSensorUnity());
    	sensor_unity.setTypeface(lato_light);
		
		//SET THE CORRECT SENSOR_TYPE IMAGE
		ImageView sensortype_img = (ImageView) result.findViewById(R.id.sensortype_img);
		sensortype_img.setImageResource(line.getIdImgSensor());
		
		//SET THE SENSOR STATE IMAGE
		ImageView sensorstate_img = (ImageView) result.findViewById(R.id.sensor_state);
		if(line.isState())//data is correct
			sensorstate_img.setImageResource(R.drawable.ok);//data is ok
		else
			sensorstate_img.setImageResource(R.drawable.warning);//data is in a weird range of values	
		
		
		return result;
		
	}
	
	public void updateData(){
		this.notifyDataSetChanged();
	}
	
}
