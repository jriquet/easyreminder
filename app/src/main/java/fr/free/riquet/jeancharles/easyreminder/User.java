package fr.free.riquet.jeancharles.easyreminder;

public class User {
    private int _id;
    private String _username;
    private String _password;
    private String _firstname;
    private String _lastname;
    private String _emailaddress;

    public User() {
        this._username = " ";
        this._password = " ";
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

    public boolean isEmpty() {
        return _username.isEmpty() || _password.isEmpty();
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getUsername() {
        return this._username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getPassword() {
        return this._password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public void setFirstname(String firstname) {
        this._firstname = firstname;
    }

    public String get_firstname() { return this._firstname; }

    public String getLastname() {
        return this._lastname;
    }

    public void setLastname(String lastname) {
        this._lastname = lastname;
    }

    public String getEmailAddress() {
        return this._emailaddress;
    }

    public void setEmailAddress(String emailAddress) {
        this._emailaddress = emailAddress;
    }
}
