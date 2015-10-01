package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = MainUserSingleton.getInstance().getUser(getBaseContext());
        if (user != null){
            Intent intent = new Intent(MainActivity.this, LoginResult.class);
            intent.putExtra(EXTRA_LOGIN, user.getUsername().toString());
            intent.putExtra(EXTRA_PASSWORD, user.getPassword().toString());
            intent.putExtra("calling-activity", ActivityConstants.MainActivity);
            startActivity(intent);
        }
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
            case R.id.action_login:
                openLogin();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
