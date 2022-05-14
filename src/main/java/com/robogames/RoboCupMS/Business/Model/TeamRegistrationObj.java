package com.robogames.RoboCupMS.Business.Model;

public class TeamRegistrationObj {

    /**
     * Rocnik souteze, do ktereho se tym registruje
     */
    private int year;

    /**
     * Pokud bude true automaticky bude tym zarazen do kategorie OPEN. V opecnem
     * pripade bude zarazen do kategorie podle veku nejstarsiho clena tymu.
     */
    private boolean open;

    public TeamRegistrationObj() {
    }

    public TeamRegistrationObj(int year, boolean open) {
        this.year = year;
        this.open = open;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
