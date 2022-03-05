package com.robogames.RoboCupMS.Entity;

/**
 * Stav zapasu
 */
public enum MatchState {
    /**
     * zapas ceka na odehrani (jeste neni znamo skore)
     */
    WAITING("waiting"),

    /**
     * Zapas byl jiz odehran
     */
    DONE("done"),

    /**
     * Zopakovat zapas znovu (skore pro tento zapas se prepise)
     */
    AGAIN("again");

    private final String val;

    private MatchState(String _val) {
        this.val = _val;
    }

    /**
     * Navrati stav zapasu v podobe stringu
     * @return String
     */
    @Override
    public String toString() {
        return this.val;
    }
}
