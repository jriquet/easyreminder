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

public class NewUser extends AppCompatActivity {
    public static String userName = "fr.free.riquet.jeancharles.easyreminder.username";
    public static String password = "fr.free.riquet.jeancharles.easyreminder.password";

    final String EXTRA_LOGIN = "user_login";
    final String EXTRA_PASSWORD = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        Button createUserButton = (Button) findViewById(R.id.button_NewUserSubmit);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.editNewUserName);
                EditText pass = (EditText) findViewById(R.id.editNewUserPassword);
                EditText rePass = (EditText) findViewById(R.id.editNewUserRePassword);
                if (pass.getText().toString().equals(rePass.getText().toString())) {
                    TextView textView = (TextView) findViewById(R.id.textView_checkPassword);
                    textView.setText("");
                    Intent intent = new Intent(NewUser.this, LoginResult.class);
                    intent.putExtra(EXTRA_LOGIN, login.getText().toString());
                    intent.putExtra(EXTRA_PASSWORD, Utilities.md5(pass.getText().toString()));
                    intent.putExtra("calling-activity", ActivityConstants.NewUser);
                    startActivity(intent);
                } else {
                    TextView textView = (TextView) findViewById(R.id.textView_checkPassword);
                    textView.setText("Please retape your password");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_login:
                openLogin();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
