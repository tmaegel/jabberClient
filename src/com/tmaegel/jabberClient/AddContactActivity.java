package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;

import android.app.Activity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TextView;

public class AddContactActivity extends Activity {

	private EditText jidText, nameText, groupText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);

		jidText = (EditText)findViewById(R.id.acont_jid_edit);
		nameText = (EditText)findViewById(R.id.acont_name_edit);
		groupText = (EditText)findViewById(R.id.acont_group_edit);
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
				MainActivity.net.contact = new Contact(jidText.getText().toString(), nameText.getText().toString(), groupText.getText().toString());
				clearText();
				MainActivity.net.sendRequest(Constants.C_ROSTER_SET);
				finish();
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void clearText() {
		jidText.setText("", TextView.BufferType.EDITABLE);
		nameText.setText("", TextView.BufferType.EDITABLE);
		groupText.setText("", TextView.BufferType.EDITABLE);
	}
}
