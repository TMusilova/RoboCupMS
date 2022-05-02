package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "playground")
public class Playground {

    /**
     * ID robota
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nazev hriste
     */
    @Column(name = "name", length = 40, nullable = false, unique = true)
    private String name;

    /**
     * Cislo hriste (melo by byt unikatni v ramci rocniku souteze)
     */
    @Column(name = "number", nullable = false, unique = false)
    private int number;

    /**
     * Vytvori soutezni hriste. V systemu urcitovani poradi pro jednoznacne urceni
     * konani mista zapasu.
     */
    public Playground() {
    }

    /**
     * Vytvori soutezni hriste. V systemu urcitovani poradi pro jednoznacne urceni
     * konani mista zapasu.
     * 
     * @param _name   Jmeno hriste
     * @param _number Cislo hriste
     */
    public Playground(String _name, int _number) {
        this.name = _name;
        this.number = _number;
    }

    /**
     * Navrati ID hriste
     * 
     * @return ID hriste
     */
    public long getID() {
        return this.id;
    }

    /**
     * Navrati jmeno hriste
     * 
     * @return Jmeno hriste
     */
    public String getName() {
        return this.name;
    }

    /**
     * Navrati cislo hriste
     * 
     * @return Cislo hriste
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Nastavi nove jmeno hriste
     * 
     * @param _name Nove jmeno hriste
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Nastavi nove cislo hriste
     * 
     * @param _number Nove cislo hriste
     */
    public void setNumber(int _number) {
        this.number = _number;
    }

}
