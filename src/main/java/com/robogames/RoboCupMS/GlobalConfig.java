package com.robogames.RoboCupMS;

import java.io.Serializable;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GlobalConfig implements Serializable {

    // -CONFIG-START------------------------------------------------------------------

    /**
     * API prefix
     */
    public static final transient String API_PREFIX = "/api";

    /**
     * Minimalni vek uzivatele
     */
    public int USER_MIN_AGE = 6;

    /**
     * Maximalni vek uzivatele
     */
    public int USER_MAX_AGE = 99;

    /**
     * Format datumu
     */
    public String DATE_FORMAT = "yyyy-mm-dd";

    /**
     * Enkoder hesel
     */
    public PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // -CONFIG-END--------------------------------------------------------------------

    private static GlobalConfig conf = null;

    private GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        if (GlobalConfig.conf == null) {
            GlobalConfig.conf = new GlobalConfig();
        }
        return GlobalConfig.conf;
    }

}
