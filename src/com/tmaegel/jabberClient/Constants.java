package com.tmaegel.jabberClient;

import android.util.Log;
import java.util.Collections;
import java.util.HashMap;

final public class Constants {

	public static final String LOG_TAG 				= "jabberClient";

	/**
	 * ROOT STANZA
	 */
	public static final String TAG_ROOT				= "root";

	/**
	 * NAMESPACES
	 */
	public final static int NS_STREAM				= 88;
	public static final int NS_TLS_AUTH				= 89;
	public static final int NS_SASL_AUTH			= 90;
	public static final int NS_COMPRESS				= 91;
	public static final int NS_RESOURCE_BIND		= 92;
	public static final int NS_SESSION 				= 93;
 	public static final int NS_ROSTER				= 94;



	/**
	 * COMMON STANZA
	 */
	public static final int NONE 					= 0;
	public static final String TAG_IQ				= "iq";
	public static final int IQ						= 1;

	public static final String TAG_PRESENCE			= "presence";
	public static final int PRESENCE				= 2;

	public static final String TAG_MESSAGE			= "message";
	public static final int MESSAGE					= 3;


	/**< Client side */
	public static final int C_ROSTER_REQUEST		= 21;			/**< type=get */
	public static final int C_ROSTER_SET			= 22;			/**< type=set */
	public static final int C_ROSTER_RESPONSE		= 23;			/**< type=result */
	/**< Server side */
	public static final int S_ROSTER_RESPONSE		= 25;			/**< type=result */
	public static final int S_ROSTER_PUSH			= 26;			/**< type=set */
	public static final int S_ROSTER_ERROR			= 28;			/**< type=error */

	/** Available tags */
	public static final String[] TAGS = new String[]
	{
		"iq",
		"message",
		"presence",
		"query",
		"mechanisms",
		"mechanism",
		"success",
		"item",
		"group",
		"jid"
	};

	/** Available attributes (meta) */
	public static final String[] META = new String[]
	{
		"id",
		"type",
		"to",
		"from",
		"ver",
		"ask"
	};

	/** Available attributes and tags (content) */
	public static final String[] CONTENT = new String[]
	{
		"jid",
		"name",
		"subscription",
		"mechanism",
		"group"
	};

	/** Available attributes and tags for STREAM */
	public static final String[] STREAM = new String[]
	{
		"...",
		"..."
	};

	/** Available attributes and tags for roster */
	public static final String[] ROSTER = new String[]
	{
		"jid",
		"name",
		"subscription",
		"group"
	};

	// private constructor to prevent instantiation/inheritance
	private Constants() {

	}
}
