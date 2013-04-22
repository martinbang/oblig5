package com.gpstracker;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

public class GTTabListener<T extends Fragment> implements TabListener
{
	private Fragment fragment;
	private final Context context;
	private String tag;
	private Class<T> fragmentClass;
	
	public GTTabListener(Context context, String tag, Class<T> cls)
	{
		this.context = context;
		this.tag = tag;
		this.fragmentClass = cls;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
		
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

}
