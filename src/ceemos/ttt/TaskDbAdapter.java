package ceemos.ttt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static ceemos.ttt.DatabaseHelper.*;

public class TaskDbAdapter {


    private static final String DATABASE_TABLE = "task";
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public TaskDbAdapter(Context context) {
        this.context = context;
    }

    public TaskDbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /** * Create a new todo If the todo is successfully created return the new * rowId for that note, otherwise return a -1 to indicate failure. */
    public long createTask(String label, String color) {
        ContentValues initialValues = createContentValues(label, color);

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    /** * Deletes todo */
    public boolean deleteTask(long rowId) {
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /** * Return a Cursor over the list of all todo in the database * * @return Cursor over all notes */
    public Cursor fetchAllTasks() {
        return database.query(DATABASE_TABLE, new String[]{KEY_ROWID,
                    KEY_LABEL, KEY_COLOR}, null, null, null,
                null, KEY_PRIORITY + " desc");
    }

    public boolean incPriority(long rowId) {
        Cursor prio = database.query(DATABASE_TABLE, new String[]{KEY_PRIORITY}, KEY_ROWID + "=" + rowId, null, null, null, null);
        prio.moveToFirst();
        int p = prio.getInt(prio.getColumnIndex(KEY_PRIORITY));
        prio.close();
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_PRIORITY, p + 1);
        return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
                + rowId, null) > 0;
    }

    private ContentValues createContentValues(String label, String color) {
        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, label);
        values.put(KEY_COLOR, color);
        values.put(KEY_PRIORITY, 1);
        return values;
    }
}
