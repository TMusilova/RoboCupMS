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
    public static String HEADER_FIELD_TOKEN = "X-TOKEN";

    /**
     * Minimalni vek uzivatele
     */
    public static int USER_MIN_AGE = 6;

    /**
     * Maximalni vek uzivatele
     */
    public static int USER_MAX_AGE = 99;

    /**
     * Defaultni nastaveni kategorii
     * zakladni skola: max 15 let
     * stredni skola: max 19 let
     * vysoka skola: max 26 let
     * open kategorie: vic jak 26 let
     */

    /**
     * Maximalni vekove hranice pro zakladni skoly
     * (pro zruseni kategorie zadej zaporne cislo)
     */
    public static int ELEMENTARY_SCHOOL_MAX_AGE = 15;

    /**
     * Maximalni vekove hranice pro strdeni skoly
     * (pro zruseni kategorie zadej zaporne cislo)
     */
    public static int HIGH_SCHOOL_MAX_AGE = 19;

    /**
     * Maximalni vekova hranice pro vysoke skoly
     * (pro zruseni kategorie zadej zaporne cislo)
     */
    public static int UNIVERSITY_MAX_AGE = 26;

    /**
     * Maximalni pocet robotu v kategorii na jeden tym
     */
    public static int MAX_ROBOTS_IN_CATEGORY = 1;

    /**
     * Maximalni pocet clenu v jednom tymu
     */
    public static int MAX_TEAM_MEMBERS = 4;

    /**
     * Enkoder hesel
     */
    public static transient PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // -CONFIG-END--------------------------------------------------------------------

}
