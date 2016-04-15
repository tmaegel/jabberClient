package com.tmaegel.jabberClient;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXAdapter extends DefaultHandler {

	private XMLParser parent;
	
	private Stream stream;
	
	private String streamTmp;

	public SAXAdapter(XMLParser parent) {
		this.parent = parent;
	}
	
	public Stream getStream() {
		return stream;
	}


	@Override
	public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
		/**  reset */
		streamTmp = "";
	
		switch(tag.toLowerCase()) {
			case Constants.TAG_MESSAGE:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_MESSAGE + " tag");
				stream = new Stream(Constants.MESSAGE);
				stream.setTo(attributes.getValue("to"));
				stream.setFrom(attributes.getValue("from"));
				break;
			case Constants.TAG_CHALLENGE:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_CHALLENGE + " tag");
				stream = new Stream(Constants.CHALLENGE);
				break;
			case Constants.TAG_IQ:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_IQ + " tag");
				stream = new Stream(Constants.IQ);
				stream.setTo(attributes.getValue("to"));
				break;
			case Constants.TAG_SUCCESS:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_SUCCESS + " tag");
				stream = new Stream(Constants.SUCCESS);
				break;
			case Constants.TAG_ITEM:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_ITEM + " tag");
				stream = new Stream(Constants.ITEM);
				break;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {	
	
		streamTmp = new String(ch, start, length);
		Log.d(Constants.LOG_TAG, ">>> TMP:  " + streamTmp);
		
		/*switch(stream.getObjectType()) {
			case Constants.MESSAGE:
				stream.setBody(new String(ch, start, length));
				break;
			case Constants.CHALLENGE:
				stream.setBody(new String(ch, start, length));
				break;
			case Constants.IQ:
				String str = new String(ch, start, length);
				Log.d(Constants.LOG_TAG, "Str. " + str);
				stream.setBody(str);
				
				break;
			case Constants.SUCCESS:
				stream.setBody(new String(ch, start, length));
				break;
		}*/
	}

	@Override
	public void endElement(String uri, String localName, String tag) throws SAXException {
		switch(tag.toLowerCase()) {
			case Constants.TAG_MESSAGE:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_MESSAGE + " end");
				// Log.d(Constants.LOG_TAG, "Temp Stream:  " + streamTmp);
				break;
			case Constants.TAG_CHALLENGE:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_CHALLENGE + " end");
				// Log.d(Constants.LOG_TAG, "Temp Stream:  " + streamTmp);
				break;
			case Constants.TAG_IQ:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_IQ + " end");
				// Log.d(Constants.LOG_TAG, "Temp Stream:  " + streamTmp);
				break;
			case Constants.TAG_SUCCESS:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_SUCCESS + " end");
				// Log.d(Constants.LOG_TAG, "Temp Stream:  " + streamTmp);
				break;
			case Constants.TAG_ITEM:
				Log.d(Constants.LOG_TAG, "Detect " + Constants.TAG_ITEM + " end");
				Log.d(Constants.LOG_TAG, "Temp Stream:  " + streamTmp);
				break;
		}
	}
}
