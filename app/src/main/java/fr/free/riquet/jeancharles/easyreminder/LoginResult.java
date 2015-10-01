package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class LoginResult extends AppCompatActivity {
    public static String userName_path = "fr.free.riquet.jeancharles.easyreminder.username";
    public static String password_path = "fr.free.riquet.jeancharles.easyreminder.password";
    public static String userdetails = "fr.free.riquet.jeancharles.easyreminder.userdetails";

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
                MainUserSingleton.getInstance().setUser(userLogin, getBaseContext());
                textView.setText("Welcome back "+userName);
                break;
            case ActivityConstants.NewUser:
                User newUser = new User(userName,password);
                dbHandler.addUser(newUser);
                MainUserSingleton.getInstance().setUser(newUser, getBaseContext());
                textView.setText("User " + userName + " created :-)");
                break;
            case ActivityConstants.MainActivity:
                User user = dbHandler.findUser(userName,password);
                MainUserSingleton.getInstance().setUser(user, getBaseContext());
                textView.setText("Welcome back " + userName);
                break;
        }
        setContentView(textView);


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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            MainUserSingleton.getInstance().disconnect(getBaseContext());
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
