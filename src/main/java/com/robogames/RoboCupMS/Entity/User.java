package com.robogames.RoboCupMS.Entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robogames.RoboCupMS.GlobalConfig;

/**
 * Uzivatel aplikace
 */
@Entity
public class User {

    /**
     * ID uzivatele
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Jmeno uzivatele
     */
    @Column(name = "name", length = 40, nullable = false, unique = false)
    private String name;

    /**
     * Prijmeni uzivatele
     */
    @Column(name = "surname", length = 60, nullable = false, unique = false)
    private String surname;

    /**
     * Email uzivatele
     */
    @Column(name = "email", length = 120, nullable = false, unique = true)
    private String email;

    /**
     * Heslo uzivatele
     */
    @Column(name = "password", nullable = false, unique = false)
    private String password;

    /**
     * Datum narozeni uzivatele
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "birthDate", nullable = false, unique = false)
    private Date birthDate;

    /**
     * Role uzivatele
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = false)
    private UserRole role;

    public User() {
    }

    /**
     * Vytvori noveho uzivatele
     * 
     * @param _name      Jmeno uzivatele
     * @param _surname   Prijmeni uzivatele
     * @param _email     Email uzivatele
     * @param _birthDate Datum narozeni
     * @param _role      Role uzivatele
     */
    public User(String _name, String _surname, String _email, Date _birthDate, UserRole _role) {
        this.name = _name;
        this.surname = _surname;
        this.email = _email;
        this.setBirthDate(_birthDate);
        this.role = _role;
    }

    /**
     * Navrati ID uzivatele
     * 
     * @return ID
     */
    public long getID() {
        return this.id;
    }

    /**
     * Nastavi nove ID uzivateli
     * 
     * @param _id Nove ID
     */
    public void setID(long _id) {
        this.id = _id;
    }

    /**
     * Navrati jmeno uzivatele
     * 
     * @return Jmeno uzivatele
     */
    public String getName() {
        return this.name;
    }

    /**
     * Nastavi nove jmeno uzivatele
     * 
     * @param _name Nove jmeno
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Navrati prijmeni uzivatele
     * 
     * @return Prijmeni uzivatele
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Nastavi nove prijmeni uzivatele
     * 
     * @param _surname Nove prijmeni
     */
    public void setSurname(String _surname) {
        this.surname = _surname;
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
     * Nastavi novy email uzivatele
     * 
     * @param _email Novy email
     */
    public void setEmail(String _email) {
        this.email = _email;
    }

    /**
     * Navrati heslo uzivatel (HASH)
     * 
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Nastavi nove heslo pro uzivatel
     * 
     * @param _password Nove heslo (plain text)
     */
    public void setPassword(String _password) {
        this.password = GlobalConfig.getInstance().PASSWORD_ENCODER.encode(_password);
    }

    /**
     * Navrati vek uzivatele
     * 
     * @return Vek
     */
    @JsonIgnore
    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        if ((this.birthDate != null) && (currentDate != null)) {
            LocalDate bd = birthDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            return Period.between(bd, currentDate).getYears();
        } else {
            return 0;
        }
    }

    /**
     * Navrati datum narozeni
     * 
     * @return Datum narozeni
     */
    public Date getBirthDate() {
        return this.birthDate;
    }

    /**
     * Nastavi datum narozeni
     * 
     * @param _age Vek uzivatele
     */
    public void setBirthDate(Date _birthDate) {
        // z datumu narozeni vypocit vek uzivatele
        LocalDate currentDate = LocalDate.now();
        LocalDate bd = _birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        int age = Period.between(bd, currentDate).getYears();

        // vek musi splnovat omezeni
        if (age >= GlobalConfig.getInstance().USER_MIN_AGE &&
                age <= GlobalConfig.getInstance().USER_MAX_AGE) {
            this.birthDate = _birthDate;
        }
    }

    /**
     * Role uzivatele
     * 
     * @return Role
     */
    public UserRole getRole() {
        return this.role;
    }

    /**
     * Nastaveni role uzivatele
     * 
     * @param _role Role
     */
    public void setRole(UserRole _role) {
        this.role = _role;
    }

}
