package com.tmaegel.jabberClient;

import android.util.Log;

import android.database.Cursor;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLController extends SQLiteOpenHelper {

	public static SQLiteDatabase db;

	// Database name
	private static final String DB_NAME = "db_xmmp";

	// Database version
	private static final int DB_VERSION = 19;

	// Database reset
	private static final boolean DB_RESET = true;
	
	public SQLController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

		/* if(DB_RESET) {
			onCreate(this.getWritableDatabase());
		} */
		
		insertSession("user1", "123456", "my-resource", "localhost", "192.168.178.103", 5222);

		/*try {
			database = getWritableDatabase();
		} catch(SQLException e) {
			Log.e("jabberClient", "" + e);
		}*/
		//  open();
	}

	/**
	 * @brief will be called on first time use of the application
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {	
		if(DB_RESET) {
			reset();
		}
	
		try {
			Log.d("jabberClient", ">Intitialize database");
		
			db.execSQL("CREATE TABLE table_session (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER TEXT, PASSWORD TEXT, RESOURCE TEXT, DOMAIN TEXT, IP TEXT, PORT INTEGER);");
			Log.d("jabberClient", "> Create table_session");
		
			db.execSQL("CREATE TABLE table_roster (ID INTEGER PRIMARY KEY AUTOINCREMENT, JID TEXT, NAME TEXT, CIRCLE TEXT);");
			Log.d("jabberClient", "> Create table_roster");
			
			db.execSQL("CREATE TABLE table_message (ID INTEGER PRIMARY KEY AUTOINCREMENT, SENDER TEXT, RECEIVER TEXT, SUBJECT TEXT, BODY TEXT, THREAD TEXT, LOCAL INTEGER);");
			Log.d("jabberClient", "> Create table_message");
		} catch(SQLException e) {
			Log.e("jabberClient", "Error: execSQL:CREATE " + e);
			System.exit(1);
		}
	}

	/**
	 * @brief is called only when the database version is changed
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("jabberClient", "> Reset database for upgrade");
	
		db.execSQL("DROP TABLE IF EXISTS table_session");
		Log.d("jabberClient", "drop table_user");
		db.execSQL("DROP TABLE IF EXISTS table_roster");
		Log.d("jabberClient", "drop table_roster");
		db.execSQL("DROP TABLE IF EXISTS table_message");
		Log.d("jabberClient", "drop table_message");
		onCreate(db);
	}
	
	/**
	 * SESSION
	 */
	 
	/**< @todo: password - salt or peper */
	public void insertSession(String user, String password, String resource, String domain, String ip, int port) {
		Log.d("jabberClient", "> Insert session in db");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "INSERT INTO table_session VALUES (" 
				+  null + ",'" 
				+ user + "','" 
				+ password + "','" 
				+ resource + "','" 
				+ domain + "','" 
				+ ip + "'," 
				+ port + ")";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:INSERT " + e.toString());
		}
	}
	
	public Session selectSession() {
		Log.d("jabberClient", "> Select session from db");
		Session session = null;
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT USER, PASSWORD, RESOURCE, DOMAIN, IP, PORT FROM table_session", null);
			if(c.moveToFirst()) {
				do {
					Log.d("jabberClient", "SELECT " + c.getString(0) + ", " + c.getString(1) + ", " + c.getString(2) + ", " + c.getString(3) + ", " + c.getString(4) + ", " + c.getInt(5));
					session = new Session(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT " + e.toString());
		}
		
		return session;
	}

	/**
	 * CONTACT
	 */
	public void insertContact(Contact contact) {
		Log.d("jabberClient", "> Insert contact in db");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "INSERT INTO table_roster VALUES (" 
				+  null + ",'" 
				+ contact.getJid() + "','" 
				+ (contact.getName() == null ? "" : contact.getName()) + "','" 
				+ (contact.getGroup() == null ? "" : contact.getGroup()) + "')";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:INSERT " + e.toString());
		}
	}
	
	public void updateContact(Contact contact) {
		Log.d("jabberClient", "> Update contact in db");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "UPDATE ...";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:UPDATE " + e.toString());
		}
	}
	
	public void removeContact(int id) {
		Log.d("jabberClient", "> Remove contact from db with id " + id);
		try {
			db = this.getWritableDatabase();
			String sqlExec = "DELETE FROM table_roster WHERE ID=" + id;
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:DELETE " + e.toString());
		}
	}
	
	public Contact selectContact(int id) {
		Log.d("jabberClient", "> Select contact by id " + id + " from db");
		Contact contact = null;
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT ID, JID, NAME, CIRCLE FROM table_roster WHERE ID=" + id, null);
			if(c.moveToFirst()) {
				do {
					Log.d("jabberClient", "SELECT " + c.getInt(0) + ", " + c.getString(1) + ", " + c.getString(2) + ", " + c.getString(3));
					contact = new Contact(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT " + e.toString());
		}
		
		return contact;
	}
	
	public List<Contact> fetchContacts() {
		Log.d("jabberClient", "> Fetch contacts from db");
		List<Contact> contacts = new ArrayList<Contact>();
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT ID, JID, NAME, CIRCLE FROM table_roster", null);
			if(c.moveToFirst()) {
				do {
					// ID, JID, NAME, GROUP
					contacts.add(new Contact(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT " + e.toString());
		}
		
		return contacts;
	}

	/**
	 *  MESSAGE
	 */
	public void insertMessage(Message message) {
		Log.d("jabberClient", "> Insert message in db");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "INSERT INTO table_message VALUES (" 
				+  null + ",'" 
				+ message.getFrom() + "','" 
				+ message.getTo() + "','" 
				+ message.getSubject() + "','" 
				+ message.getBody() +"','" 
				+ message.getThread() + "',"
				+ ((message.isLocal()) ? 1 : 0) + ")";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:INSERT " + e.toString());
		}
	}
	
	public List<Message> fetchMessages(String jid) {
		Log.d("jabberClient", "> Fetch messages from db");
		List<Message> messages = new ArrayList<Message>();
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT SENDER, RECEIVER, SUBJECT, BODY, THREAD, LOCAL FROM table_message WHERE SENDER LIKE '" + jid + "%' OR RECEIVER LIKE '" + jid + "%'", null);
			if(c.moveToFirst()){
				do {
					messages.add(new Message(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), (c.getInt(5) != 0)));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT " + e.toString());
		}
		
		return messages;
	}

	/**
	 * DATABASE
	 */
	public void reset() {
		Log.d("jabberClient", "> Reset database");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "";
			sqlExec = "DROP TABLE IF EXISTS table_session";	
			Log.d("jabberClient", "execSQL: " + sqlExec);
			sqlExec = "DROP TABLE IF EXISTS table_roster";	
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			sqlExec = "DROP TABLE IF EXISTS table_message";	
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:DROP " + e.toString());
		}
	}
}
