package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gpstracker.map.TrackerMapActivity;

public class MainActivity extends Activity {
	
	private Button btnMap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //kun for testing
        TestButtonForMap();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Kun For testing mot google map greia
     */
    public void TestButtonForMap(){
    	
    	btnMap = (Button) findViewById(R.id.buttonMap);
    	btnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,TrackerMapActivity.class);
		        startActivity(i);
			}
		});
    	
    }//end method
    
}//end activity
