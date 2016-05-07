package com.tmaegel.jabberClient;

import java.util.Vector;
import java.util.HashMap;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXAdapter extends DefaultHandler {

	private boolean iq, message, presence;

	private XMLParser parser;
	private Stanza stanza;
	//private ItemObject content;

	private String tmp;

	public SAXAdapter(XMLParser parser) {
		this.parser = parser;
		this.iq = false;
		this.message = false;
		this.presence = false;
	}

	public Stanza getStanza() {
		return stanza;
	}

	@Override
	public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
		/**  reset */
		tmp = "";

		tag  = tag.toLowerCase();
		switch(tag) {
			/**< IQ */
			case Constants.TAG_IQ:
				Log.d(Constants.LOG_TAG, "New stanza object");
				Log.d(Constants.LOG_TAG, "Detect IQ begin");
				//stanza = new Stanza(Constants.TAG_IQ, 1);
				iq = true;
				break;
			/**< MESSAGE */
			case Constants.TAG_MESSAGE:
				Log.d(Constants.LOG_TAG, "New stanza object");
				Log.d(Constants.LOG_TAG, "Detect MESSAGE begin");
				//stanza = new Stanza(Constants.TAG_MESSAGE, 2);
				message = true;
				break;
			/**< PRESNCE */
			case Constants.TAG_PRESENCE:
				Log.d(Constants.LOG_TAG, "New stanza object");
				Log.d(Constants.LOG_TAG, "Detect PRESENCE begin");
				//stanza = new Stanza(Constants.TAG_PRESENCE, 3);
				presence = true;
				break;
		}

		if(iq) {
			/*for(int i = 0; i < Constants.COMMON_IQ_ATTR.length; i++) {
				String attr = Constants.COMMON_IQ_ATTR[i];
				String val = attributes.getValue(attr);
				if(val != null) {
					Log.d(Constants.LOG_TAG, "Put common attr " +  attr + " = " + val);
					stanza.common.add(new Item(attr, val));
				}
			}
			content = new ItemObject();
			for(int i = 0; i < Constants.OPTIONAL_IQ_ATTR.length; i++) {
				String attr = Constants.OPTIONAL_IQ_ATTR[i];
				String val = attributes.getValue(attr);
				if(val != null) {
					Log.d(Constants.LOG_TAG, "Get content " +  attr + " = " + val);
					content.add(attr, val);
				}
			}*/
			// stanza.add(content);
		} else if(message) {
			Log.d(Constants.LOG_TAG, "In MESSAGE");
		} else if(presence) {
			Log.d(Constants.LOG_TAG, "In PRESENCE");
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		tmp = new String(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String tag) throws SAXException {
		tag  = tag.toLowerCase();
		switch(tag) {
			/**< IQ */
			case Constants.TAG_IQ:
				Log.d(Constants.LOG_TAG, "Detect IQ end");
				iq = false;
				break;
			/**< MESSAGE */
			case Constants.TAG_MESSAGE:
				Log.d(Constants.LOG_TAG, "Detect MESSAGE end");
				message = false;
				break;
			/**< PRESNCE */
			case Constants.TAG_PRESENCE:
				Log.d(Constants.LOG_TAG, "Detect PRESENCE end");
				presence = false;
				break;
		}

		/*if(iq) {
			for(int i = 0; i < Constants.OPTIONAL_IQ_ATTR.length; i++) {
				if(tag.equals(Constants.OPTIONAL_IQ_ATTR[i])) {
					if(!tmp.equals(null) || !tmp.equals("")) {
						Log.d(Constants.LOG_TAG, "Get content " +  tag + " = " + tmp);
						stanza.content.lastElement().add(tag, tmp);
					}
				}
			}
		} else if(message) {
			Log.d(Constants.LOG_TAG, "In MESSAGE");
		} else if(presence) {
			Log.d(Constants.LOG_TAG, "In PRESENCE");
		}*/
	}
}
