package com.robogames.RoboCupMS.Entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "competition")
public class Competition {

    /**
     * ID tymu
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Rocnik souteze
     */
    @Column(name = "year", nullable = false, unique = true)
    private int year;

    /**
     * Datum souteze
     */
    @Column(name = "date", nullable = false, unique = false)
    private Date date;

    /**
     * Zacatek souteze
     */
    @Column(name = "start_time", nullable = false, unique = false)
    private Time startTime;

    /**
     * Konec souteze
     */
    @Column(name = "end_time", nullable = false, unique = false)
    private Time endTime;

    /**
     * Info o stavu souteze (zacala/nezacala)
     */
    @Column(name = "started", nullable = false, unique = false)
    private Boolean started;

    public Competition() {

    }

    public Competition(int _year, Date _date, Time _start, Time _end) {
        this.year = _year;
        this.date = _date;
        this.startTime = _start;
        this.endTime = _end;
        this.started = false;
    }

    /**
     * Navrati ID souteze
     * 
     * @return ID souteze
     */
    public Long getID() {
        return this.id;
    }

    /**
     * Navrati rocnik souteze
     * 
     * @return Rocnik souteze
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Narati datum konani souteze
     * 
     * @return Datum konani souteze
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Navrati cas zacatku souteze
     * 
     * @return Cas zacatku souteze
     */
    public Time getStartTime() {
        return this.startTime;
    }

    /**
     * Navrati cas konce souteze
     * 
     * @return Cas konce souteze
     */
    public Time getEndTime() {
        return this.endTime;
    }

    /**
     * Navrati stav souteze (zacala/nazacala)
     * 
     * @return Stav souteze
     */
    public Boolean getStarted() {
        return this.started;
    }

    /**
     * Nastavi rocnik
     * 
     * @param _year Rocnik souteze
     */
    public void setYear(int _year) {
        this.year = _year;
    }

    /**
     * Nastavi datum konani
     * 
     * @param _date Datum konani souteze
     */
    public void setDate(Date _date) {
        this.date = _date;
    }

    /**
     * Nastavi cas zacatku souteze
     * 
     * @param _time Cas zacatku souteze
     */
    public void setStartTime(Time _time) {
        this.startTime = _time;
    }

    /**
     * Nastavi cas konce souteze
     * 
     * @param _time Cas konce souteze
     */
    public void setEndTime(Time _time) {
        this.endTime = _time;
    }

    /**
     * Nastavi stav souteze (zacala/nezacala)
     * 
     * @param s Stav
     */
    public void setStarted(Boolean s) {
        this.started = s;
    }

}
