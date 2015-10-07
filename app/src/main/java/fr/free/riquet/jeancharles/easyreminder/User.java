package fr.free.riquet.jeancharles.easyreminder;

public class User {
    private long _id;
    private String _username;
    private String _password;
    private String _firstname;
    private String _lastname;
    private String _emailaddress;

    public User() {
        this._id = 0;
        this._username = " ";
        this._password = " ";
        this._firstname = " ";
        this._lastname = " ";
        this._emailaddress = " ";
    }

    public User(String username, String password) {
        this._id = 0;
        this._username = username;
        this._password = password;
        this._firstname = " ";
        this._lastname = " ";
        this._emailaddress = " ";
    }

    public boolean isEmpty() {
        return _username.isEmpty() || _password.isEmpty();
    }

    public long getID() {
        return this._id;
    }

    public void setID(long id) {
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
