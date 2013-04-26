package com.gpstracker.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gpstracker.MainActivity;
import com.gpstracker.MainActivity_v11;
import com.gpstracker.R;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceClass;
import com.gpstracker.tab.GTTabListener;

@SuppressLint("NewApi")
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

	/**
	 * Registrerer bruker hvis navn er satt
	 */
	@Override
	public void onClick(View v) 
	{
		Activity activity = getActivity();//Da vi skal bruke activity flere ganger tenker jeg det er like greit å instansiere ett objekt av det.
		Configuration conf = Configuration.getCurrentConfiguration(activity);//Finner instillinger
		String usrName = usrField.getText().toString(); //henter brukernavn fra tekstfeltet i appen
		if(!usrName.equals(""))
		{
			conf.setUserName(usrName);//setter brukernavnet til det som stod i tekstfeltet
			conf.setRegistered(true);//setter at bruker er registrert
			conf.commit(activity);//oppdaterer
			ServiceClass.register(activity, conf.getUserName());//Registrerer navnet på server
	        GTTabListener.initTabs(activity);//Registreringsfanen forsvinner. kart-fanen blir synlig
			MainActivity_v11.actionBarMenu.getItem(0).setIcon(android.R.drawable.button_onoff_indicator_on);//setter ikonet til menuitem til på. Mulig fordi vi gjorde den statisk i main activity
		}else
		{
			Toast.makeText(activity, getResources().getString(R.string.register_fragment_name_not_set), Toast.LENGTH_SHORT).show(); //om navnet ikke var satt skal ikke registreringen utføres
		}
	}	
}
