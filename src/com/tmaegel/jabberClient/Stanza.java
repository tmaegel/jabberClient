package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

public class Stanza {

	/** general stanza type (iq, presence, message) */
	public int stanzaType = -1;

	/**< common attribute for iq, presence, message */
	public String to = "";
	public String from = "";
	public String id = "";
	public String type = "";
	public String lang = "";

	// public String xmlns = "";

	/** inital stream negotiation */
	public ArrayList<String> mechanism;

	/**< roster specific attributes */
	public String ver = "";
	public ArrayList<Contact> items;

	/**< message specific attributes */
	public Message message;

	/**< presence specific attributes */
	// ...

	/*
	public String ask = ""; */

	public Stanza() {
		this.stanzaType = stanzaType;
		this.mechanism = new ArrayList<String>();
		this.items = new ArrayList<Contact>();

		message = new Message();
	}
}
