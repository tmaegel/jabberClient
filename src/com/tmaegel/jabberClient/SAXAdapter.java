package com.tmaegel.jabberClient;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXAdapter extends DefaultHandler {

	private XMLParser parent;

	boolean challenge = false;
	boolean isMessage = false;

	public String handshake;

	public SAXAdapter(XMLParser parent) {
		this.parent = parent;
	}

	@Override
	public void startElement(String uri, String localName, String tag, Attributes attributes)
		throws SAXException {
		if(tag.equalsIgnoreCase("stream:stream")) {
			/*Log.d("jabberClient", "to (Stream): " + attributes.getValue("to"));
			//Log.d("jabberClient", "from (Stream): " + attributes.getValue("from"));
			Log.d("jabberClient", "version (Stream): " + attributes.getValue("version"));
			Log.d("jabberClient", "xmlns (Stream): " + attributes.getValue("xmlns"));*/
		} else if(tag.equalsIgnoreCase("challenge")) {
			Log.d("jabberClient", "BEGIN " + tag);
			Log.d("jabberClient", "xmlns=" + attributes.getValue("xmlns"));
			challenge = true;
		} else if(tag.equalsIgnoreCase("message")) {
			// Log.d("jabberClient", "BEGIN " + tag);
			parent.msg = new Message(attributes.getValue("to"), attributes.getValue("from"), true);
			// Log.d("jabberClient", "id=" + attributes.getValue("id"));
			// Log.d("jabberClient", "type=" + attributes.getValue("type"));
			isMessage = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String tag)
		throws SAXException {
		if(tag.equalsIgnoreCase("challenge")) {
			Log.d("jabberClient", "END " + tag);
			parent.result = handshake;
		} else if(tag.equalsIgnoreCase("message")) {
			// Log.d("jabberClient", "END " + tag);
		}
	}

	@Override
	public void characters(char ch[], int start, int length)
		throws SAXException {
		if(challenge) {
			handshake = new String(ch, start, length);
			Log.d("jabberClient", "Handshake=" + handshake);
			challenge = false;
		} else if(isMessage) {
			parent.msg.setMessage(new String(ch, start, length));
			isMessage = false;
		}
	}
}
