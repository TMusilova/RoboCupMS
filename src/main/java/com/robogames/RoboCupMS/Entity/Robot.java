package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

}
