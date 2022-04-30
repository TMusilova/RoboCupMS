package com.robogames.RoboCupMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.robogames.RoboCupMS.Enum.ECategory;

/**
 * Entita reprezentujici kategorie, ve kterych muze tym soutezit
 */
@Entity(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ECategory name;

    public Category() {
    }

    public Category(ECategory name) {
        this.name = name;
    }

    public Integer getID() {
        return id;
    }

    public void getID(Integer id) {
        this.id = id;
    }

    public ECategory getName() {
        return name;
    }

    public void setName(ECategory name) {
        this.name = name;
    }

}
