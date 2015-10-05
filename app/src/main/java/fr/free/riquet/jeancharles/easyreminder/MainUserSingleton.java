package fr.free.riquet.jeancharles.easyreminder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jean-Charles on 23/09/2015.
 */
public class MainUserSingleton {
    public static String userName = "username";
    public static String password = "password";
    public static String app_settings = "fr.free.riquet.jeancharles.easyreminder";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor SPEditor;
    private static MainUserSingleton ourInstance = new MainUserSingleton();
    private static User userRegistered = new User();

    public static MainUserSingleton getInstance() {
        return ourInstance;
    }

    private MainUserSingleton() {
    }

    public void setUser(User user, Context context){
        userRegistered = user;
        saveUserDetails(user.getUsername(), user.getPassword(), context);
    }

    public User getUser(Context context){
        if (getUserDetails(context))
            return userRegistered;
        else
            return null;
    }

    public void disconnect(Context context){
        SPEditor.putString(userName, null);
        SPEditor.putString(password, null);
        SPEditor.commit();
    }


    private static void saveUserDetails(String name, String pass, Context context) {
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        SharedPreferences.Editor SPEditor = settings.edit();
        SPEditor.putString(userName, name);
        SPEditor.putString(password, pass);
        SPEditor.commit();
    }

    private static boolean getUserDetails(Context context){
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        String name = settings.getString(userName, null);
        String pass = settings.getString(password, null);
        if (name != null && pass != null) {
            MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
            User user = dbHandler.findUser(name, pass);
            if (user != null){
                userRegistered.setUsername(name);
                userRegistered.setPassword(pass);
                return true;
            }
            else
                return false;
        }
        return false;
    }

    public SharedPreferences getSharedPreference(){
        return settings;
    }

    public void setSharedPreferences(Context context){
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        SPEditor = settings.edit();
    }
}
