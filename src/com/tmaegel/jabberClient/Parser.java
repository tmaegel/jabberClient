package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

import java.io.StringReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Parser {

	private XmlPullParserFactory pullParserFactory;
	private XmlPullParser parser;
	private Stanza stanza;

	private String tag = "";

	public Parser() {
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			parser = pullParserFactory.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public Stanza parseXML(String stream) {
		// Log.d("jabberClient", "Parsing ...");
		Log.d("jabberClient", "" + stream);

		try {
			parser.setInput(new StringReader(stream));
			int eventType = parser.getEventType();

			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
					case XmlPullParser.START_DOCUMENT:
						// Log.d("jabberClient", ">>> Stream start");
						stanza = new Stanza();
						break;
					case XmlPullParser.START_TAG:
						tag = parser.getName();
						for(int i = 0; i < Constants.TAGS.length; i++) {
							if(Constants.TAGS[i].equals(tag)) {
								parseAttributes();
								break;
							}
						}
						break;
					case XmlPullParser.TEXT:
						parseText();
						break;
					case XmlPullParser.END_TAG:
						// Log.d("jabberClient", "< Tag " + parser.getName() + " end");
						break;
					case XmlPullParser.END_DOCUMENT:
						// Log.d("jabberClient", "<<< Stream end");
						break;
				}

				eventType = parser.next();
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}

		// listStanza();

		return stanza;
	}

	public void parseAttributes() {
		Contact contact = new Contact();
		boolean isContent = false;

		/**< Parsing common attributes (meta) */
		for(int a = 0; a < parser.getAttributeCount(); a++) {
			String attr = parser.getAttributeName(a);
			String val = parser.getAttributeValue(a);
			switch(attr) {
				case "xmlns":
					stanza.xmlns = val;
					switch(stanza.xmlns) {
						/**
						 * Initialization and Authentifaction
						 */
						/**< initial stram */
						case "http://etherx.jabber.org/streams":
							Log.d(Constants.LOG_TAG, "Detect namespace STREAM");
							stanza.namespace = Constants.NS_STREAM;
							break;
						 /**< tls auth */
 						case "urn:ietf:params:xml:ns:xmpp-tls":
							Log.d(Constants.LOG_TAG, "Detect namespace TLS_AUTH");
 							stanza.namespace = Constants.NS_TLS_AUTH;
 							break;
						/**< sasl auth */
						case "urn:ietf:params:xml:ns:xmpp-sasl":
							Log.d(Constants.LOG_TAG, "Detect namespace SALS_AUTH");
							stanza.namespace = Constants.NS_SASL_AUTH;
							break;
						/**< Resource binding */
						case "urn:ietf:params:xml:ns:xmpp-bind":
							Log.d(Constants.LOG_TAG, "Detect namespace RESOUCE_BIND");
							stanza.namespace = Constants.NS_RESOURCE_BIND;
							break;
						/**< Session */
						case "urn:ietf:params:xml:ns:xmpp-session":
							Log.d(Constants.LOG_TAG, "Detect namespace SESSION");
							stanza.namespace = Constants.NS_SESSION;
							break;
						/**< Compress */
						case "http://jabber.org/features/compress":
							Log.d(Constants.LOG_TAG, "Detect namespace COMPRESS");
							stanza.namespace = Constants.NS_COMPRESS;
							break;


						/**
						 * IQ
						 */
						/**< Roster */
						case "jabber:iq:roster":
							stanza.namespace = Constants.NS_ROSTER;
							break;
						/**
						 * PRESENCE
						 */
						 // ...
						 /**
						 * MESSAGE
						 */
						 // ...
					}
					break;
				case "id":
					stanza.id = val;
					break;
				case "type":
					stanza.type = val;
					break;
			/*	case "to":
					stanza.to = val;
					break;
				case "from":
					stanza.from = val;
					break;
				case "ver":
					stanza.ask = val;
					break;
			*/
			}
		}

		for(int b = 0; b < parser.getAttributeCount(); b++) {
			String attr = parser.getAttributeName(b);
			String val = parser.getAttributeValue(b);
			switch(stanza.namespace) {
				/**
				 * Initialization and Authentifaction
				 */
				/**< sasl auth */
				case Constants.NS_SASL_AUTH:
					if(tag.equals("success")) {
						stanza.success = true;
					}
					break;
				/**
				 * IQ
				 */
				/**< Roster */
				case Constants.NS_ROSTER:
					if(stanza.type.equals("result")) {
						stanza.subtype = Constants.S_ROSTER_RESPONSE;
					} else if(stanza.type.equals("set")) {
						stanza.subtype = Constants.S_ROSTER_PUSH;
					} else if(stanza.type.equals("error")) {
						stanza.subtype = Constants.S_ROSTER_ERROR;
					}

					if(attr.equals("jid")) {
						contact.jid = val;
						isContent = true;
					} else if(attr.equals("name")) {
						contact.name = val;
						isContent = true;
					} else if(attr.equals("subscription")) {
						contact.subscription = val;
						isContent = true;
					}
					break;

				/**
				 * PRESENCE
				 */

				 // ...

				 /**
				 * MESSAGE
				 */

				 // ...
			}
		}

		if(isContent) {
			stanza.contacts.add(contact);
			isContent = false;
		}
	}

	public void parseText() {
		// Log.d(Constants.LOG_TAG, "Last tag" + tag);
		switch(stanza.namespace) {
			/**
			 * Initialization and Authentifaction
			 */
			/**< initial stram */
			case Constants.NS_STREAM:

				break;
			/**< tls auth */
			case Constants.NS_TLS_AUTH:
				break;
			/**< sasl auth */
			case Constants.NS_SASL_AUTH:
				if(tag.equals("mechanism")) {
					stanza.mechanisms.add(parser.getText());
				}
				break;
			/**< Resource binding */
			case Constants.NS_RESOURCE_BIND:
				if(tag.equals("jid")) {
					stanza.jid = parser.getText();
				}
				break;
			/**< Compress */
			case Constants.NS_COMPRESS:
				break;

			/**
			 * IQ
			 */
			/**< Roster */
			case Constants.NS_ROSTER:
				if(tag.equals("group")) {
					stanza.contacts.lastElement().group = parser.getText();
				}
				break;

			/**
			 * PRESENCE
			 */

			 // ...

			 /**
			 * MESSAGE
			 */

			 // ...
		}
	}

	/*public void listStanza() {
		if(stanza != null) {
			Log.d("jabberClient", "> LIST ...");
			Log.d(Constants.LOG_TAG, "	xmlns = " + stanza.xmlns);
			Log.d(Constants.LOG_TAG, "	id = " + stanza.id);
			Log.d(Constants.LOG_TAG, "	type = " + stanza.type);
			Log.d(Constants.LOG_TAG, "	to = " + stanza.to);
			Log.d(Constants.LOG_TAG, "	from = " + stanza.from);
			Log.d(Constants.LOG_TAG, "	ver = " + stanza.ver);
			Log.d(Constants.LOG_TAG, "	ask = " + stanza.ask);

			for(int i = 0; i < stanza.contacts.size(); i++) {
				Log.d(Constants.LOG_TAG, "	jid = " + stanza.contacts.get(i).jid);
				Log.d(Constants.LOG_TAG, "	name = " + stanza.contacts.get(i).name);
				Log.d(Constants.LOG_TAG, "	group = " + stanza.contacts.get(i).group);
				Log.d(Constants.LOG_TAG, "	subscription = " + stanza.contacts.get(i).subscription + "\n");
			}
		}
	}*/
}
