package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "discipline")
public class Discipline {

    /**
     * ID discipliny
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Nazev discipliny
     */
    @Column(name = "name", length = 80, nullable = false, unique = false)
    private String name;

    /**
     * Popis discipliny
     */
    @Column(name = "description", length = 8192, nullable = true, unique = false)
    private String description;

    /**
     * Disciplina, ve ktere muzou roboti soutezit
     */
    public Discipline() {

    }

    /**
     * Disciplina, ve ktere muzou roboti soutezit
     * 
     * @param _name        Nazev discipliny
     * @param _description Popis discipliny (max 8192 znaku)
     */
    public Discipline(String _name, String _description) {
        this.name = _name;
        this.description = _description;
    }

    /**
     * Navrati ID discipliny
     * 
     * @return ID
     */
    public Long getID() {
        return this.id;
    }

    /**
     * Navrati nazev discipliny
     * 
     * @return Naze discipliny
     */
    public String getName() {
        return this.name;
    }

    /**
     * Navrati popis discipliny
     * 
     * @return Popis discipliny
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Nastavi novy nazev discipline
     * 
     * @param _name Novy nazev
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Nastavi novy popis discipliny
     * 
     * @param _description Novy popis discipliny (max 8192 znaku)
     */
    public void setDescription(String _description) {
        this.description = _description;
    }

}
