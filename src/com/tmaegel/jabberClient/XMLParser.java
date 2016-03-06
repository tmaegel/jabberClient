package com.tmaegel.jabberClient;

import android.util.Log;

import java.io.StringReader;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLParser {

	private Network net;

	private SAXParserFactory factory;
	private SAXParser saxParser;
	private SAXAdapter saxAdapter;

	/**
	 * Evtl Object
	 */
	public String result;

	public Message msg;

	public XMLParser(Network net) {
		this.net = net;

		try {
			factory = SAXParserFactory.newInstance();
			saxParser = factory.newSAXParser();
			saxAdapter = new SAXAdapter(this);
		} catch (Exception e) {
			Log.e("jabberClient", "Error: XMLParse", e);
		}
	}

	public String parseStream(String stream) {
		Log.d("jabberClient", "Start parsing");
		Log.d("jabberClient", "" +   stream);
		try {
			saxParser.parse(new InputSource(new StringReader(stream)), saxAdapter);
		} catch (Exception e) {
			Log.e("jabberClient", "Error: XMLParse", e);
		}

		return result;
	}
	
	public Message parseMessage(String stream) {
		Log.d("jabberClient", "Start parsing");
		Log.d("jabberClient", "" +   stream);
		try {
			saxParser.parse(new InputSource(new StringReader(stream)), saxAdapter);
		} catch (Exception e) {
			Log.e("jabberClient", "Error: XMLParse", e);
		}

		return msg;
	}
}
