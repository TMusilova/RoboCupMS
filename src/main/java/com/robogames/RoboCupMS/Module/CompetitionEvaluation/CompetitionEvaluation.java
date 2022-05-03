package com.robogames.RoboCupMS.Module.CompetitionEvaluation;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Business.model.CompetitionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ulehcuje praci z vyhodnocovanim souteze
 */
@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/competitionEvaluation")
public class CompetitionEvaluation {

    /**
     * Navrati skore a poradi vsech robotu, kteri soutezili v danem rocniku
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech roboku a jejich skore v soutezi
     */
    @GetMapping("/scoreOfAll")
    Response getScoreOfAll(@RequestParam int year) {
        return ResponseHandler.response("");
    }

    /**
     * Navrati skore vsech robotu urciteho tymu
     * 
     * @param year Rocnik
     * @param id   ID tymu
     * @return Navrati skore vsech reobotu v tymu
     */
    @GetMapping("/scoreOfTeam")
    Response getScoreOfTeam(@RequestParam int year, @RequestParam long id) {
        return ResponseHandler.response("");
    }

    /**
     * Navrati skore robota
     * 
     * @param year Rocnik
     * @param id   ID robota
     * @return Navrati skore robota
     */
    @GetMapping("/scoreOfRobot")
    Response getScoreOfRobot(@RequestParam int year, @RequestParam long id) {
        return ResponseHandler.response("");
    }

    /**
     * Navrati viteze ve vsech kategoriich
     * 
     * @param year Rocnik souteze
     * @return Seznam vitezu vsech kategorii
     */
    @GetMapping("/winners")
    Response getWinners(@RequestParam int year) {
        return ResponseHandler.response("");
    }

    /**
     * Navrati data pro tisk na diplom za danou disciplinu a misto.
     * 
     * @param year  Rocnik souteze
     * @param id    ID discipliny
     * @param place Umisteni robota v discipline (1-3)
     * @return
     */
    @GetMapping("/dataForPrinting")
    Response getDataForPrinting(@RequestParam int year, @RequestParam long id, @RequestParam int place) {
        return ResponseHandler.response("");
    }

}
