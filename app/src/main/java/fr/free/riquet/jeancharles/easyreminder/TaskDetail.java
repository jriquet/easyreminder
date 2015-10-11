package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

public class TaskDetail extends AppCompatActivity {
    final String EXTRA_TASK_ID = "task_id";
    final String EXTRA_TASK_NAME = "task_name";
    final String EXTRA_TASK_DESC = "task_desc";
    final String EXTRA_TASK_DONE = "task_done";
    final String EXTRA_TASK_COLOR = "task_color";
    final String EXTRA_TASK_LIMITE_DATE = "task_limite_date";
    long task_id;
    String task_name;
    String task_desc;
    String task_limitedate;
    Boolean task_done;
    int task_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setSupportActionBar(toolbar);

        LineColorPicker colorPicker = (LineColorPicker) findViewById(R.id.picker);

        // set color palette
        colorPicker.setColors(new int[]{Color.parseColor("#b8c847"), Color.parseColor("#67bb43"), Color.parseColor("#41b691"),
                Color.parseColor("#4182b6"), Color.parseColor("#4149b6"), Color.parseColor("#7641b6"), Color.parseColor("#b741a7"),
                Color.parseColor("#c54657"), Color.parseColor("#d1694a")});

        // set on change listener
        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                task_color = c;
                //Log.d(TAG, "Selected color " + Integer.toHexString(c));
            }
        });

        // get selected color
        //task_color = colorPicker.getColor();

        Bundle extras = getIntent().getExtras();
        task_id = extras.getLong(EXTRA_TASK_ID);
        task_name = extras.getString(EXTRA_TASK_NAME);
        setTitle(task_name);
        task_desc = extras.getString(EXTRA_TASK_DESC);
        task_done = extras.getBoolean(EXTRA_TASK_DONE);
        task_color = extras.getInt(EXTRA_TASK_COLOR);
        colorPicker.setSelectedColor(task_color);

        task_limitedate = extras.getString(EXTRA_TASK_LIMITE_DATE);
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);

        switch (callingActivity) {
            case ActivityConstants.TasksList:
                EditText name = (EditText) findViewById(R.id.editTextTaskName);
                EditText desc = (EditText) findViewById(R.id.editTextTaskDescription);
                DatePicker date = (DatePicker) findViewById(R.id.datePickerTask);
                TimePicker time = (TimePicker) findViewById(R.id.timePickerTask);
                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxTaskDetail);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        task_done = isChecked;
                    }
                });
                name.setText(task_name);
                desc.setText(task_desc);
                checkBox.setChecked(task_done);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
                Date theDate = new Date();
                try {
                    theDate = format.parse(task_limitedate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar myCal = new GregorianCalendar();
                myCal.setTime(theDate);
                int Year = myCal.get(Calendar.YEAR);
                int Month = myCal.get(Calendar.MONTH);
                int Day = myCal.get(Calendar.DAY_OF_MONTH);
                int Hour = myCal.get(Calendar.HOUR_OF_DAY);
                int Min = myCal.get(Calendar.MINUTE);
                date.init(Year, Month, Day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Notify the user.
                    }
                });
                time.setIs24HourView(true);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.M){
                    time.setHour(Hour);
                    time.setMinute(Min);
                } else{
                    time.setCurrentHour(Hour);
                    time.setCurrentMinute(Min);
                }


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
            TimePicker time = (TimePicker) findViewById(R.id.timePickerTask);
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxTaskDetail);
            task_name = editTextName.getText().toString();
            task_desc = editTextDesc.getText().toString();
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.M){
                task_limitedate = Integer.toString(datePicker.getYear()) + "-" +
                        Integer.toString(datePicker.getMonth() + 1) + "-" +
                        Integer.toString(datePicker.getDayOfMonth()) + "-" +
                        Integer.toString(time.getHour()) + ":" +
                        Integer.toString(time.getMinute());
            }
            else
            {
                task_limitedate = Integer.toString(datePicker.getYear()) + "-" +
                        Integer.toString(datePicker.getMonth() + 1) + "-" +
                        Integer.toString(datePicker.getDayOfMonth()) + "-" +
                        Integer.toString(time.getCurrentHour()) + ":" +
                        Integer.toString(time.getCurrentMinute());
            }

            DBHandlerSingleton.getInstance(getApplicationContext()).updateTask(task_id, task_name, task_desc, task_limitedate);
            DBHandlerSingleton.getInstance(getApplicationContext()).setColorTask(task_id, task_color);
            DBHandlerSingleton.getInstance(getApplicationContext()).setTaskDone(task_id, task_done);
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
