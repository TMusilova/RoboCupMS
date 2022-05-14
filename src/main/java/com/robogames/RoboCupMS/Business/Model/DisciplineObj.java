package com.robogames.RoboCupMS.Business.Model;

import com.robogames.RoboCupMS.Business.Enum.EScoreAggregation;

public class DisciplineObj {
    
    private String name;
    private String description;
    private EScoreAggregation scoreAggregation;
    private int time;
    private int maxRounds;


    public DisciplineObj() {
    }

    public DisciplineObj(String name, String description, EScoreAggregation scoreAggregation, int time, int maxRounds) {
        this.name = name;
        this.description = description;
        this.scoreAggregation = scoreAggregation;
        this.time = time;
        this.maxRounds = maxRounds;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EScoreAggregation getScoreAggregation() {
        return this.scoreAggregation;
    }

    public void setScoreAggregation(EScoreAggregation scoreAggregation) {
        this.scoreAggregation = scoreAggregation;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxRounds() {
        return this.maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

}
