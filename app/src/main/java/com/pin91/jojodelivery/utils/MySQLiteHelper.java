package com.pin91.jojodelivery.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.pin91.jojodelivery.model.User;

/**
 * @author Debdutta Bhattacharya
 * 
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_USER = "user";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String USER_ID = "userId";
	public static final String DATABASE_NAME = "jojodelivery.db";
	private static final int DATABASE_VERSION = 1;
	private static SQLiteDatabase database;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_USER
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_USERNAME + " text not null," + COLUMN_PASSWORD
			+ " text not null );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(db);
	}

	public static void insertUser(User user, Context context) {
		MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USERNAME, user.getUserName());
		values.put(MySQLiteHelper.COLUMN_PASSWORD, user.getPassword());
		database.insert(MySQLiteHelper.TABLE_USER, null, values);
		database.close();
	}

	public static User fetchUser(Context context) {
		MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();

		User user = null;

		String selectQuery = "SELECT * FROM " + TABLE_USER
				+ " order by id desc limit 1";
		Cursor c = database.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			user = new User();

			user.setUserName(c.getString(c.getColumnIndex("username")));
			user.setPassword(c.getString(c.getColumnIndex("password")));

		}
		c.close();
		database.close();
		return user;

	}

	public static void deleteUser(Context context) {
		MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_USER + " ";
		database.execSQL(query);
		database.close();

	}

}