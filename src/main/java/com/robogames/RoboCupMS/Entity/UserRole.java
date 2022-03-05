package com.robogames.RoboCupMS.Entity;

/**
 * Role uzivatele
 */
public enum UserRole {

    /**
     * Role soutezici
     */
    COMPETITOR("competitor"),

    /**
     * Role admin
     */
    ADMIN("admin"),

    /**
     * Role vedouci
     */
    LEADER("leader"),

    /**
     * Role asistent
     */
    ASSISTANT("assistant"),

    /**
     * Role rozhodci
     */
    REFEREE("referee");

    private final String val;

    private UserRole(String _val) {
        this.val = _val;
    }

    /**
     * Navrati nazev role v podobe stringu
     * 
     * @return String
     */
    @Override
    public String toString() {
        return this.val;
    }
}
