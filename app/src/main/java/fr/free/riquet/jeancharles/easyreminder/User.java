package fr.free.riquet.jeancharles.easyreminder;

/**
 * Created by Jean-Charles on 14/09/2015.
 */
public class User {
    private int _id;
    private String _username;
    private String _password;
    private String _firstname;
    private String _lastname;
    private String _emailaddress;

    public User() {

    }

    public User(int id, String username, String password) {
        this._id = id;
        this._username = username;
        this._password = password;
    }

    public User(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getUsername() {
        return this._username;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getPassword() {
        return this._password;
    }

    public void setFirstname(String firstname) {
        this._firstname = firstname;
    }

    public String get_firstname() { return this._firstname; }

    public void setLastname(String lastname) {
        this._lastname = lastname;
    }

    public String getLastname() {
        return this._lastname;
    }

    public void setEmailAddress(String emailAddress) {
        this._emailaddress = emailAddress;
    }

    public String getEmailAddress() {
        return this._emailaddress;
    }
}
