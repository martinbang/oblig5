package com.gpstracker.conf;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.gpstracker.R;
import com.gpstracker.map.TrackerMapActivity;

public class ConfigurationFragment extends Fragment
{
	View view;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_configuration, container, false);
		setCurrentConfigurations();
		return view;
	}
	
	private void setCurrentConfigurations()
	{
		EditText meters = (EditText)view.findViewById(R.id.editText_meters);
		Switch onLocationChange = (Switch)view.findViewById(R.id.switch_send_on_location_changed);
		EditText minutes = (EditText)view.findViewById(R.id.editText_minutes);
		Switch onMinutesPassed = (Switch)view.findViewById(R.id.switch_send_on_time_passed);
		
		Activity activity = getActivity();
		meters.addTextChangedListener(new WidgetChangeListener(meters, activity));
		minutes.addTextChangedListener(new WidgetChangeListener(minutes, activity));
		onLocationChange.setOnCheckedChangeListener(new WidgetChangeListener(view, activity));
		onMinutesPassed.setOnCheckedChangeListener(new WidgetChangeListener(view, activity));
		
		
		Configuration conf = Configuration.getCurrentConfiguration(getActivity());
		
		meters.setText(conf.getMetersBetweenSend() + "");
		boolean onLocationChanged = conf.getSendOnUserMoved();
		onLocationChange.setChecked(onLocationChanged);
		meters.setEnabled(onLocationChanged);
		
		minutes.setText(conf.getMinutesBetweenSend() + "");
		boolean onTimePassed = conf.getSendOnTimePassed();
		onMinutesPassed.setChecked(onTimePassed);
		minutes.setEnabled(onTimePassed);
		
		
		
			
			
	}

}
