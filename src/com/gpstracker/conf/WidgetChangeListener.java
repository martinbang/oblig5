package com.gpstracker.conf;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

import com.gpstracker.R;

public class WidgetChangeListener implements TextWatcher, OnCheckedChangeListener
{
	private View view;
	private Context context;
	
	/**
	 * 
	 * @param v hvis den skal høre på en Switch, set in fragment. hvis den skal høre på en EditText, sett in EditText
	 * @param context
	 */
	public WidgetChangeListener(View v, Context context)
	{
		view = v;
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) 
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Når tekst på et av feltene blir endret, lagres det i konfigurasjon
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		try
		{
			Configuration conf = Configuration.getCurrentConfiguration(context);
			EditText et = (EditText)view;
			switch(view.getId())
			{
			case R.id.editText_meters:
				conf.setMetersBetweenSend(Integer.parseInt(et.getText().toString()));
				break;
			case R.id.editText_minutes:
				conf.setMinutesBetweenSend(Integer.parseInt(et.getText().toString()));
				break;
			}
			conf.commit(context);
			
		} catch(NumberFormatException e)
		{
			Log.d("NUMBER FORMAT EXCEPTION", e.getLocalizedMessage());
		}
		
	}

	/**
	 * Når en Switch forandres i viewet settes konfigurasjonparameteren den representerer til Switchens verdi.
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		Switch s = (Switch)buttonView;
		
		Configuration conf = Configuration.getCurrentConfiguration(context);
		EditText et = null;
		switch(s.getId())
		{
		case R.id.switch_send_on_location_changed:
			conf.setSendOnUserMoved(isChecked);
			et = (EditText)view.findViewById(R.id.editText_meters);
			break;
			
		case R.id.switch_send_on_time_passed:
			conf.setSendOnTimePassed(isChecked);
			et = (EditText)view.findViewById(R.id.editText_minutes);
			break;
		}
		
		if(et != null)
			et.setEnabled(isChecked); //Felt som bestemmer meter eller miutt skal ikke være tilgjengelig switches verdi er false.
		conf.commit(context);
	}
}
