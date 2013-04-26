package com.gpstracker.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gpstracker.R;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceClass;
import com.gpstracker.tab.GTTabListener_v4;

public class RegisterFragment_v4 extends Fragment implements OnClickListener
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

	/**
	 * Registrerer bruker hvis navn er satt
	 */
	@Override
	public void onClick(View v) 
	{
		FragmentActivity activity = getActivity();//Da vi skal bruke activity flere ganger tenker jeg det er like greit å instansiere ett objekt av det.
		String usrName = usrField.getText().toString(); //henter brukernavn fra tekstfeltet i appen
		
		if(!usrName.equals(""))
		{
			ServiceClass.register(activity, usrName);//Registrerer navnet på server
	        GTTabListener_v4.initTabs(activity);//Registreringsfanen forsvinner. kart-fanen blir synlig
		}else
		{
			Toast.makeText(activity, getResources().getString(R.string.register_fragment_name_not_set), Toast.LENGTH_SHORT).show(); //om navnet ikke var satt skal ikke registreringen utføres
		}
	}	
}
