package com.robogames.RoboCupMS.API;

import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Repository.TeamRepository;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Navrati info o tymu s konkretnim ID
     * 
     * @param id ID tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/infoByID")
    Response getInfoID(@RequestParam Long id) {
        Optional<Team> team = this.teamRepository.findById(id);
        if (team.isPresent()) {
            return ResponseHandler.response(team);
        } else {
            return ResponseHandler.error(String.format("Team with ID [%d] not found", id));
        }
    }

    /**
     * Navrati info o tymu s konkretnim jmenem
     * 
     * @param name Jmeno tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/infoByName")
    Response getInfoName(@RequestParam String name) {
        Optional<Team> team = this.teamRepository.findByName(name);
        if (team.isPresent()) {
            return ResponseHandler.response(team);
        } else {
            return ResponseHandler.error(String.format("Team with Name [%s] not found", name));
        }
    }

    /**
     * Vytvori v databazi novy tym
     * 
     * @param name Jmeno tymu (unikatni!!)
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam String name) {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (leader.getTeam() == null) {
            Team t = new Team(name, leader);
            return ResponseHandler.response(this.teamRepository.save(t));
        } else {
            return ResponseHandler.error("failure, you already have team");
        }
    }

    /**
     * Odstrani tym z databaze
     * 
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/delete")
    Response delete() {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            this.teamRepository.delete(t.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error("failure, you are not the leader of any existing team");
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
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            t.get().setName(name);
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error("failure, you are not the leader of any existing team");
        }
    }

    /**
     * Prida do tymu noveho clena
     * 
     * @param id ID clena, ktery ma byt pridat do tymu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/addmember")
    Response addMember(@RequestParam long id) {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            Optional<UserRC> u = this.userRepository.findById(id);
            if (u.isPresent()) {
                t.get().getMembers().add(u.get());
                return ResponseHandler.response("success");
            } else {
                return ResponseHandler.response(String.format("failure, user with ID [%d] not found", id));
            }
        } else {
            return ResponseHandler.error("failure, you are not the leader of any existing team");
        }
    }

    /**
     * Odebere z tymu jednoho clena
     * 
     * @param id ID clena, ktery ma byt odebran z tymu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/removeMember")
    Response removeMember(@RequestParam long id) {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            Optional<UserRC> u = this.userRepository.findById(id);
            if (u.isPresent()) {
                t.get().getMembers().remove(u.get());
                return ResponseHandler.response("success");
            } else {
                return ResponseHandler.response(String.format("failure, user with ID [%d] not found", id));
            }
        } else {
            return ResponseHandler.error("failure, you are not the leader of any existing team");
        }
    }

}
