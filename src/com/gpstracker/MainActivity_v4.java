package com.gpstracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.gpstracker.tab.GTTabListener_v4;

public class MainActivity_v4 extends FragmentActivity 
{
	public static FragmentActivity activity;
	public static Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        GTTabListener_v4.initTabs(this);
        checkIfGoogleAccountExists();//Om ingen er tilstedet får bruker beskjed om å opprette en
        registerThisDevice(); //Registrerer enheten hos server
        
        activity = this;
    }

    private void checkIfGoogleAccountExists()
    {
    	AccountManager accountManager = AccountManager.get(this);
    	Account[] acc = accountManager.getAccountsByType("com.google");
    	if(acc.length == 0)
    		startActivity(new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT));
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
    	
    	//TEST
    	if(GCMRegistrar.getRegistrationId(this).equals(""))
    		Toast.makeText(this, "IKKE REGISTRERT", Toast.LENGTH_LONG).show();
    	else
    		Toast.makeText(this, "REGISTRERT", Toast.LENGTH_LONG).show();
    }
}//end activity