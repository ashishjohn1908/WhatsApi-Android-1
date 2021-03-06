package nl.giovanniterlingen.whatsapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android adaptation from the PHP WhatsAPI by WHAnonymous {@link https
 * ://github.com/WHAnonymous/Chat-API/}
 * 
 * @author Giovanni Terlingen
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "msgstore.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE messages (_id INTEGER PRIMARY KEY AUTOINCREMENT, `from` TEXT, `to` TEXT, message TEXT, id TEXT, t TEXT);");
	}

	public static Cursor getContacts(SQLiteDatabase db) {
		// Select All Query
		String selectQuery = "SELECT *, MAX(t) FROM messages GROUP BY MIN(`from`, `to`), MAX(`from`, `to`) ORDER BY t DESC;";
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}
	
	public static Cursor getMessages(SQLiteDatabase db, String number) {
		// Select All Query
		String selectQuery = "SELECT _id, `from`,`to`, id, message, t FROM messages WHERE `from` = " + DatabaseUtils.sqlEscapeString(number) + " OR `to` = " + DatabaseUtils.sqlEscapeString(number);
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}