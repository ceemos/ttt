/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ceemos.ttt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import static ceemos.ttt.DatabaseHelper.*;

/**
 *
 * @author marcel
 */
public class TimeDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    
    private final String DATABASE_TABLE = "timelist";

    public TimeDbAdapter(Context context) {
        this.context = context;
    }

    public TimeDbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    
    public void setTimeBase(long ts_start) {
        database.execSQL("create temp view timelist as "
                + "select task._id, task.label, task.color, sum(time.t) as sumt "
                + "from time "
                + "inner join task on task._id = time.tid "
                + "where time.ts > " + ts_start + " "
                + "group by task._id "
                + "order by sum(time.t) desc;");
    }
    
    public Cursor fetchAllTimes() {
        return database.query(DATABASE_TABLE, new String[]{KEY_ROWID,
                    KEY_LABEL, KEY_COLOR, KEY_TIME_SUM}, null, null, null,
                null, null);
    }

    public void close() {
        dbHelper.close();
    }
    
    public long createTime(long timestamp, int time, int task_id) {
        ContentValues initialValues = createContentValues(time, timestamp, task_id);

        return database.insert("time", null, initialValues);
    }

    private ContentValues createContentValues(int t, long ts, int task_id) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TIME, t);
        cv.put(KEY_TASKID, task_id);
        cv.put(KEY_TIMESTAMP, ts);
        return cv;
    }
}
