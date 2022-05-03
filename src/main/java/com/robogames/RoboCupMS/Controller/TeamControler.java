package com.robogames.RoboCupMS.Controller;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Business.Service.TeamService;
import com.robogames.RoboCupMS.Entity.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/team")
public class TeamControler {

    @Autowired
    private TeamService teamService;

    /**
     * Navrati info o tymu, ve kterem se prihlaseny uzivatel nachazi
     * 
     * @param id ID tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/myTeam")
    Response myTeam() {
        Team team;
        try {
            team = this.teamService.myTeam();
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(team);
    }

    /**
     * Navrati info o tymu s konkretnim ID
     * 
     * @param id ID tymu
     * 
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/findByID")
    Response findID(@RequestParam Long id) {
        Team team;
        try {
            team = this.teamService.findID(id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(team);
    }

    /**
     * Navrati info o tymu s konkretnim jmenem
     * 
     * @param name Jmeno tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/findByName")
    Response findName(@RequestParam String name) {
        Team team;
        try {
            team = this.teamService.findName(name);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(team);
    }

    /**
     * Navrati vsechny tymy
     * 
     * @return Seznam vsech tymu
     */
    @GetMapping("/all")
    Response getAll() {
        List<Team> all = this.teamService.getAll();
        return ResponseHandler.response(all);
    }

    /**
     * Vytvori v databazi novy tym
     * 
     * @param name Jmeno tymu (unikatni!!)
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam String name) {
        try {
            this.teamService.create(name);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odstrani tym z databaze
     * 
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/delete")
    Response delete() {
        try {
            this.teamService.delete();
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Prejmenuje tym
     * 
     * @param name Nove jmeno tymu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/rename")
    Response rename(@RequestParam String name) {
        try {
            this.teamService.rename(name);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Prida do tymu noveho clena
     * 
     * @param id ID clena, ktery ma byt pridat do tymu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/addMember")
    Response addMember(@RequestParam String uuid) {
        try {
            this.teamService.addMember(uuid);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odebere z tymu jednoho clena
     * 
     * @param id ID clena, ktery ma byt odebran z tymu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/removeMember")
    Response removeMember(@RequestParam String uuid) {
        try {
            this.teamService.removeMember(uuid);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Opusti tym, ve ktrem se prihlaseny uzivatel aktualne nachazi
     * 
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/leave")
    Response leave() {
        try {
            this.teamService.leaveTeam();
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

}
