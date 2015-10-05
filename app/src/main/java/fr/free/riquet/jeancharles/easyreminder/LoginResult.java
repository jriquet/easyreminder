package fr.free.riquet.jeancharles.easyreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginResult extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("user_login");
        String password = extras.getString("user_password");
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);

        switch (callingActivity) {
            case ActivityConstants.Login:
                User userLogin = dbHandler.findUser(userName,password);
                MainUserSingleton.getInstance().setUser(userLogin, getApplicationContext());

                SharedPreferences settings = getSharedPreferences("fr.free.riquet.jeancharles.easyreminder", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(EXTRA_LOGIN, userName);
                editor.putString(EXTRA_PASSWORD, password);
                editor.commit();

                textView.setText("Welcome back "+userName);
                break;
            case ActivityConstants.NewUser:
                User newUser = new User(userName,password);
                dbHandler.addUser(newUser);
                MainUserSingleton.getInstance().setUser(newUser, getApplicationContext());
                textView.setText("User " + userName + " created :-)");

                SharedPreferences settings2 = getSharedPreferences("fr.free.riquet.jeancharles.easyreminder", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putString(EXTRA_LOGIN, userName);
                editor2.putString(EXTRA_PASSWORD, password);
                editor2.commit();
                break;
            case ActivityConstants.MainActivity:
                User user = dbHandler.findUser(userName,password);
                MainUserSingleton.getInstance().setUser(user, getApplicationContext());
                textView.setText("Welcome back " + userName);

                SharedPreferences settings3 = getSharedPreferences("fr.free.riquet.jeancharles.easyreminder", MODE_PRIVATE);
                SharedPreferences.Editor editor3 = settings3.edit();
                editor3.putString(EXTRA_LOGIN, userName);
                editor3.putString(EXTRA_PASSWORD, password);
                editor3.commit();
                break;
        }
        setContentView(textView);

        final ListView listview = (ListView) findViewById(R.id.listViewTask);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_result, menu);
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
            callLoginDialog();
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

    private void callLoginDialog()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(LoginResult.this);
        myDialog.setView(getLayoutInflater().inflate(R.layout.dialog_new_task, null));
        myDialog.setCancelable(true);
        myDialog.create();

        final Dialog test = myDialog.create();
        Button buttonAddTask = (Button) findViewById(R.id.buttonAddTask);
        Button buttonCancelTask = (Button) findViewById(R.id.buttonCancelAddTask);

        final EditText taskName = (EditText) findViewById(R.id.editTextNameTask);
        final EditText taskDesc = (EditText) findViewById(R.id.editTextTaskDescription);;
        myDialog.show();

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyDBHandler dbHandler = new MyDBHandler(getBaseContext(), null, null, 1);
                //dbHandler.addTask(taskName.getText().toString(), taskDesc.getText().toString(), Utilities.getDateTime());
                test.dismiss();
            }
        });
/*
        buttonCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.dismiss();
            }
        });
*/

    }
}
