package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
    	Intent i;
    	if(android.os.Build.VERSION.SDK_INT >= 11)
    		i = new Intent(this, MainActivity_v11.class);
    	else
    		i = new Intent(this, MainActivity_v4.class);
    	
    	startActivity(i);
    }
}//end activity
