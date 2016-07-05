package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

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
		
		EditTextPreference prefUserName = (EditTextPreference)findPreference("prefUsername");
		EditTextPreference prefPassword = (EditTextPreference)findPreference("prefPassword");
		EditTextPreference prefResource = (EditTextPreference)findPreference("prefResource");
		EditTextPreference prefServer = (EditTextPreference)findPreference("prefServer");
		EditTextPreference prefPort = (EditTextPreference)findPreference("prefPort");
		
		prefUserName.setText(session.user);
		prefPassword.setText(session.password);
		prefResource.setText(session.resource);
		prefServer.setText(session.ip);
		prefPort.setText("" + session.port);
	}
}
