package com.robogames.RoboCupMS.Business.Model;

public class MatchGroupObj {

    private Long creatorID;

    public MatchGroupObj() {
    }

    public MatchGroupObj(Long creatorID) {
        this.creatorID = creatorID;
    }

    public Long getCreatorID() {
        return this.creatorID;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

}
