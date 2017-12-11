package me.luizclaudiosantos.flatFiles.model;

import java.util.Date;

public class Customer {

    private long id;

    private String firstName;

    private String lasName;

    private Date birthday;

    public Customer(long id, String firstName, String lasName, Date birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lasName = lasName;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLasName() {
        return lasName;
    }

    public Date getBirthday() {
        return birthday;
    }
}
