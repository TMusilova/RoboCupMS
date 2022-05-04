package com.robogames.RoboCupMS.Module.OrderManagement.Bussiness.Model;

import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.Robot;

/**
 * Uchovava, ktery robot ma jit jako dalsi zapasit
 */
public class NextMatch {

    private final Robot robot;

    private final Playground playground;

    /**
     * Uchovava, ktery robot ma jit jako dalsi zapasit
     * 
     * @param _robot      Robot, ktery ma jit zapasit
     * @param _playground Hriste, na ktere ma jit zapasit
     */
    public NextMatch(Robot _robot, Playground _playground) {
        this.robot = _robot;
        this.playground = _playground;
    }

    /**
     * Navrati ID robota, ktery se ma dostavit na zapas
     * 
     * @return ID robota
     */
    public long getRobotID() {
        return this.robot.getID();
    }

    /**
     * Navrati jmeno robota, ktery se ma dostavit na zapas
     * 
     * @return Jmeno robota
     */
    public long getRobotNumber() {
        return this.robot.getNumber();
    }

    /**
     * Navrati ID hriste, na ktere se ma robot dostavit k odehrani zapasu
     * 
     * @return ID hriste
     */
    public long getPlaygroundID() {
        return this.playground.getID();
    }

    /**
     * Navrati cislo hriste, na ktere se ma robot dostavit k odehrani zapasu
     * 
     * @return Cislo hriste
     */
    public long getPlaygroundNumber() {
        return this.playground.getNumber();
    }

}
