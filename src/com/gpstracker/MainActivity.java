package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class MainActivity extends Activity {
	
	
	public static Activity activity;
	public static Handler handler = new Handler();
	public static Menu actionBarMenu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
         * Finner starter aktivitet etter hva slag os telefonen kjører
         */
    	Intent i;
    	if(android.os.Build.VERSION.SDK_INT >= 11)
    		i = new Intent(this, MainActivity_v11.class);
    	else
    		i = new Intent(this, MainActivity_v4.class);
    	
    	startActivity(i);
    }
}//end activity
