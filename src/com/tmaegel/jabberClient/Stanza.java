package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

import java.util.Vector;

public class Stanza {

	// public int tag = -1;
	public int namespace = -1;
	public int subtype = -1;

	/**< common */
	public String xmlns = "";
	public String id = "";
	public String type = "";
	/* public String to = "";
	public String from = "";
	public String ver = "";
	public String ask = ""; */

	/**< sasl auth */
	public Vector<String> mechanisms;
	public boolean success = false;

	/**< resource binding */
	public String jid = "";

	/**< roster */
	public Vector<Contact> contacts;
	/**< message */
	// public Vector<Message> messages;

	public Stanza() {
		this.mechanisms = new Vector<String>();

		this.contacts = new Vector<Contact>();
	}

	public String getCommonAttribute(String attribute) {
		/*for(int i = 0; i < meta.size(); i++) {
			if(meta.get(i).getAttribute().equals(attribute)) {
				return meta.get(i).getValue();
			}
		}*/

		return null;
	}
}
