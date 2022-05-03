package com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robogames.RoboCupMS.Entity.Robot;

/**
 * Uklada celkove dosazene skore robota v soutezi
 */
public class RobotScore {

    private final Robot robot;

    private final int score;

    /**
     * Uklada celkove dosazene skore robota v soutezi
     * 
     * @param _robot Robot
     * @param _score Celkove score
     */
    public RobotScore(Robot _robot, int _score) {
        this.robot = _robot;
        this.score = _score;
    }

    /**
     * Navrati robota
     * 
     * @return Robot
     */
    @JsonIgnore
    public Robot getRobot() {
        return this.robot;
    }

    /**
     * Navrati ID robota
     * 
     * @return Robot
     */
    public long getRobotID() {
        if (this.robot == null) {
            return -1;
        }
        return this.robot.getID();
    }

    /**
     * Navrati jmeno robota
     * 
     * @return Jmeno robota
     */
    public String getRobotName() {
        if (this.robot == null) {
            return "";
        }
        return this.robot.getName();
    }

    /**
     * Navrati ID discipliny, ve ktere robot soutezi
     * 
     * @return ID discipliny
     */
    public long getDisciplindeID() {
        return this.robot.getDisciplineID();
    }

    /**
     * Navrati nazev discipliny, ve ktere robot soutezi
     * 
     * @return Nazev discipliny
     */
    public String getDisciplindeName() {
        return this.robot.getDiciplineName();
    }

    /**
     * Navrati celkove skore
     * 
     * @return Celkove skore
     */
    public int getScore() {
        return this.score;
    }

}
