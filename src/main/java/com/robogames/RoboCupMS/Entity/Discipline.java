package com.robogames.RoboCupMS.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "discipline")
public class Discipline {

    /**
     * Navratova hodnota ID "zadne" discipliny (navrati pokud robot neni registrovan
     * v zadne
     * discipline)
     */
    public static final int NOT_REGISTRED = -1;

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
     * Seznam vsech hrist pro tuto disciplinu
     */
    @OneToMany(mappedBy = "discipline", fetch = FetchType.EAGER)
    private List<Playground> playgrounds;

    /**
     * Disciplina, ve ktere muzou roboti soutezit
     */
    public Discipline() {
        this.playgrounds = new ArrayList<Playground>();
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
        this.playgrounds = new ArrayList<Playground>();
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
     * Navrati seznam vsech hrist pro tuto disciplinu
     * @return
     */
    @JsonIgnore
    public List<Playground> getPlaygrounds() {
        return this.playgrounds;  
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
