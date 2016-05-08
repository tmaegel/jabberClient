package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

import java.util.Vector;

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
	public Vector<String> mechanism;
	
	/**< roster specific attributes */
	public String ver = "";
	public Vector<Contact> items;
	
	/**< message specific attributes */
	// ...
	
	/**< presence specific attributes */
	// ...
	
	/*
	public String ask = ""; */

	public Stanza(int stanzaType) {
		this.stanzaType = stanzaType;
		this.mechanism = new Vector<String>();
		this.items = new Vector<Contact>();
	}
}
