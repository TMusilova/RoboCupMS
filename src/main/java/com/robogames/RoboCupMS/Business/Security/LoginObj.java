package com.robogames.RoboCupMS.Business.Security;


public class LoginObj {

    private String email;

    private String password;

    public LoginObj() {
        this.email = null;
        this.password = null;
    }

    public LoginObj(String _email, String _password) {
        this.email = _email;
        this.password = _password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

}
