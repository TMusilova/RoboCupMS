package com.robogames.RoboCupMS.Business.Security;

import java.util.Date;

public class RegistrationObj {

    private String name;

    private String surname;

    private String email;

    private String password;

    private Date birthDate;

    public RegistrationObj() {
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.birthDate = null;
    }

    public RegistrationObj(String _name, String _surname, String _email, String _password, Date _birthDate) {
        this.name = _name;
        this.surname = _surname;
        this.email = _email;
        this.password = _password;
        this.birthDate = _birthDate;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }
}
