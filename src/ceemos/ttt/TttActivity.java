package ceemos.ttt;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import static ceemos.ttt.DatabaseHelper.*;

public class TttActivity extends TabActivity {

    TabHost mTabHost;
    ListView tasklist;
    ListView timelist;
    ListView todolist;
    EditText editNewTask;
    
    private TaskDbAdapter taskHelper;
    private TodoDbAdapter todoHelper;
    
    
    static String[] COUNTRIES = new String[]{
        "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
        "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina"};

    private void fillData() {
        Cursor cursor;
        cursor = taskHelper.fetchAllTasks();
        startManagingCursor(cursor);

        String[] from = new String[]{KEY_LABEL, KEY_COLOR, KEY_ROWID};
        int[] to = new int[]{R.id.tasktext, R.id.teskentrylayout, R.id.taskbuttons};

        SimpleCursorAdapter tasks = 
                new SimpleCursorAdapter(this, R.layout.taskentry, cursor, from, to);
        tasks.setViewBinder(new TaskViewBinder());
        tasklist.setAdapter(tasks);
        
        cursor = todoHelper.fetchAllTodos();
        from = new String[]{KEY_LABEL, KEY_NOTES, KEY_ROWID};
        to = new int[]{R.id.todolabel, R.id.todoextra, R.id.tododone};
        SimpleCursorAdapter todos = 
                new SimpleCursorAdapter(this, R.layout.todoentry, cursor, from, to);
        todos.setViewBinder(new IdBinder());
        todolist.setAdapter(todos);
        
    }

    class TaskViewBinder implements SimpleCursorAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            //System.out.println("setViewValue: " + view.getClass().getName() + " id:" + view.getId() + " column:" + columnIndex);
            if(columnIndex == 1) {
                TextView t = (TextView) view;
                t.setText(cursor.getString(columnIndex));
            } else if (columnIndex == 2) {
                String color = cursor.getString(columnIndex);
                int c = (Integer.parseInt(color) | 0xFF000000) & 0xFF7F7F7F;
                LinearLayout ll = (LinearLayout) view;
                ll.setBackgroundColor(c);
            } else if (columnIndex == 0) {
                LinearLayout ll = (LinearLayout) view;
                for(View v : ll.getTouchables()){
                    v.setTag(cursor.getInt(columnIndex));
                }
            } else {
                return false;
            }
            return true;
        }
    }
    
    class IdBinder implements SimpleCursorAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if(cursor.getColumnName(columnIndex).equals("_id")){
                view.setTag(cursor.getInt(columnIndex));
                return true;
            }
            return false;
        }
        
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Tasks").setContent(R.id.listViewTasks));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Time").setContent(R.id.listViewTime));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("ToDo").setContent(R.id.listViewTodo));
        mTabHost.setCurrentTab(0);



        tasklist = (ListView) findViewById(R.id.listViewTasks);
        tasklist.addFooterView(inflater.inflate(R.layout.taskadd, null));

        editNewTask = (EditText) findViewById(R.id.taskeditnew);

        taskHelper = new TaskDbAdapter(this);
        taskHelper.open();
        
        
        todolist = (ListView) findViewById(R.id.listViewTodo);
        todoHelper = new TodoDbAdapter(this);
        todoHelper.open();
        
        fillData();
        
        timelist = (ListView) findViewById(R.id.listViewTime);
        timelist.setAdapter(new ArrayAdapter<String>(this, R.layout.timeentry, R.id.timelabel, COUNTRIES));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timebasemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tbDay:

                break;
            case R.id.tb7Days:

                break;
            case R.id.tbWeek:

                break;
            case R.id.tbEver:

                break;
        }
        return true;
    }

    public void onTodoDone(View v) {
        final Button b = (Button) v;
        int id = (Integer) b.getTag();
        todoHelper.deleteTodo(id);
        fillData();

    }

    public void onNewTask(View v) {
        String label = editNewTask.getText().toString();
        long id = taskHelper.createTask(label, "" + label.hashCode());
        fillData();
    }
    
    public void onTaskTodo(View v) {
        Button b = (Button) v;
        final int id = (Integer) b.getTag();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Extra");
        alert.setMessage("Enter Notes");

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                todoHelper.createTodo(value, id);
                taskHelper.incPriority(id);
                fillData();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void onTask5min(View v) {
        Button b = (Button) v;
        b.setText("Awa");
    }

    public void onTask10min(View v) {
        Button b = (Button) v;
        b.setText("ToDo");
    }

    public void onTask30min(View v) {
        Button b = (Button) v;
        b.setText("ToDo");
    }

    public void onTask60min(View v) {
        Button b = (Button) v;
        b.setText("ToDo");
    }

    public void onTaskStart(View v) {
        Button b = (Button) v;
        b.setText("Stop");
    }
}