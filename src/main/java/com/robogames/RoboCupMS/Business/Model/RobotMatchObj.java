package com.robogames.RoboCupMS.Business.Model;

public class RobotMatchObj {

    private long robotID;
    private long playgroundID;
    private long groupID;

    public RobotMatchObj() {
    }

    public RobotMatchObj(long robotID, long playgroundID, long groupID) {
        this.robotID = robotID;
        this.playgroundID = playgroundID;
        this.groupID = groupID;
    }

    public long getRobotID() {
        return this.robotID;
    }

    public void setRobotID(long robotID) {
        this.robotID = robotID;
    }

    public long getPlaygroundID() {
        return this.playgroundID;
    }

    public void setPlaygroundID(long playgroundID) {
        this.playgroundID = playgroundID;
    }

    public long getGroupID() {
        return this.groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

}
