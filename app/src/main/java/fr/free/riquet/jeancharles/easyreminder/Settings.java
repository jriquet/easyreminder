package fr.free.riquet.jeancharles.easyreminder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uz.shift.colorpicker.LineColorPicker;

public class Settings extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1 ;
    static final int REQUEST_IMAGE_CAPTURE = 1;
        static final String photodetails = "fr.free.riquet.jeancharles.easyreminder.app_settings";
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText editTextUsername = (EditText) findViewById(R.id.editTextSettingsUsername);
        EditText editTextFirstname = (EditText) findViewById(R.id.editTextFirstname);
        EditText editTextLastname = (EditText) findViewById(R.id.editTextLastname);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextUsername.setText(MainUserSingleton.getInstance().getUser(getApplicationContext()).getUsername());
        editTextFirstname.setText(MainUserSingleton.getInstance().getUser(getApplicationContext()).get_firstname());
        editTextLastname.setText(MainUserSingleton.getInstance().getUser(getApplicationContext()).getLastname());
        editTextEmail.setText(MainUserSingleton.getInstance().getUser(getApplicationContext()).getEmailAddress());

        Button buttonNewProfilePicture = (Button) findViewById(R.id.buttonNewProfilePicture);
        buttonNewProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Button createSaveSettingsButton = (Button) findViewById(R.id.buttonSaveSettings);
        createSaveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify) {
            return true;
        } else if (id == R.id.action_delete) {
            return true;
        } else if (id == R.id.action_cancel) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
            ImageView mImageView = (ImageView) findViewById(R.id.mImageView);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        else
            setPic();
    }

    private void saveSettings() {
        EditText pass = (EditText) findViewById(R.id.editTextPassword);
        EditText rePass = (EditText) findViewById(R.id.editTextRetapePassword);
        EditText editTextFirstname = (EditText) findViewById(R.id.editTextFirstname);
        EditText editTextLastname = (EditText) findViewById(R.id.editTextLastname);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        TextView textView = (TextView) findViewById(R.id.textView_checkPasswordSettings);
        String firstname = editTextFirstname.getText().toString();
        String lastname = editTextLastname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = MainUserSingleton.getInstance().getUser(getApplicationContext()).getPassword();
        String newpass = pass.getText().toString();
        String ReNewPass = rePass.getText().toString();

        if (newpass.contentEquals(ReNewPass) && newpass.length() >= 1) {
            textView.setText("");
            password = Utilities.md5(newpass);
        } else if (newpass.contentEquals(ReNewPass) && newpass.length() == 0) {
            textView.setText("");
        } else {

            String message = getString(R.string.retapePassword);
            textView.setText(message);
            return;
        }

        MainUserSingleton.getInstance().getUser(getApplicationContext()).setPassword(password);
        MainUserSingleton.getInstance().getUser(getApplicationContext()).setFirstname(firstname);
        MainUserSingleton.getInstance().getUser(getApplicationContext()).setLastname(lastname);
        MainUserSingleton.getInstance().getUser(getApplicationContext()).setEmailAddress(email);
        DBHandlerSingleton.getInstance(getApplicationContext()).saveSettings(MainUserSingleton.getInstance().getUser(getApplicationContext()).getID(), firstname, lastname, email, password);
        Intent intent = new Intent(this, TasksList.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent_small() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = getDir(photodetails, 0);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        ImageView mImageView = (ImageView) findViewById(R.id.mImageView);
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        //int photoW = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions).getWidth();
        int photoH = bmOptions.outHeight;
        //int photoH = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions).getHeight();

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mImageView.setImageBitmap(bitmap);
    }
}
