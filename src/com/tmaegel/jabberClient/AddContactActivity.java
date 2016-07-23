package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;

import android.app.Activity;

import android.content.Intent;

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
		
		int contactId;
		Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
        	contactId = bundle.getInt("contact-id");
        	
        	if(contactId > 0) {
        		Contact contact = MainActivity.instance.dbCon.selectContact(contactId);
        		jidText.setText(contact.jid, TextView.BufferType.EDITABLE);
        		jidText.setEnabled(false);
        		nameText.setText(contact.name, TextView.BufferType.EDITABLE);
        		groupText.setText(contact.group, TextView.BufferType.EDITABLE);
        	}
        }

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
	 			localIntent.putExtra("jid", jidText.getText().toString());
				localIntent.putExtra("name", nameText.getText().toString());
				localIntent.putExtra("group", groupText.getText().toString());
				clearText();
	 			setResult(RESULT_OK, localIntent);
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
