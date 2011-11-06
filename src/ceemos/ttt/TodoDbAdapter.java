package ceemos.ttt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static ceemos.ttt.DatabaseHelper.*;

/**
 *
 * @author marcel
 */
public class TodoDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    
    private static final String DATABASE_TABLE = "todolist";

    public TodoDbAdapter(Context context) {
        this.context = context;
    }

    public TodoDbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        
        database.execSQL("create temp view if not exists todolist as "
                + "select todo._id, task.label, task.color, todo.notes "
                + "from todo "
                + "inner join task on task._id = todo.tid");
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    
    public Cursor fetchAllTodos() {
        return database.query(DATABASE_TABLE, new String[]{KEY_ROWID,
                    KEY_LABEL, KEY_COLOR, KEY_NOTES}, null, null, null,
                null, null);
    }

    public boolean deleteTodo(long rowId) {
        return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public long createTodo(String notes, int task_id) {
        ContentValues initialValues = createContentValues(notes, task_id);

        return database.insert("todo", null, initialValues);
    }

    private ContentValues createContentValues(String notes, int task_id) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTES, notes);
        cv.put("tid", task_id);
        return cv;
    }
    
}
