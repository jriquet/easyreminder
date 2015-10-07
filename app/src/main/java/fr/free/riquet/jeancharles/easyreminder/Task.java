package fr.free.riquet.jeancharles.easyreminder;

import android.graphics.Color;


public class Task {
    private long _id;
    private long _user_id;
    private String _name;
    private String _desc;
    private String _createdAt;
    private String _limiteDate;
    private int _color = Color.BLUE;

    public Task() {
        this._id = 0;
        this._name = " ";
        this._desc = " ";
        this._user_id = 0;
        this._createdAt = Utilities.getDateTime();
        this._limiteDate = Utilities.getDateTime();
    }

    public Task(String name, String desc, User user) {
        this._id = 0;
        this._name = name;
        this._desc = desc;
        this._user_id = user.getID();
        this._createdAt = Utilities.getDateTime();
        this._limiteDate = Utilities.getDateTime();
    }

    public Task(String name, String desc, User user, String limiteDate) {
        this._id = 0;
        this._name = name;
        this._desc = desc;
        this._user_id = user.getID();
        this._createdAt = Utilities.getDateTime();
        this._limiteDate = limiteDate;
    }

    public Task(int id, String name, String desc, User user, String createdAt, String limiteDate) {
        this._id = id;
        this._name = name;
        this._desc = desc;
        this._user_id = user.getID();
        this._createdAt = createdAt;
        this._limiteDate = limiteDate;
    }

    public Task(int id, String name, String desc, int userId, String createdAt, String limiteDate) {
        this._id = id;
        this._name = name;
        this._desc = desc;
        this._user_id = userId;
        this._createdAt = createdAt;
        this._limiteDate = limiteDate;
    }

    public boolean isEmpty() {
        return _name.isEmpty();
    }

    public long get_id() {
        return this._id;
    }

    public void set_id(long id) {
        this._id = id;
    }

    public long get_user_id() {
        return this._user_id;
    }

    public void set_user_id(long user_id) {
        this._user_id = user_id;
    }

    public String get_name() {
        return this._name;
    }

    public void set_name(String name) {
        this._name = name;
    }

    public String get_desc() {
        return this._desc;
    }

    public void set_desc(String desc) {
        this._desc = desc;
    }

    public String get_createdAt() {
        return this._createdAt;
    }

    public void set_createdAt(String createdAt) {
        this._createdAt = createdAt;
    }

    public String get_limiteDate() {
        return this._limiteDate;
    }

    public void set_limiteDate(String limiteDate) {
        this._limiteDate = limiteDate;
    }

    public int get_color() {
        return this._color;
    }

    public void set_color(int color) {
        this._color = color;
    }
}
