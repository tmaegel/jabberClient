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
	
	public Stream parseResponse(String stream) {
		// Wegschneiden
		// <?xml version='1.0'?><stream:stream xmlns:stream='http://etherx.jabber.org/streams' version='1.0' from='localhost' id='774b11bc-c31d-4714-880a-bf736bbe134f' xml:lang='en' xmlns='jabber:client'>
		
		if(stream.indexOf("<?xml") != -1) {
			stream = stream.substring(stream.indexOf(">") + 1);
		}
		if(stream.indexOf("<stream:stream") != -1) {
			stream = stream.substring(stream.indexOf(">") + 1);
		}
		int i;
		if(stream.indexOf("<stream:features") != -1) {
			stream = stream.substring(stream.indexOf(">") + 1);
		}
		if((i = stream.indexOf("</stream:features")) != -1) {
			stream = stream.substring(0, i);
		}
		if(stream.indexOf("<ver") != -1) {
			stream = stream.substring(stream.indexOf(">") + 1);
		}
		
		/**< temp root element */
		stream = "<root>" +  stream + "</root>";
		
		Log.d("jabberClient", "Start parsing");
		Log.d("jabberClient", "Stream: " + stream);
		
		
		try {
			saxParser.parse(new InputSource(new StringReader(stream)), saxAdapter);
		} catch (Exception e) {
			Log.e("jabberClient", "Error: XMLParse", e);
		}

		return saxAdapter.getStream();
	}
}
