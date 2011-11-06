package ceemos.ttt;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @author marcel
 */
public class TodoDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public TodoDbAdapter(Context context) {
        this.context = context;
    }

    public TodoDbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
}
