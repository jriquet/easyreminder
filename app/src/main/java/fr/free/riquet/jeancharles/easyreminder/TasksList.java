package fr.free.riquet.jeancharles.easyreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TasksList extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";
    final String EXTRA_TASK_ID = "task_id";
    final String EXTRA_TASK_NAME = "task_name";
    final String EXTRA_TASK_DESC = "task_desc";
    final String EXTRA_TASK_LIMITE_DATE = "task_limite_date";
    final Context context = this;
    List<Task> tasksList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Bundle extras = getIntent().getExtras();

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
        if (id == R.id.action_add_task) {
            callAddTaskDialog();
            return true;
        }
        else if (id == R.id.action_settings) {
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

        addTaskDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String desc = editTextDesc.getText().toString();
                String date = Integer.toString(datePicker.getYear()) + "-" +
                        Integer.toString(datePicker.getMonth() + 1) + "-" +
                        Integer.toString(datePicker.getDayOfMonth()) + "-" +
                        Utilities.getTime();
                addTask(name, desc, date);
            }
        });

        addTaskDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        addTaskDialog.show();
    }

    public void addTask(String name, String desc, String date) {
        Task taskToAdd = new Task(name, desc, MainUserSingleton.getInstance().getUser(getApplicationContext()), date);
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
                intent.putExtra(EXTRA_TASK_LIMITE_DATE, taskToModify.get_limiteDate());
                intent.putExtra("calling-activity", ActivityConstants.TasksList);
                startActivity(intent);
            }
        });
    }
}
