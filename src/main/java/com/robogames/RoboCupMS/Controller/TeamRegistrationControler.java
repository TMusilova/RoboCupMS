package com.robogames.RoboCupMS.Controller;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Business.Service.TeamRegistrationService;
import com.robogames.RoboCupMS.Entity.TeamRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/teamRegistration")
public class TeamRegistrationControler {

    @Autowired
    private TeamRegistrationService registrationService;

    /**
     * Registruje tym do souteze (registrovat muze pouze vedouci tymu!!!!!)
     * 
     * @param year Rocni souteze, do ktere se tym chce registrovate
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/register")
    Response register(@RequestParam int year, @RequestParam Boolean open) {
        try {
            this.registrationService.register(year, open);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Zrusi registraci tymu
     * 
     * @param year Rocni souteze
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/unregister")
    Response unregister(@RequestParam int year) {
        try {
            this.registrationService.unregister(year);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Navrati vsechny registrace tymu, ve kterem se uzivatel nachazi (vsehny
     * rocniky, kterych se ucastnil)
     * 
     * @return Seznam vsech registraci
     */
    @GetMapping("/all")
    Response getAll() {
        List<TeamRegistration> all = null;
        try {
            all = this.registrationService.getAll();
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(all);
    }

}
