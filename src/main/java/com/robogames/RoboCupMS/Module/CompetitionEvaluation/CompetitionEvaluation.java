package com.robogames.RoboCupMS.Module.CompetitionEvaluation;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Service.CompetitionEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ulehcuje praci z vyhodnocovanim souteze
 */
@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/competitionEvaluation")
public class CompetitionEvaluation {

    @Autowired
    private CompetitionEvaluationService competitionEvaluationService;

    /**
     * Navrati skore a poradi vsech robotu, kteri soutezili v danem rocniku
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech roboku a jejich skore v soutezi
     */
    @GetMapping("/scoreOfAll")
    Response getScoreOfAll(@RequestParam int year) {
        try {
            this.competitionEvaluationService.getScoreOfAll(year);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
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
        try {
            this.competitionEvaluationService.getScoreOfTeam(year, id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
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
        try {
            this.competitionEvaluationService.getScoreOfRobot(year, id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
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
        try {
            this.competitionEvaluationService.getWinners(year);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
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
        try {
            this.competitionEvaluationService.getDataForPrinting(year, id, place);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response("");
    }

}
