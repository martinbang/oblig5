package com.gpstracker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gcm.GCMRegistrar;
import com.gpstracker.conf.ConfigurationFragment;
import com.gpstracker.gcm.ServiceTestClass;

import com.gpstracker.map.TrackerMapActivity;

public class MainActivity extends Activity {
	
	private Button btnMap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        registerThisDevice();
        initTabs();
        
        //kun for testing
        //TestButtonForMap();
    }

    private void initTabs()
    {
    	ActionBar actionBar = getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	actionBar.setDisplayShowTitleEnabled(false);
    	addTab(actionBar, "Instillinger", "configurations", ConfigurationFragment.class);
    	Tab t = actionBar.newTab();
    	t.setText("Map");
    	t.setTabListener(new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, TrackerMapActivity.class);
				startActivity(i);
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		});
    	actionBar.addTab(t);
    	
    }
    
    private <T extends Fragment> void addTab(ActionBar actionBar, String title, String name, Class<T> c)
    {
    	Tab tab = actionBar.newTab();
		tab.setText(title);
		tab.setTabListener(new GTTabListener<T>(this, name, c));
    	
    	actionBar.addTab(tab);
    }
    
    /**
     * Registrerer telefonen hos GCM-server, hvis ikke allerede registrert
     */
    private void registerThisDevice()
    {
    	final String SENDER_ID = "579021654488";
    	final String TAG = "";
    	
    	GCMRegistrar.checkDevice(this);
    	GCMRegistrar.checkManifest(this);
    	final String regId = GCMRegistrar.getRegistrationId(this);
    	if(regId.equals(""))
    	{
    		GCMRegistrar.register(this, SENDER_ID);
    	} else {
    		Log.v(TAG, "Already registered");
    		ServiceTestClass.register(this, "name", "email", regId);
    	}
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
