package com.tmaegel.jabberClient;

import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.EditTextPreference;

public class PreferencesActivity extends PreferenceActivity {
	
	public Session session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		session = MainActivity.instance.dbCon.selectSession();
		
		EditTextPreference prefUserName = (EditTextPreference)findPreference(getResources().getString(R.string.pref_user_name));
		EditTextPreference prefPassword = (EditTextPreference)findPreference(getResources().getString(R.string.pref_password));
		EditTextPreference prefResource = (EditTextPreference)findPreference(getResources().getString(R.string.pref_resource));
		EditTextPreference prefServer = (EditTextPreference)findPreference(getResources().getString(R.string.pref_server));
		EditTextPreference prefPort = (EditTextPreference)findPreference(getResources().getString(R.string.pref_port));
		
		prefUserName.setText(session.user);
		prefPassword.setText(session.password);
		prefResource.setText(session.resource);
		prefServer.setText(session.domain);
		prefPort.setText("" + session.port);
	}
}
