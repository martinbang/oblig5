package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;
import com.gpstracker.map.TrackerMapActivity;

public class MainActivity extends Activity {
	
	private Button btnMap;
	public static Activity activity;
	public static Handler handler = new Handler();
	public static Menu actionBarMenu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        registerThisDevice();
        
        activity = this;
        
        GTTabListener.initTabs(this);
        
        //kun for testing
        //TestButtonForMap();
    }
    
    /**
     * Registrerer telefonen hos GCM-server, hvis ikke allerede registrert
     */
    private void registerThisDevice()
    {
    	final String SENDER_ID = "579021654488";
    	final String TAG = "REGISTER";
    	
    	GCMRegistrar.checkDevice(this);
    	GCMRegistrar.checkManifest(this);
    	final String regId = GCMRegistrar.getRegistrationId(this);
    	if(regId.equals(""))
    	{
    		GCMRegistrar.register(this, SENDER_ID);
    		
    	} else     	
    		Log.v(TAG, "Already registered");
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        //Setter ikonet til start-knappen etter om du er registrert eller ei
        MenuItem powerService = menu.getItem(0);
        actionBarMenu = menu;
        if(Configuration.getCurrentConfiguration(this).getRegistered())
        	powerService.setIcon(android.R.drawable.button_onoff_indicator_on);
        else
        	powerService.setIcon(android.R.drawable.button_onoff_indicator_off);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Configuration conf = Configuration.getCurrentConfiguration(this);
    	
    	if(conf.getUserName().equals(""))
    		Toast.makeText(this, getResources().getString(R.string.register_fragment_name_not_set), Toast.LENGTH_SHORT).show();
    	
    	else if(item.getItemId() == R.id.powerService)
    	{
    		if(conf.getRegistered())
    		{
    			item.setIcon(android.R.drawable.button_onoff_indicator_off);
    			ServiceTestClass.unRegister(this);
    		} else
    		{
    			item.setIcon(android.R.drawable.button_onoff_indicator_on);
    			ServiceTestClass.register(this, conf.getUserName());
    		}
    		GTTabListener.initTabs(this);
    	}
    		
    	return super.onOptionsItemSelected(item);
    }
    
    
	
    
    
    
    
   
    /**
     * Kun For testing mot google map greia
     */
    public void TestButtonForMap(){
    	
    	//btnMap = (Button) findViewById(R.id.buttonMap);
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
