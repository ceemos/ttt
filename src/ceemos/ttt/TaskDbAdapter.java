package ceemos.ttt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskDbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_LABEL = "label";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_COLOR = "color";
	private static final String DATABASE_TABLE = "task";
	private Context context;
	private SQLiteDatabase database;
	private TaskDatabaseHelper dbHelper;

	public TaskDbAdapter(Context context) {
		this.context = context;
	}

	public TaskDbAdapter open() throws SQLException {
		dbHelper = new TaskDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	
/** * Create a new todo If the todo is successfully created return the new * rowId for that note, otherwise return a -1 to indicate failure. */

	public long createTodo(String label, String color) {
		ContentValues initialValues = createContentValues(label, color);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	
/** * Update the todo */

	public boolean updateTodo(long rowId, String label, String color) {
		ContentValues updateValues = createContentValues(label, color);

		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	
/** * Deletes todo */

	public boolean deleteTodo(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	
/** * Return a Cursor over the list of all todo in the database * * @return Cursor over all notes */

	public Cursor fetchAllTodos() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_LABEL, KEY_COLOR }, null, null, null,
				null, null);
	}

	
/** * Return a Cursor positioned at the defined todo */

	public Cursor fetchTodo(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_LABEL },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String label, String color) {
		ContentValues values = new ContentValues();
		values.put(KEY_LABEL, label);
		values.put(KEY_COLOR, color);
		return values;
	}
}
