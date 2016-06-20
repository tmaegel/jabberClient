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

	// database version
	private static final int DB_VERSION = 16;

	private boolean resetOnStart = true;
	
	public SQLController(Context context, boolean resetOnStart) {
		super(context, DB_NAME, null, DB_VERSION);
		this.resetOnStart = resetOnStart;

		/*try {
			database = getWritableDatabase();
		} catch(SQLException e) {
			Log.e("jabberClient", "" + e);
		}*/
		//  open();
		
		if(resetOnStart) {
			reset();
			db = this.getWritableDatabase();
			onCreate(db);
		}
	}

	/**
	 * @brief will be called on first time use of the application
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE table_roster (ID INTEGER PRIMARY KEY AUTOINCREMENT, JID TEXT, NAME TEXT, CIRCLE TEXT);");
			Log.d("jabberClient", "create table_roster");
			
			db.execSQL("CREATE TABLE table_message (ID INTEGER PRIMARY KEY AUTOINCREMENT, SENDER TEXT, RECEIVER TEXT, SUBJECT TEXT, BODY TEXT, THREAD TEXT, LOCAL INTEGER);");
			Log.d("jabberClient", "create table_message");
		} catch(SQLException e) {
			Log.e("jabberClient", "create table: " + e);
			System.exit(1);
		}
	}

	/**
	 * @brief is called only when the database version is changed
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS table_roster");
		Log.d("jabberClient", "drop table_roster");
		db.execSQL("DROP TABLE IF EXISTS table_message");
		Log.d("jabberClient", "drop table_message");
		onCreate(db);
	}

	/**
	 * CONTACT
	 */

	public void insertContact(Contact contact) {
		Log.d("jabberClient", "Insert contact");
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
			Log.e("jabberClient", "Error: execSQL:INSERT" + e.toString());
		}
	}
	
	public void updateContact(Contact contact) {
		Log.d("jabberClient", "Update contact");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "UPDATE ...";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:UPDATE" + e.toString());
		}
	}
	
	public void removeContact(Contact contact) {
		Log.d("jabberClient", "Remove contact");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "DROP ...";
				
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:DROP" + e.toString());
		}
	}
	
	public List<Contact> fetchContacts() {
		Log.d("jabberClient", "Fetch contacts");
		List<Contact> contacts = new ArrayList<Contact>();
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT JID, NAME, CIRCLE FROM table_roster", null);
			if(c.moveToFirst()){
				do {
					contacts.add(new Contact(c.getString(0), c.getString(1), c.getString(2)));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT" + e.toString());
		}
		
		return contacts;
	}

	/**
	 *  MESSAGE
	 */

	public void insertMessage(Message message) {
		Log.d("jabberClient", "Insert message");
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
			Log.e("jabberClient", "Error: execSQL:INSERT" + e.toString());
		}
	}
	
	public List<Message> fetchMessages() {
		Log.d("jabberClient", "Fetch messages");
		List<Message> messages = new ArrayList<Message>();
		try {
			
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT SENDER, RECEIVER, SUBJECT, BODY, THREAD, LOCAL FROM table_message", null);
			if(c.moveToFirst()){
				do {
					messages.add(new Message(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), (c.getInt(5) != 0)));
				} while(c.moveToNext());
			}
			c.close();
			db.close();
			
		} catch (Exception e) {
			Log.e("jabberClient", "Error: rawQuery:SELECT" + e.toString());
		}
		
		return messages;
	}

	/**
	 * DATABASE
	 */
	public void reset() {
		Log.d("jabberClient", "Reset database");
		try {
			db = this.getWritableDatabase();
			String sqlExec = "";
			sqlExec = "DROP TABLE IF EXISTS table_roster";	
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			sqlExec = "DROP TABLE IF EXISTS table_message";	
			Log.d("jabberClient", "execSQL: " + sqlExec);
			db.execSQL(sqlExec);
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "Error: execSQL:DROP" + e.toString());
		}
	}

	/**
	 * @brief delete record
	 */
	/*public void delete(long id) {
		db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID + "=" + id, null);
		db.close();
	}*/

	/**
	 * @brief delete all records
	 */
	/*public void deleteAll() {
		/*db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID + "=" + id, null);
		db.close();*/
	//}
}
