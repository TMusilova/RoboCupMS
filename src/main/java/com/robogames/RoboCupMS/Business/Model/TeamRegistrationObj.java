package com.robogames.RoboCupMS.Business.Model;

public class TeamRegistrationObj {
    
    private int year;
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
