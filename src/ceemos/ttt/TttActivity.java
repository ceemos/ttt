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

public class TttActivity extends TabActivity {
	
	TabHost mTabHost;
	ListView tasklist;
	ListView timelist;
	ListView todolist;
	
	EditText editNewTask;
	
	private TaskDbAdapter dbHelper;
	private Cursor cursor;
	
	
	static String[] COUNTRIES = new String[] {
	    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
	    "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina"};
	
	private void fillData() {
		cursor = dbHelper.fetchAllTodos();
		startManagingCursor(cursor);

		String[] from = new String[] { TaskDbAdapter.KEY_LABEL };
		int[] to = new int[] { R.id.tasktext };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.taskentry, cursor, from, to);
		notes.setViewBinder(new viewBinder());
		tasklist.setAdapter(notes);
	}
	
	class  viewBinder implements SimpleCursorAdapter.ViewBinder {

		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			String color = cursor.getString(cursor.getColumnIndex(TaskDbAdapter.KEY_COLOR));
			int c = Integer.parseInt(color) & 0xFFFFFF;
			view.setBackgroundColor(c);
			return false;
		}
		
	}


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LayoutInflater inflater = (LayoutInflater) this.getSystemService
        	      (Context.LAYOUT_INFLATER_SERVICE);
        
        mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Tasks").setContent(R.id.listViewTasks));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Time").setContent(R.id.listViewTime));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("ToDo").setContent(R.id.listViewTodo));
        mTabHost.setCurrentTab(0);
        
        
        
        tasklist = (ListView) findViewById(R.id.listViewTasks);
        tasklist.addFooterView(inflater.inflate(R.layout.taskadd, null));
        
        editNewTask = (EditText) findViewById(R.id.taskeditnew);
        
        dbHelper = new TaskDbAdapter(this);
		dbHelper.open();
		fillData();
		
		
        timelist = (ListView) findViewById(R.id.listViewTime);
        timelist.setAdapter(new ArrayAdapter<String>(this,R.layout.timeentry, R.id.timelabel, COUNTRIES));
        
        todolist = (ListView) findViewById(R.id.listViewTodo);
        todolist.setAdapter(new ArrayAdapter<String>(this,R.layout.todoentry, R.id.todolabel, COUNTRIES));

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
    	if (b.getTag() == null){
    		b.setText("ToDo");
    		b.setTag(Boolean.TRUE);
    	} else {
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);

    		alert.setTitle("Extra");
    		alert.setMessage("Enter Notes");

    		// Set an EditText view to get user input 
    		final EditText input = new EditText(this);
    		alert.setView(input);

    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    		  String value = input.getText().toString();
    		  LinearLayout ll = (LinearLayout) b.getParent();
    		  TextView tv = (TextView) ll.findViewById(R.id.todoextra);
    		  tv.setText(value);
    		  // Do something with value!
    		  }
    		});

    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int whichButton) {
    		    // Canceled.
    		  }
    		});

    		alert.show();
    	}
    	
    }
    
    public void onNewTask(View v){
    	String label = editNewTask.getText().toString();
    	long id = dbHelper.createTodo(label, "" + label.hashCode());
    	fillData();
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