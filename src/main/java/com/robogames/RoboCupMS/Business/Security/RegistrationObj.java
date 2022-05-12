package com.robogames.RoboCupMS.Business.Security;

import java.util.Date;

/**
 * Uchovava registracni udaje uzivatele
 */
public class RegistrationObj {

    /**
     * Jmeno uzivatle
     */
    private String name;

    /**
     * Prijmeni uzivatele
     */
    private String surname;

    /**
     * Email uzivatele
     */
    private String email;

    /**
     * Heslo uzivatele
     */
    private String password;

    /**
     * Datum narozeni uzivatle
     */
    private Date birthDate;

    /**
     * Uchovava registracni udaje uzivatele
     */
    public RegistrationObj() {
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.birthDate = null;
    }

    /**
     * Uchovava registracni udaje uzivatele
     * 
     * @param _name      Jmeno noveho uzivatele
     * @param _surname   Prijmeni noveho uzivatle
     * @param _email     Email noveho uzivatle
     * @param _password  Heslo noveho uzivatle
     * @param _birthDate Datum narozeni noveho uzivatele
     */
    public RegistrationObj(String _name, String _surname, String _email, String _password, Date _birthDate) {
        this.name = _name;
        this.surname = _surname;
        this.email = _email;
        this.password = _password;
        this.birthDate = _birthDate;
    }

    /**
     * Navrati jmeno uzivatele
     * 
     * @return Jmeno
     */
    public String getName() {
        return this.name;
    }

    /**
     * Navrati prijmeni uzivatele
     * 
     * @return Prijmeni
     */
    public String getSurname() {
        return this.surname;
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

    /**
     * Navrati datum narozeni uzivatele
     * 
     * @return Datum narozeni
     */
    public Date getBirthDate() {
        return this.birthDate;
    }
}
