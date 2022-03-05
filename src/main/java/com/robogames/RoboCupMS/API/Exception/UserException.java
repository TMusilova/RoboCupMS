package com.robogames.RoboCupMS.API.Exception;

public abstract class UserException {

    public static class NotFound extends RuntimeException {
        public NotFound(Long _id) {
            super("Could not find user " + _id);
        }
    }

}