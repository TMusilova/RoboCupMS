package com.robogames.RoboCupMS.Controller;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Business.Service.MatchService;
import com.robogames.RoboCupMS.Entity.RobotMatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/match")
public class MatchControler {

    @Autowired
    private MatchService matchService;

    /**
     * Navrati vsechny zapasy
     * 
     * @return Seznam vsech zapasu
     */
    @GetMapping("/all")
    Response getAll() {
        List<RobotMatch> all = this.matchService.getAll();
        return ResponseHandler.response(all);
    }

    /**
     * Navrati vsechny zapasy pro konkretni rocnik
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech zapasu
     */
    @GetMapping("/allByYear")
    Response allByYear(@RequestParam int year) {
        List<RobotMatch> matches;
        try {
            matches = this.matchService.allByYear(year);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(matches);
    }

    /**
     * Naplanuje novy zapas
     * 
     * @param robotID      ID robota, ktery bude zapasit
     * @param playgroundID ID hriste, na kterem se bude hrat
     * @param groupID      ID zapasove skupiny. Jen v pripade pokud zapasi proti
     *                     sobe vice robotu. V opacnem pripade zadat neplatnou
     *                     zapornou hodnotu.
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam long robotID, @RequestParam long playgroundID, @RequestParam long groupID) {
        try {
            this.matchService.create(robotID, playgroundID, groupID);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odstrani zapas
     * 
     * @param id ID zapasu
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response remove(@RequestParam long id) {
        try {
            this.matchService.remove(id);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odstrani vsechny zapasy, ktere nalezi do urcite zkupiny
     * 
     * @param groudID ID skupiny, jejiz zapasy maji byt odstraneni
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/removeAll")
    Response removeAll(@RequestParam long groupID) {
        try {
            int cnt = this.matchService.removeAll(groupID);
            return ResponseHandler.response("success, removed [" + cnt + "]");

        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Zapise vysledne skore zapasu
     * 
     * @param id    ID zapasu
     * @param score Skore zapasu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/writeScore")
    Response writeScore(@RequestParam long id, @RequestParam float score) {
        try {
            this.matchService.writeScore(id, score);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Vyzada, aby zapas byl odehran znovu. Pokud jde o skupinovy zapas automaticky
     * tento pozadavek vyzada i u ostatnich zapasu.
     * 
     * @param id ID zapasu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/rematch")
    Response rematch(@RequestParam long id) {
        try {
            this.matchService.rematch(id);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

}
