package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskDetail extends AppCompatActivity {
    final String EXTRA_TASK_ID = "task_id";
    final String EXTRA_TASK_NAME = "task_name";
    final String EXTRA_TASK_DESC = "task_desc";
    final String EXTRA_TASK_LIMITE_DATE = "task_limite_date";
    long task_id;
    String task_name;
    String task_desc;
    String task_limitedate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        Bundle extras = getIntent().getExtras();
        task_id = extras.getLong(EXTRA_TASK_ID);
        task_name = extras.getString(EXTRA_TASK_NAME);
        task_desc = extras.getString(EXTRA_TASK_DESC);
        task_limitedate = extras.getString(EXTRA_TASK_LIMITE_DATE);
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);

        switch (callingActivity) {
            case ActivityConstants.TasksList:
                EditText name = (EditText) findViewById(R.id.editTextTaskName);
                EditText desc = (EditText) findViewById(R.id.editTextTaskDescription);
                DatePicker date = (DatePicker) findViewById(R.id.datePickerTask);
                name.setText(task_name);
                desc.setText(task_desc);
                SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm");
                Date theDate = new Date();
                try {
                    theDate = format.parse(task_limitedate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar myCal = new GregorianCalendar();
                myCal.setTime(theDate);
                int Year = myCal.get(Calendar.YEAR);
                int Month = myCal.get(Calendar.MONTH) + 1;
                int Day = myCal.get(Calendar.DAY_OF_MONTH);
                date.init(Year, Month, Day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Notify the user.
                    }
                });
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            Intent intent = new Intent(this, TasksList.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            DBHandlerSingleton.getInstance(getApplicationContext()).deleteTask(task_id);
            Intent intent = new Intent(this, TasksList.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_modify) {
            EditText editTextName = (EditText) findViewById(R.id.editTextTaskName);
            EditText editTextDesc = (EditText) findViewById(R.id.editTextTaskDescription);
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerTask);
            task_name = editTextName.getText().toString();
            task_desc = editTextDesc.getText().toString();
            task_limitedate = Integer.toString(datePicker.getYear()) + "-" +
                    Integer.toString(datePicker.getMonth() + 1) + "-" +
                    Integer.toString(datePicker.getDayOfMonth()) + "-" +
                    Utilities.getTime();
            DBHandlerSingleton.getInstance(getApplicationContext()).updateTask(task_id, task_name, task_desc, task_limitedate);
            Intent intent = new Intent(this, TasksList.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            MainUserSingleton.getInstance().disconnect(getApplicationContext());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
