package fr.free.riquet.jeancharles.easyreminder;

import android.content.Context;
import android.content.SharedPreferences;

public class MainUserSingleton {
    public static String path_userName = "username";
    public static String path_password = "password";
    public static String app_settings = "fr.free.riquet.jeancharles.easyreminder";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor SPEditor;
    private static MainUserSingleton ourInstance = new MainUserSingleton();
    private static User userRegistered = new User();

    private MainUserSingleton() {
    }

    public static MainUserSingleton getInstance() {
        return ourInstance;
    }

    private static void saveUserDetails(String name, String pass, Context context) {
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        SharedPreferences.Editor SPEditor = settings.edit();
        SPEditor.putString(path_userName, name);
        SPEditor.putString(path_password, pass);
        SPEditor.apply();
    }

    private static boolean getUserDetails(Context context) {
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        String name = settings.getString(path_userName, null);
        String pass = settings.getString(path_password, null);
        if (name != null && pass != null) {
            User user = DBHandlerSingleton.getInstance(context).findUser(name, pass);
            if (user != null) {
                userRegistered.setUsername(name);
                userRegistered.setPassword(pass);
                userRegistered.setID(user.getID());
                return true;
            } else
                return false;
        }
        return false;
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
        SPEditor.putString(path_userName, null);
        SPEditor.putString(path_password, null);
        SPEditor.apply();
    }

    public SharedPreferences getSharedPreference(){
        return settings;
    }

    public void setSharedPreferences(Context context){
        settings = context.getSharedPreferences(app_settings, Context.MODE_PRIVATE);
        SPEditor = settings.edit();
    }
}
