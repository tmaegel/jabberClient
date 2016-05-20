package com.tmaegel.jabberClient;

import android.util.Log;
import java.util.Collections;
import java.util.HashMap;

final public class Constants {

	/**
	 * OTHER
	 */
	public static final String LOG_TAG 				= "jabberClient";

	/**
	 * NAMESPACES
	 */
	/*public final static int NS_STREAM				= 88;
	public static final int NS_TLS_AUTH				= 89;
	public static final int NS_SASL_AUTH			= 90;
	public static final int NS_COMPRESS				= 91;
	public static final int NS_RESOURCE_BIND		= 92;
	public static final int NS_SESSION 				= 93;
 	public static final int NS_ROSTER				= 94;*/

	public static final int NONE 					= 0;

	/**
	 * INITAL STREAM
	 */
	public static final int STREAM_INIT				= 1;
	public static final String TAG_STREAM_INIT		= "stream:stream";
	public static final int STREAM_FEATURES			= 2;
	public static final String TAG_STREAM_FEATURES 	= "stream:features";
	public static final int STREAM_SUCCESS			= 10;
	public static final String TAG_STREAM_SUCCESS	= "success";
	public static final int STREAM_FAILURE			= 11;
	public static final String TAG_STREAM_FAILURE	= "failure";

	/**
	 * COMMON STANZA
	 */
	public static final String TAG_IQ				= "iq";
	public static final int IQ						= 3;
	public static final String TAG_PRESENCE			= "presence";
	public static final int PRESENCE				= 4;
	public static final String TAG_MESSAGE			= "message";
	public static final int MESSAGE					= 5;


	/**< Client side */
	public static final int C_ROSTER_REQUEST		= 21;			/**< type=get */
	public static final int C_ROSTER_SET			= 22;			/**< type=set */
	public static final int C_ROSTER_RESPONSE		= 23;			/**< type=result */
	/**< Server side */
	public static final int S_ROSTER_RESPONSE		= 25;			/**< type=result */
	public static final int S_ROSTER_PUSH			= 26;			/**< type=set */
	public static final int S_ROSTER_ERROR			= 28;			/**< type=error */

	/** Client side */
	public static final int C_SEND_MESSAGE			= 30;
	/**< Server side */
	public static final int RECV_MESSAGE			= 31;

}
