package com.gpstracker.tab;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gpstracker.MainActivity_v4;
import com.gpstracker.R;
import com.gpstracker.conf.Configuration;
import com.gpstracker.conf.ConfigurationFragment_v4;
import com.gpstracker.log.LogFragment_v4;
import com.gpstracker.map.TrackerMapActivity;
import com.gpstracker.register.RegisterFragment_v4;

public class GTTabListener_v4 
{
	public static void initTabs(FragmentActivity activity)
	{
		initTabs(activity, null);
	}
	
	/**
	 * 
	 * @param activity
	 * @param fragment 
	 */
    public static void initTabs(FragmentActivity activity, android.support.v4.app.Fragment fragment)
    {

    	Configuration conf = Configuration.getCurrentConfiguration(activity);
    	Button register_map = (Button) activity.findViewById(R.id.v4tab_button_register);
    	Button messages = (Button)activity.findViewById(R.id.v4tab_button_log);
    	Button configurations = (Button)activity.findViewById(R.id.v4tab_button_configurations);
    	
    	register_map.setOnClickListener(getV4OnClickListener());
    	messages.setOnClickListener(getV4OnClickListener());
    	configurations.setOnClickListener(getV4OnClickListener());
    	
    	if(conf.getRegistered() && fragment == null)
    	{
    		register_map.setText(activity.getResources().getString(R.string.tab_map));
    		fragment = new LogFragment_v4();
        	register_map.setEnabled(true);
        	configurations.setEnabled(true);
        	messages.setEnabled(false);
    	} 
    	else if(fragment == null)
    	{
    		register_map.setText(activity.getResources().getString(R.string.tab_register));
    		fragment = new RegisterFragment_v4();
        	register_map.setEnabled(false);
        	messages.setEnabled(true);
        	configurations.setEnabled(true);
    	} 
    	else if(fragment instanceof ConfigurationFragment_v4)
    	{
    		configurations.setEnabled(false);
    		register_map.setEnabled(true);
    		messages.setEnabled(true);
    	}
    	else if(fragment instanceof LogFragment_v4)
    	{
    		configurations.setEnabled(true);
    		register_map.setEnabled(true);
    		messages.setEnabled(false);
    	}
    	
		FragmentManager fm = activity.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
		
		transaction.replace(R.id.v4contentFragment, fragment);
		transaction.commit();
    	
    }
    
    
    private static OnClickListener getV4OnClickListener()
    {
    	OnClickListener oncl = new OnClickListener()
    	{
			@Override
			public void onClick(View v) 
			{
				FragmentActivity a = MainActivity_v4.activity;
				
				if(v.getId() == R.id.v4tab_button_register && Configuration.getCurrentConfiguration(a.getBaseContext()).getRegistered())
				{
					Intent map = new Intent(a.getApplicationContext(), TrackerMapActivity.class);
					a.startActivity(map);
				}
				else if(v.getId() == R.id.v4tab_button_configurations)
					initTabs(a, new ConfigurationFragment_v4());
				else if(v.getId() == R.id.v4tab_button_log)
				{
					initTabs(a, new LogFragment_v4());
				} else
					initTabs(a);
			}
    	};
    	
    	return oncl;
    }

}
