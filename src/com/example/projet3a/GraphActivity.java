package com.example.projet3a;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;


public class GraphActivity extends Activity {
	private TextView coucou = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

    }
    
}