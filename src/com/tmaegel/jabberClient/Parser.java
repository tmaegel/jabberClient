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
	private int rootTag = 0;

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
		Log.d("jabberClient", "" + stream);

		try {
			parser.setInput(new StringReader(stream));
			int eventType = parser.getEventType();

			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
					case XmlPullParser.START_DOCUMENT:
						Log.d("jabberClient", ">>> New stream");
						rootTag = 0;
						break;
					case XmlPullParser.START_TAG:
						tag = parser.getName();
						parse(false);
						break;
					case XmlPullParser.TEXT:
						parse(true);
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

		return stanza;
	}
	
	/**
	 * @brief Parsing commonn attributes of iq, presence or message tag
	 */
	public void parseCommon() {
		stanza = new Stanza(rootTag);
		
		Log.d(Constants.LOG_TAG, "Parsing common attributes of "  + tag + " tag");
		for(int i = 0; i < parser.getAttributeCount(); i++) {
			switch(parser.getAttributeName(i)) {		
				case "to":
					stanza.to = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "TO = " + stanza.to);
					break;
				case "from":
					stanza.from = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "FROM = " + stanza.from);
					break;
				case "id":
					stanza.id = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "ID = " + stanza.id);
					break;
				case "type":
					stanza.type = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "TYPE = " + stanza.type);
					break;
				case "xml:lang":
					stanza.lang = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "LANG = " + stanza.lang);
					break;
				/*case "xmlns":
					stanza.xmlns = parser.getAttributeValue(i);
					Log.d(Constants.LOG_TAG, "XMLNS = " + stanza.xmlns);
					break;*/
			}
		}
	}
	
	/**
	 * @brief Parse initial stream e.g. stream:stream, stream:features
	 */
	public void parseStream(boolean parseText) {
		if(parseText) {
			if(tag.equals("mechanism")) {
				Log.d(Constants.LOG_TAG, "MECHANISM " + parser.getText());
				stanza.mechanism.add(parser.getText());
			}
		} else {
			// do nothing
		}
	}
	
	/**
	 * @brief Parse iq attributes and tags
	 * @param parseText - true parse body, false parse attributes
	 */
	public void parseIq(boolean parseText) {
		boolean toContinue = false;
		
		if(parseText) {
			if(tag.equals("group")) {
				Log.d(Constants.LOG_TAG, "GROUP " + parser.getText());
				stanza.items.lastElement().group = parser.getText();
			}
		} else {
			if(tag.equals("item")) {
				toContinue = true;
			}
		
			if(toContinue) {
				Contact contact = new Contact();
				Log.d(Constants.LOG_TAG, "New roster item");
				for(int i = 0; i < parser.getAttributeCount(); i++) {
					switch(parser.getAttributeName(i)) {		
						case "jid":
							contact.jid = parser.getAttributeValue(i);
							Log.d(Constants.LOG_TAG, "JID = " + contact.jid);
							break;
						case "name":
							contact.name = parser.getAttributeValue(i);
							Log.d(Constants.LOG_TAG, "NAME = " + contact.name);
							break;
						case "subscription":
							contact.subscription = parser.getAttributeValue(i);
							Log.d(Constants.LOG_TAG, "SUPSCRIPTION = " + contact.subscription);
							break;
					}
				}
				stanza.items.add(contact);
			}
		}
	}

	public void parse(boolean parseText) {	
		if(stanza != null) {
			switch(rootTag) {
				/**< STREAM:STREAM */
				case Constants.STREAM_INIT:
					parseStream(parseText);
					break;
				/**< IQ */
				case Constants.IQ:
					parseIq(parseText);
					break;
				/**< PRESENCE */
				case Constants.PRESENCE:
					// ...
					break;
				/**< MESSAGE */
				case Constants.MESSAGE:
					// ...
					break;
			}
		}
		
		switch(tag) {
			/**
			 * STREAM
			 */
			 
			/**< STREAM:STREAM */
			case Constants.TAG_STREAM_INIT:
				Log.d(Constants.LOG_TAG, "TAG <STREAM:STREAM>");
				rootTag = Constants.STREAM_INIT;
				parseCommon();
				break;
			/**< STREAM:FEATURES */
			/*case Constants.TAG_STREAM_FEATURES:
				Log.d(Constants.LOG_TAG, "TAG <STREAM:FEATURES>");
				rootTag = Constants.STREAM_FEATURES;
				break;*/
				
			/**
			 * STANZA
			 */
				
			/**< IQ */
			case Constants.TAG_IQ:
				Log.d(Constants.LOG_TAG, "TAG <IQ>");
				rootTag = Constants.IQ;
				parseCommon();
				break;
			/**< PRESENCE */
			case Constants.TAG_PRESENCE:
				Log.d(Constants.LOG_TAG, "TAG <PRESENCE>");
				rootTag = Constants.PRESENCE;
				parseCommon();
				break;
			/**< MESSAGE */
			case Constants.TAG_MESSAGE:
				Log.d(Constants.LOG_TAG, "TAG <MESSAGE>");
				rootTag = Constants.MESSAGE;
				parseCommon();
				break;
			
			/**
			 * ERROR OR SUCCESS
		 	*/
		 	
		 	/**< SUCCESS */
			case Constants.TAG_STREAM_SUCCESS:
				Log.d(Constants.LOG_TAG, "TAG <SUCESS>");
				rootTag = Constants.STREAM_SUCCESS;
				stanza = new Stanza(Constants.STREAM_SUCCESS);
				break;
			/**< FAILURE */
			case Constants.TAG_STREAM_FAILURE:
				Log.d(Constants.LOG_TAG, "TAG <FAILURE>");
				rootTag = Constants.STREAM_FAILURE;
				stanza = new Stanza(Constants.STREAM_FAILURE);
				break;
				
		}
	}
}
