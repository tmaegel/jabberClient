package com.tmaegel.jabberClient;

import android.util.Log;
import java.util.Collections;
import java.util.HashMap;

final public class Constants {
	 
	public static final String LOG_TAG 				= "jabberClient";
	 
	/** Allowed tags, attributes */	
	public static final HashMap<String,String[]> TAGS = new HashMap<String, String[]>() {{
		put("iq", new String[] { "to", "type", "id" });
		put("query", new String[] { "xmlns" });
		put("item", new String[] { "jid", "name", "subscription" });
		put("group", new String[] { "" });
	}};
	 
	// stream
	public static final int 	NONE 				= 0;
	public static final int 	STREAM_MAIN 		= 1;
	public static final String TAG_STREAM_MAIN 	= "stream:stream";
	public static final int 	STREAM_FEATURES 	= 2;
	public static final String TAG_STREAM_FEATURES = "stream:features";
	public static final int 	STREAM_ERROR 		= 3;
	public static final String TAG_STREAM_ERROR 	= "stream:error";
	/** ... to 9 */

	// auth
	public static final int 	STARTTLS 			= 10;
	public static final int 	MECHANISM 			= 11;
	public static final int 	REQUIRED 			= 12;
	public static final int 	AUTH 				= 13;
	public static final int 	CHALLENGE 			= 14;
	public static final String TAG_CHALLENGE		= "challenge";
	public static final int 	RESPONSE 			= 15;

	// general
	public static final int 	IQ					= 16;
	public static final String TAG_IQ				= "iq";

	// resource bind
	public static final int 	BIND				= 17;
	public static final int 	RESOURCE			= 18;

	// message
	public static final int 	MESSAGE				= 20;
	public static final String TAG_MESSAGE			= "message";
	public static final int 	BODY				= 21;
	public static final String TAG_BODY			= "body";
	public static final int 	SUBJECT 			= 22;
	public static final String TAG_SUBJECT			= "subject";
	public static final int 	THREAD 				= 23;
	public static final String TAG_THREAD			= "thread";

	// presence
	public static final int 	PRESENCE			= 30;
	public static final int 	SHOW				= 31;
	public static final int 	STATUS				= 32;
	public static final int 	PRIORITY			= 33;

	// roster
	public static final int 	QUERY				= 41;
	public static final String TAG_QUERY			= "query";
	public static final int 	ITEM				= 42;
	public static final String TAG_ITEM			= "item";
	public static final int 	GROUP				= 43;

	public static final int 	ROSTER_REQUEST		= 44;
	public static final int 	ROSTER_ADD			= 45;
	public static final int 	ROSTER_UPDATE		= 46;
	public static final int 	ROSTER_DELETE		= 47;

	// status
	public static final int 	SUCCESS				= 90;
	public static final String	TAG_SUCCESS			= "success";
	public static final int 	PROCEED				= 91;
	public static final String TAG_PROCEED			= "proceed";
	public static final int 	FAILURE				= 92;
	public static final String	TAG_FAILURE			= "failure";
	public static final int 	ERROR				= 93;
	public static final String	TAG_ERROR			= "error";

	// private constructor to prevent instantiation/inheritance
	private Constants() {
	
	}
}

