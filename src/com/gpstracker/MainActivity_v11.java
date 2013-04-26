package com.gpstracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceClass;
import com.gpstracker.tab.GTTabListener;

public class MainActivity_v11 extends Activity
{
	public static Activity activity;
	public static Handler handler = new Handler();
	public static Menu actionBarMenu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
       	GTTabListener.initTabs(this);
        
        registerThisDevice(); //Registrerer enheten hos server
        
        activity = this;
    }
    
    /**
     * Registrerer telefonen hos GCM-server, hvis ikke allerede registrert
     */
    private void registerThisDevice()
    {
    	final String SENDER_ID = "579021654488";
    	final String TAG = "REGISTER";
    	
    	GCMRegistrar.checkDevice(this); //Vertifiserer at enheten støtter GCM
    	GCMRegistrar.checkManifest(this); //Vertifiserer at manifestet møter kriteriene
    	final String regId = GCMRegistrar.getRegistrationId(this); //Skjekker om enheten er registrert. returnerer nøkkelen hvis den er registrert
    	if(regId.equals(""))
    	{
    		GCMRegistrar.register(this, SENDER_ID); //Registrerer enheten
    		
    	} else     	
    		Log.v(TAG, "Already registered");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        /*Setter ikonet til start-knappen etter om du er registrert eller ei*/
        MenuItem powerService = menu.getItem(0);
        actionBarMenu = menu; //Oppretter en statisk instans av menyen, slik at den kan nåes fra flere steder i programmet
        if(Configuration.getCurrentConfiguration(this).getRegistered()) //Skjekker om du er registrert på server
        	powerService.setIcon(android.R.drawable.button_onoff_indicator_on); //Setter ikonet til serviceknappen
        else
        	powerService.setIcon(android.R.drawable.button_onoff_indicator_off);
        
        return true;
    }
    
    /**
     * Skjekker ikke hvilket item som ble valgt da det bare finnes ett.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Configuration conf = Configuration.getCurrentConfiguration(this); //Finner innstillingene
    	
    	if(conf.getUserName().equals("")) //Du skal ikke kunne registrere deg fra actionbar om du ikke har registrert deg tidligere. Dette fordi serveren trenger et navn
    		Toast.makeText(this, getResources().getString(R.string.register_fragment_name_not_set), Toast.LENGTH_SHORT).show();//Informerer bruker om at det ikke er satt noe navn
    	
    	else if(item.getItemId() == R.id.powerService)
    	{
    		if(conf.getRegistered())//hvis du er registrert skal du avregistreres.
    		{
    			item.setIcon(android.R.drawable.button_onoff_indicator_off);
    			ServiceClass.unRegister(this);
    		} else//Hvis du ikke er registrert skal du registreres
    		{
    			item.setIcon(android.R.drawable.button_onoff_indicator_on);
    			ServiceClass.register(this, conf.getUserName());
    		}
    		GTTabListener.initTabs(this);//Fanene blir oppdatert etter om du er pålogget eller ei
    	}
    		
    	return super.onOptionsItemSelected(item);
    }
}
