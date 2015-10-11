package fr.free.riquet.jeancharles.easyreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

public class TasksList extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";
    final String EXTRA_TASK_ID = "task_id";
    final String EXTRA_TASK_NAME = "task_name";
    final String EXTRA_TASK_DESC = "task_desc";
    final String EXTRA_TASK_DONE = "task_done";
    final String EXTRA_TASK_COLOR = "task_color";
    final String EXTRA_TASK_LIMITE_DATE = "task_limite_date";
    final Context context = this;
    List<Task> tasksList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Bundle extras = getIntent().getExtras();
        ActionBar ab =getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.mipmap.ic_launcher);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_task_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddTaskDialog();
            }
        });

        String userName = " ";
        String password = " ";
        int callingActivity = ActivityConstants.TaskDetail;
        User user = null;

        if (extras == null) {
            user = MainUserSingleton.getInstance().getUser(this);
        } else {
            userName = extras.getString(EXTRA_LOGIN);
            password = extras.getString(EXTRA_PASSWORD);
            callingActivity = getIntent().getIntExtra("calling-activity", 0);
        }

        switch (callingActivity) {
            case ActivityConstants.Login:
                user = DBHandlerSingleton.getInstance(getApplicationContext()).findUser(userName, password);
                break;
            case ActivityConstants.NewUser:
                user = new User(userName, password);
                DBHandlerSingleton.getInstance(getApplicationContext()).addUser(user);
                break;
            case ActivityConstants.MainActivity:
                user = DBHandlerSingleton.getInstance(getApplicationContext()).findUser(userName, password);
                break;
        }
        MainUserSingleton.getInstance().setUser(user, getApplicationContext());
        fillTasksList();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            MainUserSingleton.getInstance().disconnect(getApplicationContext());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callAddTaskDialog()
    {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_new_task, null);
        AlertDialog.Builder addTaskDialog = new AlertDialog.Builder(context);
        addTaskDialog.setView(promptsView);
        addTaskDialog.setTitle(getString(R.string.action_add_task));
        addTaskDialog.setIcon(android.R.drawable.ic_input_add);

        final EditText editTextName = (EditText) promptsView.findViewById(R.id.editTextAddTaskName);
        final EditText editTextDesc = (EditText) promptsView.findViewById(R.id.editTextAddTaskDescription);
        final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePickerAddTask);
        final TimePicker time = (TimePicker) promptsView.findViewById(R.id.addTaskTimePickerTask);
        final LineColorPicker colorPicker = (LineColorPicker) promptsView.findViewById(R.id.addTaskPickerColor);
        time.setIs24HourView(true);
        // set color palette
        colorPicker.setColors(new int[]{Color.parseColor("#b8c847"), Color.parseColor("#67bb43"), Color.parseColor("#41b691"),
                Color.parseColor("#4182b6"), Color.parseColor("#4149b6"), Color.parseColor("#7641b6"), Color.parseColor("#b741a7"),
                Color.parseColor("#c54657"), Color.parseColor("#d1694a")});

        // set on change listener
        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                //Log.d(TAG, "Selected color " + Integer.toHexString(c));
            }
        });
        colorPicker.setSelectedColor(Color.parseColor("#b8c847"));

        addTaskDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String desc = editTextDesc.getText().toString();
                String date;
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.M){
                    date = Integer.toString(datePicker.getYear()) + "-" +
                            Integer.toString(datePicker.getMonth() + 1) + "-" +
                            Integer.toString(datePicker.getDayOfMonth()) + "-" +
                            Integer.toString(time.getHour()) + ":" +
                            Integer.toString(time.getMinute());
                }
                else
                {
                    date = Integer.toString(datePicker.getYear()) + "-" +
                            Integer.toString(datePicker.getMonth() + 1) + "-" +
                            Integer.toString(datePicker.getDayOfMonth()) + "-" +
                            Integer.toString(time.getCurrentHour()) + ":" +
                            Integer.toString(time.getCurrentMinute());
                }
                int color = colorPicker.getColor();
                addTask(name, desc, date, color);
            }
        });

        addTaskDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        addTaskDialog.show();
    }

    public void addTask(String name, String desc, String date, int color) {
        Task taskToAdd = new Task(name, desc, MainUserSingleton.getInstance().getUser(getApplicationContext()), date, color);
        long id = DBHandlerSingleton.getInstance(getApplicationContext()).addTask(taskToAdd);
        if (id != -1) {
            taskToAdd.set_id(id);
            fillTasksList();
        }
    }

    public void fillTasksList() {
        if (tasksList != null) {
            tasksList.clear();
        }

        tasksList = DBHandlerSingleton.getInstance(getApplicationContext()).getTasksList(MainUserSingleton.getInstance().getUser(getApplicationContext()));
        TaskAdapter adapter = new TaskAdapter(this, tasksList);
        ListView listViewTasks = (ListView) findViewById(R.id.listViewTasks);
        if (adapter != null) {
            listViewTasks.setAdapter(adapter);
        }
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Task taskToModify = (Task) parent.getAdapter().getItem(position);
                Intent intent = new Intent(TasksList.this, TaskDetail.class);
                intent.putExtra(EXTRA_TASK_ID, taskToModify.get_id());
                intent.putExtra(EXTRA_TASK_NAME, taskToModify.get_name());
                intent.putExtra(EXTRA_TASK_DESC, taskToModify.get_desc());
                Boolean checked = taskToModify.get_checked();
                intent.putExtra(EXTRA_TASK_DONE, taskToModify.get_checked());
                intent.putExtra(EXTRA_TASK_COLOR, taskToModify.get_color());
                intent.putExtra(EXTRA_TASK_LIMITE_DATE, taskToModify.get_limiteDate());
                intent.putExtra("calling-activity", ActivityConstants.TasksList);
                startActivity(intent);
            }
        });
    }
}
