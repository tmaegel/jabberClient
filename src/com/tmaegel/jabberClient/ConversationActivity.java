package com.tmaegel.jabberClient;

import android.os.Bundle;

import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ConversationActivity extends Activity {

	private ConversationAdapter convAdapter;
	private ListView listView;
	private EditText chatText;
	private Button buttonSend;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);

		buttonSend = (Button)findViewById(R.id.conv_send);
		listView = (ListView)findViewById(R.id.conv_history);
		convAdapter = new ConversationAdapter(this, R.layout.single_message);
		listView.setAdapter(convAdapter);	
		
		addMessage("Das ist eine Testnachticht...", true);
		addMessage("Testnachricht1", true);
		addMessage("Testnachricht2", true);
		addMessage("Testnachricht3", true);
		addMessage("Testnachricht4", true);
		addMessage("Das ist eine Testnachticht...", true);
		addMessage("Hallooooo", false);
		addMessage(":-)", true);
	}

	/** Called when rebuild activity, switch in landscape mode */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	
	}

	private boolean addMessage(String text, boolean left) {
		convAdapter.add(new Message(text, left));
		return true;	
	}
}
