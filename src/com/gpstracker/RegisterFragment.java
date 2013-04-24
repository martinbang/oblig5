package com.gpstracker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;

public class RegisterFragment extends Fragment implements OnClickListener
{
	private Button loginBtn;
	private EditText usrField;
	private Configuration conf;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_register, container, false);
		
		//finner widgets
		loginBtn = (Button)view.findViewById(R.id.button_register_login);
		usrField = (EditText)view.findViewById(R.id.editText_register_username);
				
		//finner instillinger
		conf = Configuration.getCurrentConfiguration(getActivity());
		usrField.setText(conf.getUserName());
		
		//setter klikklistener
		loginBtn.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) 
	{
		Activity activity = getActivity();
		Configuration conf = Configuration.getCurrentConfiguration(activity);
		String usrName = usrField.getText().toString();
		if(!usrName.equals(""))
		{
			conf.setUserName(usrName);
			conf.setRegistered(true);
			conf.commit(activity);
			ServiceTestClass.register(activity, conf.getUserName());
	        GTTabListener.initTabs(activity);
//			MenuItem powerbtn = (MenuItem)view.findViewById(R.id.powerService);
//			powerbtn.setIcon(android.R.drawable.button_onoff_indicator_on);
		}else
		{
			Toast.makeText(activity, getResources().getString(R.string.register_fragment_name_not_set), Toast.LENGTH_SHORT).show();
		}
	}	
}
