package com.gpstracker.tab;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gpstracker.conf.Configuration;
import com.gpstracker.conf.ConfigurationFragment;
import com.gpstracker.log.LogFragment;
import com.gpstracker.map.TrackerMapActivity;
import com.gpstracker.register.RegisterFragment;


@SuppressLint("NewApi")
public class GTTabListener<T extends Fragment> implements TabListener
{
	private Fragment fragment;
	private final Context context;
	private String tag;
	private Class<T> fragmentClass;
	
	/**
	 * Konstruktør 
	 * @param context
	 * @param tag
	 * @param cls
	 */
	public GTTabListener(Context context, String tag, Class<T> cls)
	{
		this.context = context;
		this.tag = tag;
		this.fragmentClass = cls;
	}
	
	
	
	
	public static void initTabs(Activity activity)
	{
		initTabs(activity, activity.getActionBar());
	}
	
    public static void initTabs(final Context context, ActionBar actionBar) throws IllegalStateException
    {
    	Configuration conf = Configuration.getCurrentConfiguration(context);
    	
    	//ActionBar actionBar = activity.getActionBar();
    	actionBar.removeAllTabs();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	int currentApiLevel = android.os.Build.VERSION.SDK_INT;
    	
    	if(!conf.getRegistered() && currentApiLevel >= 11)
    		addTab(context, actionBar, "Registrer", "registration", RegisterFragment.class);
    	
    	addTab(context, actionBar, "Log", "log", LogFragment.class);
    	
    	if(conf.getRegistered())
    		addMapTab(context, actionBar);
    	
    	addTab(context, actionBar, "Instillinger", "configurations", ConfigurationFragment.class);
    }
    
    
    private static void addMapTab(final Context context, ActionBar actionBar)
    {
    	Tab t = actionBar.newTab();
    	t.setText("Map");
    	t.setTabListener(new TabListener() 
    	{
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) 
			{
				Intent i = new Intent(context, TrackerMapActivity.class);
				context.startActivity(i);
				Log.v("Map startet", "Map Startet");
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {}
		});
    	actionBar.addTab(t);
    }
    
    private static <T extends Fragment> void addTab(Context context, ActionBar actionBar, String title, String name, Class<T> c)
    {
    	Tab tab = actionBar.newTab();
		tab.setText(title);
		tab.setTabListener(new GTTabListener<T>(context, name, c));
    	
    	actionBar.addTab(tab);
    }
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		if(fragment == null)
		{
			fragment = Fragment.instantiate(context, fragmentClass.getName());
			ft.add(android.R.id.content, fragment, tag);
		} else 
		{
			ft.attach(fragment);
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction ft) 
	{
		if(fragment != null)
			ft.detach(fragment);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
}
