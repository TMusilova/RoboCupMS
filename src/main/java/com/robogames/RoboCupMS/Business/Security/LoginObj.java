package com.robogames.RoboCupMS.Business.Security;

/**
 * Uchovava prihlasovaci udaje uzivatele
 */
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

    /**
     * Navrati email uzivatele
     * 
     * @return Email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Navrati uzivatelske heslo
     * 
     * @return Heslo
     */
    public String getPassword() {
        return this.password;
    }

}
