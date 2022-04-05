package com.robogames.RoboCupMS;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GlobalConfig {

    // -CONFIG-START------------------------------------------------------------------

    /**
     * Prefix pro vsechny API serveru
     */
    public static final transient String API_PREFIX = "/api";

    /**
     * Prefix pouze pro sekci autentizace uzivatele
     */
    public static final transient String AUTH_PREFIX = "/auth";

    /**
     * Servis prefix
     */
    public static final transient String SERVICE_PREFIX = "/service";

    /**
     * Nazev promenne v headeru requestu pro pristupovy token
     */
    public static String HEADER__FIELD_TOKEN = "X-TOKEN";

    /**
     * Minimalni vek uzivatele
     */
    public static int USER_MIN_AGE = 6;

    /**
     * Maximalni vek uzivatele
     */
    public static int USER_MAX_AGE = 99;

    /**
     * Format datumu
     */
    public static String DATE_FORMAT = "yyyy-mm-dd";

    /**
     * Enkoder hesel
     */
    public static transient PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // -CONFIG-END--------------------------------------------------------------------

}
