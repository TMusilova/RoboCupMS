package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Robot, se ktery soutezi tymy
 */
@Entity(name = "robot")
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cislo robota (unikatni v ramci rocniku a sve kategorie)
     */
    @Column(name = "number", length = 40, nullable = false, unique = false)
    private Long number;

    /**
     * Jmeno robota (unikatni v ramci rocniku)
     */
    @Column(name = "name", length = 40, nullable = false, unique = false)
    private String name;

    /**
     * Robot se vytvari na registraci tymu do souteze (kazdeho robota je tedy nutne
     * znovu vytvaret pro kazdy rocnik znovu)
     */
    @ManyToOne
    private TeamRegistration teamRegistration;

    /**
     * Vytvori robota, ten pokud se prihlasi do urcite kategorie tak muze zapasit
     */
    public Robot() {

    }

    /**
     * Vytvori robota, ten pokud se prihlasi do urcite kategorie tak muze zapasit
     * 
     * @param _name             Jmeno robota
     * @param _number           Cislo robota (toto cislo identifikuje robota pri
     *                          jednotlivych
     *                          zapasech)
     * @param _teamRegistration Registrace tymu, na kterou je robot vytvoren
     */
    public Robot(String _name, long _number, TeamRegistration _teamRegistration) {
        this.name = _name;
        this.number = _number;
        this.teamRegistration = _teamRegistration;
    }

    /**
     * Navrati ID robota
     * 
     * @return ID
     */
    public Long getID() {
        return this.id;
    }

    /**
     * Navrati jmeno robota
     * 
     * @return Jmeno robota
     */
    public String getName() {
        return this.name;
    }

    /**
     * Navrati identifikacni cislo robota
     * 
     * @return Cislo robota
     */
    public Long getNumber() {
        return this.number;
    }

    /**
     * Navrati ID registrace tymu
     * 
     * @return ID registrace tymu
     */
    public Long getTeamRegistrationID() {
        return this.teamRegistration.getID();
    }

    /**
     * Navrati jmeno tymu, ktery tohoto robota vlastni
     * 
     * @return Jmeno tymu
     */
    public String getTeamName() {
        return this.teamRegistration.getTeamName();
    }

    /**
     * Nastavi nove jmeno robota
     * 
     * @param _name Nove jmeno robota
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Nastavi nove identifikacni cislo robota
     * 
     * @param _number Nove identifikacni cislo
     */
    public void setNumber(long _number) {
        this.number = _number;
    }

    /**
     * Nastavi tymovou registraci v damen rocniku souteze
     * 
     * @param _registration Tymova registrace
     */
    public void setTeamRegistration(TeamRegistration _registration) {
        this.teamRegistration = _registration;
    }

}
