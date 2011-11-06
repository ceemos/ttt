package ceemos.ttt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 6;
    // Database fields
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_COLOR = "color";
    public static final String KEY_PRIORITY = "prio";
    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table task (_id integer primary key autoincrement, "
            + "label text not null, color text not null, prio integer not null);"
            + "create table todo(_id integer primary key autoincrement, "
            + "notes text not null, "
            + "tid integer not null, foreign key(tid) references task(_id)); "
            + "create table time(_id integer primary key autoincrement, "
            + "t integer not null, ts integer not null, "
            + "tid integer not null, foreign key(tid) references task(_id));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        String sqlText = DATABASE_CREATE;
        for(String sqlStmt : sqlText.split(";"))
             database.execSQL(sqlStmt + ";");

    }

    // Method is called during an upgrade of the database, e.g. if you increase
    // the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS todo");
        database.execSQL("DROP TABLE IF EXISTS task");
        database.execSQL("DROP TABLE IF EXISTS time");
        
        onCreate(database);
        
    }
}
