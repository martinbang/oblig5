package com.gpstracker;

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
	
	/**
	 * Legger til de fanene som skal vises på aktuelt tidspunkt
	 * @param context
	 * @param actionBar
	 * @throws IllegalStateException
	 */
	public static void initTabs(final Context context, ActionBar actionBar) throws IllegalStateException
    {
    	Configuration conf = Configuration.getCurrentConfiguration(context);
    	
    	actionBar.removeAllTabs();//Fjerner fanene om det er noen
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); //navigationmode som lar oss scrolle om det er mange faner
    	
    	if(!conf.getRegistered())//Om bruker ikke er registrert skal registreringsfanen vises først
    		addTab(context, actionBar, "Registrer", "registration", RegisterFragment.class);
    	//Om bruker er registrert vil fanen for beskjeder vises
    	addTab(context, actionBar, "Log", "log", LogFragment.class);
    	//Hvis bruker er registrert skal fanen for kart vises
    	if(conf.getRegistered())
    		addMapTab(context, actionBar);
    	//konfigurasjons fanen skal vises som sist uansett
    	addTab(context, actionBar, "Instillinger", "configurations", ConfigurationFragment.class);
    }
    
	/**
	 * Da vi bruker Map v1. Er ikke dette ett fragment. Vi bruker derfor en egen metode for å legge aktiviteten til som 
	 * en fane
	 * @param context
	 * @param actionBar
	 */
    private static void addMapTab(final Context context, ActionBar actionBar)
    {
    	Tab t = actionBar.newTab();
    	t.setText("Map");
    	t.setTabListener(new TabListener() //Oppretter en tablistener som starter MapActivity. 
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
    	actionBar.addTab(t);//Legger fanen til i applikasjonens actionbar
    }
    
    /**
     * Generisk metode som legger til fragment som fane
     * @param context
     * @param actionBar
     * @param title
     * @param name
     * @param c
     */
    private static <T extends Fragment> void addTab(Context context, ActionBar actionBar, String title, String name, Class<T> c)
    {
    	Tab tab = actionBar.newTab();
		tab.setText(title);
		tab.setTabListener(new GTTabListener<T>(context, name, c));
    	
    	actionBar.addTab(tab);
    }
    
    
	/*
	 * Metodene under er kun for å starte de ulike fragmentene. Må implementeres 
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
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
