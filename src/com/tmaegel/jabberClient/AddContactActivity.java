package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;

import android.app.Activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AddContactActivity extends Activity {
	
	// private references
	public static Network net;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);
		
		net = MainActivity.net;
		
		net.sendRequest(Constants.C_ROSTER_SET);
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
				Log.d("jabberClient", "Add contact ...");
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
