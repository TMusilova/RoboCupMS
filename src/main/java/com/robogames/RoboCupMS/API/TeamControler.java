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
     * Navrati info o tymu, ve kterem se prihlaseny uzivatel nachazi
     * 
     * @param id ID tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/myTeam")
    Response myTeam() {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getTeamID() != Team.NOT_IN_TEAM) {
            Optional<Team> team = this.teamRepository.findById(user.getTeamID());
            return ResponseHandler.response(team.get());
        } else {
            return ResponseHandler.error("failure, you are not a member of any team");
        }
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
        Optional<Team> team = this.teamRepository.findById(id);
        if (team.isPresent()) {
            return ResponseHandler.response(team);
        } else {
            return ResponseHandler.error(String.format("team with ID [%d] not found", id));
        }
    }

    /**
     * Navrati info o tymu s konkretnim jmenem
     * 
     * @param name Jmeno tymu
     * @return Informace o stavu provedene operace
     */
    @GetMapping("/findByName")
    Response findName(@RequestParam String name) {
        Optional<Team> team = this.teamRepository.findByName(name);
        if (team.isPresent()) {
            return ResponseHandler.response(team);
        } else {
            return ResponseHandler.error(String.format("team with Name [%s] not found", name));
        }
    }

    /**
     * Navrati vsechny tymy
     * 
     * @return Seznam vsech tymu
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.teamRepository.findAll());
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

        if (leader.getTeamID() == Team.NOT_IN_TEAM) {
            Team t = new Team(name, leader);
            this.teamRepository.save(t);
            this.userRepository.save(leader);
            return ResponseHandler.response("success");
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
            // odebere cleny z tymu
            t.get().getMembers().forEach((m) -> {
                m.setTeam(null);
            });
            this.userRepository.saveAll(t.get().getMembers());
            t.get().getMembers().clear();
            // odstrani tym
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
            this.teamRepository.save(t.get());
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
    @PutMapping("/addMember")
    Response addMember(@RequestParam String uuid) {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            Optional<UserRC> u = this.userRepository.findByUuid(uuid);
            if (u.isPresent()) {
                if (u.get().getTeamID() != Team.NOT_IN_TEAM) {
                    return ResponseHandler.error("failure, user is already in team");
                }
                t.get().getMembers().add(u.get());
                u.get().setTeam(t.get());
                this.teamRepository.save(t.get());
                this.userRepository.save(u.get());
                return ResponseHandler.response("success");
            } else {
                return ResponseHandler.response(String.format("failure, user with UUID [%s] not found", uuid));
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
    Response removeMember(@RequestParam String uuid) {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (t.isPresent()) {
            Optional<UserRC> u = this.userRepository.findByUuid(uuid);
            if (u.isPresent()) {
                t.get().getMembers().remove(u.get());
                u.get().setTeam(null);
                this.teamRepository.save(t.get());
                this.userRepository.save(u.get());
                return ResponseHandler.response("success");
            } else {
                return ResponseHandler.response(String.format("failure, user with UUID [%s] not found", uuid));
            }
        } else {
            return ResponseHandler.error("failure, you are not the leader of any existing team");
        }
    }

}
