package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;

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
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;

public class ConversationActivity extends Activity {

	// public references
	public ConversationAdapter convAdapter;
	public Network net;

	// private references
	private ListView listView;
	private EditText chatText;
	private ImageButton buttonSend;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		
		MainActivity.convAct = this;

		buttonSend = (ImageButton)findViewById(R.id.conv_send);
		chatText = (EditText)findViewById(R.id.conv_input_message);
		listView = (ListView)findViewById(R.id.conv_history);

		Bundle b = getIntent().getExtras();
		convAdapter = new ConversationAdapter(this, R.layout.single_message, b.getString("jid"));

		listView.setAdapter(convAdapter);

		buttonSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Stream msg = new Stream(Constants.MESSAGE);
				// msg.setBody(chatText.getText().toString());
				// convAdapter.addMessageToHistory(msg,  true);
				
				chatText.setText("", TextView.BufferType.EDITABLE);
			}
		});
	}

	/** Called when rebuild activity, switch in landscape mode */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}
}
