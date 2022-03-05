package com.robogames.RoboCupMS.Entity;

/**
 * Kategorie tymu
 */
public enum TeamCategory {
    /**
     * Kategorie pro zakladni skolu
     */
    ELEMENTARY_SCHOOL("elementary_school"),

    /**
     * Kategorie pro stredni skolu
     */
    HIGH_SCHOOL("high_school"),

    /**
     * Kategorie pro vysokou skolu
     */
    UNIVERSITY("university"),

    /**
     * Kategorie pro verejnost (bez omezeni)
     */
    OPEN("open");

    private final String val;

    private TeamCategory(String _val) {
        this.val = _val;
    }

    /**
     * Navrati nazev kategorie v podobe stringu
     * @return String
     */
    @Override
    public String toString() {
        return this.val;
    }
}