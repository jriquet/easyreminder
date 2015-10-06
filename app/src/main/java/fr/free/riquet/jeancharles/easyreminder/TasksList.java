package fr.free.riquet.jeancharles.easyreminder;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TasksList extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString(EXTRA_LOGIN);
        String password = extras.getString(EXTRA_PASSWORD);
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        User user = null;

        switch (callingActivity) {
            case ActivityConstants.Login:
                user = dbHandler.findUser(userName, password);
                break;
            case ActivityConstants.NewUser:
                user = new User(userName, password);
                dbHandler.addUser(user);
                break;
            case ActivityConstants.MainActivity:
                user = dbHandler.findUser(userName, password);
                break;
        }
        MainUserSingleton.getInstance().setUser(user, getApplicationContext());
        String message = getString(R.string.welcome) + " " + userName;
        textView.setText(message);
        setContentView(textView);
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
        AlertDialog.Builder addTaskDialog = new AlertDialog.Builder(this);
        addTaskDialog.setView(getLayoutInflater().inflate(R.layout.dialog_new_task, null));
        //addTaskDialog.setTitle(R.string.action_add_task);
        addTaskDialog.setTitle(getString(R.string.action_add_task));
        addTaskDialog.setIcon(android.R.drawable.ic_input_add);
        /*
        addTaskDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //EditText et = (EditText) alertDialogView.findViewById(R.id.EditText1);
                //Toast.makeText(Tutoriel18_Android.this, et.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        addTaskDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });*/
        addTaskDialog.show();
    }
}
