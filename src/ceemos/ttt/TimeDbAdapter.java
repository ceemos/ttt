/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ceemos.ttt;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @author marcel
 */
public class TimeDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public TimeDbAdapter(Context context) {
        this.context = context;
    }

    public TimeDbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
}
