package me.luizclaudiosantos.flatFiles.domain;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import java.util.Date;

public class Customer implements ResourceAware {

    private final long id;

    private final String firstName;

    private final String lastName;

    private final Date birthdate;

    private Resource resource;

    public Customer(long id, String firstName, String lastName, Date birthdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
