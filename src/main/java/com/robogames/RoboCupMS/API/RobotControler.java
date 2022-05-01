package com.robogames.RoboCupMS.API;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Repository.RobotRepository;
import com.robogames.RoboCupMS.Repository.TeamRegistrationRepository;
import com.robogames.RoboCupMS.Repository.TeamRepository;

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
@RequestMapping(GlobalConfig.API_PREFIX + "/robot")
public class RobotControler {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Navrati vsechny vytvorene robot pro urcitou registraci tymu
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech robotu
     */
    @GetMapping("/all")
    Response getAll(@RequestParam int year) {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // id tymu, ve kterem se uzivatel nachazi
        long team_id = user.getTeamID();
        if (team_id == Team.NOT_IN_TEAM) {
            return ResponseHandler.error("failure, you are not a member of any team");
        }

        // najde tym v datavazi
        Optional<Team> t = this.teamRepository.findById(team_id);
        if (!t.isPresent()) {
            return ResponseHandler.error("failure, team not exists");
        }

        // najde registraci tymu pro dany rocnik souteze
        List<TeamRegistration> registrations = t.get().getRegistrations();
        Optional<TeamRegistration> registration = registrations.stream().filter(r -> (r.getCompatitionYear() == year))
                .findFirst();
        if (!registration.isPresent()) {
            return ResponseHandler.error("failure, team registration not exists");
        }

        // navrati seznam vsech robotu pro danou registraci tymu
        return ResponseHandler.response(registration.get().getRobots());
    }

    /**
     * Vytvori noveho robata. Robot je vytvaren na registraci tymu v urcitem
     * konkretim rocniku souteze.
     * 
     * @param year Rocnik souteze
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam int year, @RequestParam String name) {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // id tymu, ve kterem se uzivatel nachazi
        long team_id = user.getTeamID();
        if (team_id == Team.NOT_IN_TEAM) {
            return ResponseHandler.error("failure, you are not a member of any team");
        }

        // najde tym v datavazi
        Optional<Team> t = this.teamRepository.findById(team_id);
        if (!t.isPresent()) {
            return ResponseHandler.error("failure, team not exists");
        }

        // najde registraci tymu pro dany rocnik souteze
        List<TeamRegistration> registrations = t.get().getRegistrations();
        Optional<TeamRegistration> registration = registrations.stream().filter(r -> (r.getCompatitionYear() == year))
                .findFirst();
        if (!registration.isPresent()) {
            return ResponseHandler.error("failure, team registration not exists");
        }

        // robota je mozne registrovat jen pokud soutez jeste nezacala
        if (registration.get().getCompatition().getStarted()) {
            return ResponseHandler.error("failure, competition has already begun");
        }

        // ulozi robota do databaze
        Robot r = new Robot(name, 0, registration.get());
        this.robotRepository.save(r);
        return ResponseHandler.response("success");
    }

    @DeleteMapping("/remove")
    Response remove() {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseHandler.response("success");
    }

    @PutMapping("/edit")
    Response edit() {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseHandler.response("success");
    }

}
