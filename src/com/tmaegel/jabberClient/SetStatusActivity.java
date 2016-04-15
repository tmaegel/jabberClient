package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;

import android.app.Activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SetStatusActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_status);
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
				Log.d("jabberClient", "Set status ...");
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
