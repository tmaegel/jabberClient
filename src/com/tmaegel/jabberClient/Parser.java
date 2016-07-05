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
		if(Constants.DEBUG)
			Log.d("jabberClient", "PARSE " + stream);

		try {
			parser.setInput(new StringReader(stream));
			int eventType = parser.getEventType();

			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
					case XmlPullParser.START_DOCUMENT:
						if(Constants.DEBUG)
							Log.d("jabberClient", ">>> New stream");
						stanza = new Stanza();
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
						if(Constants.DEBUG)
							Log.d("jabberClient", "< Tag " + parser.getName() + " end");
						break;
					case XmlPullParser.END_DOCUMENT:
						if(Constants.DEBUG)
							Log.d("jabberClient", "<<< Stream end");
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
		if(Constants.DEBUG)
			Log.d(Constants.LOG_TAG, "Parsing common attributes of "  + tag + " tag");
			
		for(int i = 0; i < parser.getAttributeCount(); i++) {
			switch(parser.getAttributeName(i)) {
				case "id":
					stanza.id = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "ID = " + stanza.id);
					break;
				case "to":
					stanza.to = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "TO = " + stanza.to);
					break;
				case "from":
					stanza.from = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "FROM = " + stanza.from);
					break;
				case "type":
					stanza.type = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "TYPE = " + stanza.type);
					break;
				case "xml:lang":
					stanza.lang = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "LANG = " + stanza.lang);
					break;
				case "xmlns":
					stanza.xmlns = parser.getAttributeValue(i);
					if(Constants.DEBUG)
						Log.d(Constants.LOG_TAG, "XMLNS = " + stanza.xmlns);
					break;
			}
		}
	}

	/**
	 * @brief Parse initial stream e.g. stream:stream, stream:features
	 */
	public void parseStream(boolean parseText) {
		if(parseText) {
			if(tag.equals("mechanism")) {
				if(Constants.DEBUG)
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
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "GROUP " + parser.getText());
				stanza.items.get(stanza.items.size()-1).group = parser.getText();
			} else if(tag.equals("jid")) {
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "JID " + parser.getText());
				stanza.jid = parser.getText();
			}
		} else {
			if(tag.equals("item")) {
				toContinue = true;
			}

			if(toContinue) {
				Contact contact = new Contact();
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "New roster item");
				for(int i = 0; i < parser.getAttributeCount(); i++) {
					switch(parser.getAttributeName(i)) {
						case "jid":
							contact.jid = parser.getAttributeValue(i);
							if(Constants.DEBUG)
								Log.d(Constants.LOG_TAG, "JID = " + contact.jid);
							break;
						case "name":
							contact.name = parser.getAttributeValue(i);
							if(Constants.DEBUG)
								Log.d(Constants.LOG_TAG, "NAME = " + contact.name);
							break;
						case "subscription":
							contact.subscription = parser.getAttributeValue(i);
							if(Constants.DEBUG)
								Log.d(Constants.LOG_TAG, "SUPSCRIPTION = " + contact.subscription);
							break;
					}
				}
				stanza.items.add(contact);
			}
		}
	}

	/**
	 * @brief Parse message attributes and tags
	 * @param parseText - true parse body, false parse attributes
	 */
	public void parseMessage(boolean parseText) {
		boolean toContinue = false;

		if(parseText) {
			if(tag.equals("subject")) {
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "SUBJECT " + parser.getText());
				stanza.message.setSubject(parser.getText());
			} else if(tag.equals("body")) {
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "BODY " + parser.getText());
				stanza.message.setBody(parser.getText());
			} else if(tag.equals("thread")) {
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "THREAD " + parser.getText());
				stanza.message.setThread(parser.getText());
			}
		} else {
			for(int i = 0; i < parser.getAttributeCount(); i++) {
				switch(parser.getAttributeName(i)) {
					case "id":
						stanza.message.setId(parser.getAttributeValue(i));
						if(Constants.DEBUG)
							Log.d(Constants.LOG_TAG, "ID = " + stanza.message.getId());
						break;
					case "from":
						stanza.message.setFrom(parser.getAttributeValue(i));
						if(Constants.DEBUG)
							Log.d(Constants.LOG_TAG, "FROM = " + stanza.message.getFrom());
						break;
					case "to":
						stanza.message.setTo(parser.getAttributeValue(i));
						if(Constants.DEBUG)
							Log.d(Constants.LOG_TAG, "TO = " + stanza.message.getTo());
						break;
					case "type":
						stanza.message.setType(parser.getAttributeValue(i));
						if(Constants.DEBUG)
							Log.d(Constants.LOG_TAG, "TYPE = " + stanza.message.getType());
						break;
					case "xml:lang":
						stanza.message.setLang(parser.getAttributeValue(i));
						if(Constants.DEBUG)
							Log.d(Constants.LOG_TAG, "LANG = " + stanza.message.getLang());
						break;
				}
			}
		}
	}

	public void parse(boolean parseText) {
		if(stanza != null) {
			switch(stanza.stanzaType) {
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
					parseMessage(parseText);
					break;
			}
		}

		switch(tag) {
			/**
			 * STREAM
			 */

			/**< STREAM:STREAM */
			case Constants.TAG_STREAM_INIT:
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <STREAM:STREAM>");
				stanza.stanzaType = Constants.STREAM_INIT;
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
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <IQ>");
				stanza.stanzaType = Constants.IQ;
				parseCommon();
				break;
			/**< PRESENCE */
			case Constants.TAG_PRESENCE:
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <PRESENCE>");
				stanza.stanzaType = Constants.PRESENCE;
				parseCommon();
				break;
			/**< MESSAGE */
			case Constants.TAG_MESSAGE:
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <MESSAGE>");
				stanza.stanzaType = Constants.MESSAGE;
				// parseCommon();
				parseMessage(false);
				break;

			/**
			 * ERROR OR SUCCESS
		 	*/

		 	/**< SUCCESS */
			case Constants.TAG_STREAM_SUCCESS:
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <SUCESS>");
				stanza.stanzaType = Constants.STREAM_SUCCESS;
				break;
			/**< FAILURE */
			case Constants.TAG_STREAM_FAILURE:
				if(Constants.DEBUG)
					Log.d(Constants.LOG_TAG, "TAG <FAILURE>");
				stanza.stanzaType = Constants.STREAM_FAILURE;
				break;

		}
	}
}
