package com.tmaegel.jabberClient;

import android.util.Log;

import android.content.ContentValues;
import android.database.Cursor;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLController extends SQLiteOpenHelper {

	public SQLiteDatabase db;

	// Database name
	private static final String DB_NAME = "db_xmmp";

	// database version
	private static final int DB_VERSION = 10;

	// Table name
	private static final String TABLE_NAME = "roster";

	// Table columns
	//  @todo: as array
	public static final String ID = "id";
	public static final String JID = "jid";
	public static final String NAME = "name";
	public static final String GROUP = "circle";

	public SQLController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

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
		try {
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, " + JID + " TEXT, " + NAME + " TEXT, " + GROUP + " TEXT);");
			Log.d("jabberClient", "create " + TABLE_NAME);
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		Log.d("jabberClient", "drop " + TABLE_NAME);
	}

	/**
	 * @brief add new record
	 */
	public void insert(String jid, String name, String group) {
		Log.d("jabberClient", "inserting ...");
		try {
			db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			/** @todo id != jid */
			values.put(JID, jid);
			values.put(NAME, name);
			values.put(GROUP, group);
			db.insert(TABLE_NAME, null, values);

			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "" + e);
		}
	}

	/**
	 * @brie fetching all records
	 */
	public List<Contact> fetch() {
		Log.d("jabberClient", "fetching ...");
		List<Contact> contactList = new ArrayList();
		try {
			db = this.getReadableDatabase();
			String query = "SELECT * FROM " + TABLE_NAME;
			Cursor cursor = db.rawQuery(query, null);

			// looping through all rows and adding to list
			if(cursor.moveToFirst()) {
				do {
					Contact contact = new Contact();
					contact.jid = cursor.getString(0);
					contact.name = cursor.getString(1);
					contact.group = cursor.getString(2);
					// contact.setJID(cursor.getString(0));
					// contact.setName(cursor.getString(1));
					// contact.setGroup(cursor.getString(2));
					// Adding contact to list
					contactList.add(contact);

					// Log.d("jabberClient", "JID: " + contact.getJID() + " NAME: " + contact.getName() + " GROUP: " + contact.getGroup());
				} while (cursor.moveToNext());
			}

			// return contact list
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("jabberClient", "" + e);
		}

		return contactList;
	}

	/**
	 * @brief modify record by id
	 */
	public int updateById(long id, String jid, String name, String group) {
		Log.d("jabberClient", "modifying by id ...");
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(JID, jid);
		values.put(NAME, name);
		values.put(GROUP, group);
		int i = db.update(TABLE_NAME, values, ID + " = " + id, null);
		db.close();

		return i;
	}

	/**
	 * @brief modify record by object
	 */
	/*public int updateByObject(long id, String jid, String name, String group) {
		Log.d("jabberClient", "modifying by object ...");
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());
		values.put(KEY_EMAIL, contact.getEmail());

		// updating row
		db.close();
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	}*/

	/**
	 * @brief delete record
	 */
	public void delete(long id) {
		db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID + "=" + id, null);
		db.close();
	}

	/**
	 * @brief delete all records
	 */
	public void deleteAll() {
		/*db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID + "=" + id, null);
		db.close();*/
	}
}
