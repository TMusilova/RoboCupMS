package com.robogames.RoboCupMS.Business.Model;

public class PlaygroundObj {

    private String name;
    private int number;
    private Long disciplineID;


    public PlaygroundObj() {
    }

    public PlaygroundObj(String name, int number, Long disciplineID) {
        this.name = name;
        this.number = number;
        this.disciplineID = disciplineID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getDisciplineID() {
        return this.disciplineID;
    }

    public void setDisciplineID(Long disciplineID) {
        this.disciplineID = disciplineID;
    }

}
