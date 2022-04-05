package com.robogames.RoboCupMS.Enum;

/**
 * Role uzivatele
 */
public enum ERole {

    /**
     * Role soutezici
     */
    COMPETITOR(ERole.Names.COMPETITOR),

    /**
     * Role admin
     */
    ADMIN(ERole.Names.ADMIN),

    /**
     * Role vedouci
     */
    LEADER(ERole.Names.LEADER),

    /**
     * Role asistent
     */
    ASSISTANT(ERole.Names.ASSISTANT),

    /**
     * Role rozhodci
     */
    REFEREE(ERole.Names.REFEREE);

    public static class Names {
        public static final String COMPETITOR = "COMPETITOR";
        public static final String ADMIN = "ADMIN";
        public static final String LEADER = "LEADER";
        public static final String ASSISTANT = "ASSISTANT";
        public static final String REFEREE = "REFEREE";
    }

    private final String label;

    private ERole(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }

}
