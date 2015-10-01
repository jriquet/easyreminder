package fr.free.riquet.jeancharles.easyreminder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jean-Charles on 23/09/2015.
 */
public class MainUserSingleton {
    public static String userName_path = "fr.free.riquet.jeancharles.easyreminder.username";
    public static String password_path = "fr.free.riquet.jeancharles.easyreminder.password";
    public static String userdetails = "fr.free.riquet.jeancharles.easyreminder.userdetails";
    private static MainUserSingleton ourInstance = new MainUserSingleton();
    private static User userRegistered = new User();

    public static MainUserSingleton getInstance() {
        return ourInstance;
    }

    private MainUserSingleton() {
    }

    public void setUser(User user, Context context){
        userRegistered = user;
        saveUserDetails(user.getUsername().toString(), user.getPassword().toString(), context);
    }

    public User getUser(Context context){
        if (getUserDetails(context))
            return userRegistered;
        else
            return null;
    }

    public void disconnect(Context context){
        SharedPreferences settings = context.getSharedPreferences(userdetails, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(null, userName_path);
        editor.putString(null, password_path);
        editor.commit();
    }


    private static void saveUserDetails(String userName, String password, Context context) {
        SharedPreferences settings = context.getSharedPreferences(userdetails, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(userName, userName_path);
        editor.putString(password, password_path);
        editor.commit();
    }

    private static boolean getUserDetails(Context context){
        SharedPreferences settings = context.getSharedPreferences(userdetails, 0);
        String userName = settings.getString(userName_path, null);
        String password = settings.getString(password_path, null);
        if (userName != null && password != null) {
            MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
            User user = dbHandler.findUser(userName, password);
            if (user != null){
                userRegistered.setUsername(userName.toString());
                userRegistered.setPassword(password.toString());
                return true;
            }
            else
                return false;
        }
        return false;
    }
}
