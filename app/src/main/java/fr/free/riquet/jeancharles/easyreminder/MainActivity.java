package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";
    public static String app_settings = "fr.free.riquet.jeancharles.easyreminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainUserSingleton singleton = MainUserSingleton.getInstance();

        if(singleton.getSharedPreference() == null){
            singleton.setSharedPreferences(getApplicationContext());
        }
        User user = MainUserSingleton.getInstance().getUser(getApplicationContext());
        if (user != null){
            Intent intent = new Intent(MainActivity.this, LoginResult.class);
            intent.putExtra(EXTRA_LOGIN, user.getUsername());
            intent.putExtra(EXTRA_PASSWORD, user.getPassword());
            intent.putExtra("calling-activity", ActivityConstants.MainActivity);
            startActivity(intent);
        }

        Button loginButton = (Button) findViewById(R.id.button_loginSubmit);
        Button newUserButton = (Button)  findViewById(R.id.button_newUser);

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewUser();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_new_user:
                openNewUser();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkLogin(){
        EditText login = (EditText) findViewById(R.id.editText_loginUsername);
        EditText pass = (EditText) findViewById(R.id.editText_loginPassword);
        TextView messageWrongLogin = (TextView) findViewById(R.id.textViewWrongLogin);
        String userName = login.getText().toString();
        String password = Utilities.md5(pass.getText().toString());
        MyDBHandler dbHandler = new MyDBHandler(getBaseContext(), null, null, 1);
        User userLogin = dbHandler.findUser(userName, password);
        if ((userLogin == null) & (dbHandler.checkUser(userName)))
        {
            messageWrongLogin.setText("Wrong password for user : "+userName);
        }
        else if (dbHandler.checkUser(userName) == false)
        {
            messageWrongLogin.setText("user "+userName+"does not exist");
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginResult.class);
            intent.putExtra(EXTRA_LOGIN, userName);
            intent.putExtra(EXTRA_PASSWORD, password);
            intent.putExtra("calling-activity", ActivityConstants.Login);
            startActivity(intent);
        }
    }

    public void openNewUser(){
        Intent intent = new Intent(this, NewUser.class);
        startActivity(intent);
    }
}
