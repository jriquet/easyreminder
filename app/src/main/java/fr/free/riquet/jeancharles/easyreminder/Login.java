package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }
        else if (id == R.id.action_logout) {
            MainUserSingleton.getInstance().disconnect(getBaseContext());
            return true;
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
        User userLogin = dbHandler.findUser(userName,password);
        if ((userLogin == null) & (dbHandler.checkUser(userName)))
        {
            messageWrongLogin.setText("Wrong password for user : "+userName);
        }
        else if (dbHandler.checkUser(userName) == false)
        {
            messageWrongLogin.setText("user "+userName+"does not exist");
        }
        else {
            Intent intent = new Intent(Login.this, LoginResult.class);
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

    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
