package com.robogames.RoboCupMS.Business.Security;

/**
 * Uchovava prihlasovaci udaje uzivatele
 */
public class LoginObj {

    /**
     * Email uzivate
     */
    private String email;

    /**
     * Heslo uzivatele
     */
    private String password;

    /**
     * Uchovava prihlasovaci udaje uzivatele
     */
    public LoginObj() {
        this.email = null;
        this.password = null;
    }

    /**
     * Uchovava prihlasovaci udaje uzivatele
     * 
     * @param _email    Email uzivatele
     * @param _password Heslo uzivatele
     */
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
