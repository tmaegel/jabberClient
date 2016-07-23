package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

import android.os.Bundle;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.preference.PreferenceActivity;
import android.preference.EditTextPreference;

public class PreferencesActivity extends PreferenceActivity {
	
	public Session session;
	
	public EditTextPreference prefUserName, prefPassword, prefResource, prefServer, prefPort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		session = MainActivity.instance.dbCon.selectSession();
		
		prefUserName = (EditTextPreference)findPreference("prefUsername");
		prefPassword = (EditTextPreference)findPreference("prefPassword");
		prefResource = (EditTextPreference)findPreference("prefResource");
		prefServer = (EditTextPreference)findPreference("prefServer");
		prefPort = (EditTextPreference)findPreference("prefPort");
		
		prefUserName.setText(session.user);
		prefPassword.setText(session.password);
		prefResource.setText(session.resource);
		prefServer.setText(session.ip);
		prefPort.setText("" + session.port);
	}
	
	/** Called when create menu  */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.successmenu, menu);

		return true;
	}
	
	/** Listening to click on menu items */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/** Start conversation */
			case R.id.opt_success:
				Intent localIntent = new Intent();
	 			localIntent.putExtra("user", prefUserName.getText().toString());
				localIntent.putExtra("password", prefPassword.getText().toString());
				localIntent.putExtra("resource", prefResource.getText().toString());
				localIntent.putExtra("ip", prefServer.getText().toString());
				localIntent.putExtra("port", prefPort.getText().toString());
	 			setResult(RESULT_OK, localIntent);
				finish();
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
