package com.robogames.RoboCupMS.Module.OrderManagement;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Business.Enum.ECategory;
import com.robogames.RoboCupMS.Module.OrderManagement.Bussiness.Service.OrderManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Zajistuje zobrazovani aktualniho poradi zapasu a jejich generovani
 */
@RestController
@RequestMapping(GlobalConfig.MODULE_PREFIX + "/orderManagement")
public class OrderManagement {

    @Autowired
    private OrderManagementService competitionEvaluationService;

    /**
     * Navrati seznam vsech zapasu, ktere aktualne maji byt odehrany (vyzva k odjeti
     * zapasu)
     * 
     * @param year     Rocnik souteze
     * @param category Soutezni kategorie
     * @return Seznam vsech robotu a hrist, na ktere se maji dostavit pro odehrani
     *         zapasu
     */
    @GetMapping("/currentMatches")
    Response currentMatches(@RequestParam int year, @RequestParam ECategory category) {
        try {
            this.competitionEvaluationService.currentMatches(year, category);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response("");
    }

    /**
     * Vyzada zmenu zapasu, ktery ma byt aktulane odehran na nekterem z hrist dane
     * discipliny
     * 
     * @param year     Rocnik souteze
     * @param id       ID dalsiho zapasu, ktery rozhodci chce, aby byl odehran
     *                 (pokud
     *                 bude zadana zaporna neplatna hodnota pak system vybere
     *                 nahodne ze
     *                 seznamu cekajicih zapasu)
     * @param category Soutezni kategorie
     * @return
     */
    @PutMapping("/requestAnotherMatch")
    Response requestAnotherMatch(@RequestParam int year, @RequestParam long id, @RequestParam ECategory category) {
        try {
            this.competitionEvaluationService.requestAnotherMatch(year, id, category);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response("");
    }

    /**
     * Navrati pro robota seznam vsech zapasu, ktere cekaji na odehrani
     * 
     * @param year     Rocnik souteze
     * @param id       ID robota
     * @param category Soutezni kategorie
     * @return Seznam vsech
     */
    @GetMapping("/waitingMatches")
    Response waitingMatches(@RequestParam int year, @RequestParam long id, @RequestParam ECategory category) {
        try {
            this.competitionEvaluationService.waitingMatches(year, id, category);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response("");
    }

    /**
     * Vygeneruje skupinove zapasy "kazdy s kazdym" (sumo, robo strong, ...)
     * 
     * @param year   Rocnik souteze
     * @param robots Seznam ID robotu, pro ktere se zapasy vytvori
     * @return Navrati identifikacni cislo tvurce zapasovych skupin (nasledne muze
     *         byt uplatneno pro odstraneni zapasu)
     */
    @PostMapping("/generateMatches")
    Response generateMatches(@RequestParam int year, @RequestBody List<Long> robots) {
        try {
            this.competitionEvaluationService.generateMatches(year, robots);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response("");
    }

}
