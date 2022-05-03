package com.robogames.RoboCupMS.API;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Business.model.PlaygroundService;

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

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/playground")
public class PlaygroundControler {

    @Autowired
    private PlaygroundService playgroundService;

    /**
     * Navrati vsechny hriste
     * 
     * @return Seznam vsech souteznich hrist
     */
    @GetMapping("/all")
    Response getAll() {
        List<Playground> all = this.playgroundService.getAll();
        return ResponseHandler.response(all);
    }

    /**
     * Navrati vsechny hriste pro urcitou disciplinu
     * 
     * @return Seznam vsech souteznich hrist pro urcitou disciplinu
     */
    @GetMapping("/get")
    Response get(@RequestParam Long id) {
        List<Playground> playgrounds;
        try {
            playgrounds = this.playgroundService.get(id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(playgrounds);
    }

    /**
     * Navrati vsechny zapasy odehrane na konkretnim hristi
     * 
     * @return Seznam zapasu
     */
    @GetMapping("/getMatches")
    Response getMatches(@RequestParam Long id) {
        List<RobotMatch> matches;
        try {
            matches = this.playgroundService.getMatches(id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(matches);
    }

    /**
     * Vytvori nove soutezni hriste
     * 
     * @param name         Jmeno noveho hriste
     * @param number       Cislo noveho hriste
     * @param disciplineID ID discipliny, pro ktere bude nove vytvorene hriste
     *                     urcene
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PostMapping("/create")
    Response create(@RequestParam String name, @RequestParam int number, @RequestParam Long disciplineID) {
        try {
            this.playgroundService.create(name, number, disciplineID);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odstrani soutezni hriste
     * 
     * @param id ID hriste, ktere ma byt odstraneno
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @DeleteMapping("/remove")
    Response remove(@RequestParam Long id) {
        try {
            this.playgroundService.remove(id);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Upravi parametry souzezniho hriste
     * 
     * @param id         ID hriste, ktere ma byt upraveno
     * @param playground Nove parametry hriste
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @PutMapping("/edit")
    Response edit(@RequestParam Long id, @RequestBody Playground playground) {
        try {
            this.playgroundService.edit(id, playground);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

}
