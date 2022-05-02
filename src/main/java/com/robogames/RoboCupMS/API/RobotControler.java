package com.robogames.RoboCupMS.API;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;
import com.robogames.RoboCupMS.Repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

    @Autowired
    private DisciplineRepository disciplineRepository;

    /**
     * Navrati robota s konkretnim ID
     * 
     * @param id ID robota
     * @return Robot
     */
    @GetMapping("/get")
    Response get(@RequestParam Long id) {
        Optional<Robot> robot = this.robotRepository.findById(id);
        if (robot.isPresent()) {
            return ResponseHandler.response(robot.get());
        } else {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not exists", id));
        }
    }

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
            return ResponseHandler.error(String.format("failure, team registration not exists for year [%d]", year));
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
        // ziska registraci tymu v danem rocniku souteze pro prihlaseneho uzivatele
        TeamRegistration registration;
        try {
            registration = this.getTeamRegistration(year);
        } catch (Exception e) {
            return ResponseHandler.error(e.getMessage());
        }

        // overeni unikatnosti jmena robota v ramci rocniku souteze
        if (this.robotRepository.findByName(name).isPresent()) {
            return ResponseHandler
                    .error(String.format("failure, robot with name [%s] already exists in the year [%d]", name, year));
        }

        // ulozi robota do databaze
        Robot r = new Robot(name, 0, registration);
        this.robotRepository.save(r);
        return ResponseHandler.response("success");
    }

    /**
     * Odstrani robota
     * 
     * @param year Rocnik souteze
     * @param id   ID robota
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response remove(@RequestParam int year, @RequestParam Long id) {
        // ziska registraci tymu v danem rocniku souteze pro prihlaseneho uzivatele
        TeamRegistration registration;
        try {
            registration = this.getTeamRegistration(year);
        } catch (Exception e) {
            return ResponseHandler.error(e.getMessage());
        }

        // odstrani robota
        Optional<Robot> robot = registration.getRobots().stream().filter((r) -> (r.getID() == id)).findFirst();
        if (robot.isPresent()) {
            this.robotRepository.delete(robot.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not exists", id));
        }
    }

    /**
     * Zmeni jmeno robota
     * 
     * @param year Rocnik souteze
     * @param id   ID robota
     * @param name Nove jmeno robota
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/rename")
    Response rename(@RequestParam int year, @RequestParam Long id, @RequestParam String name) {
        // ziska registraci tymu v danem rocniku souteze pro prihlaseneho uzivatele
        TeamRegistration registration;
        try {
            registration = this.getTeamRegistration(year);
        } catch (Exception e) {
            return ResponseHandler.error(e.getMessage());
        }

        // overeni unikatnosti jmena robota v ramci rocniku souteze
        if (this.robotRepository.findByName(name).isPresent()) {
            return ResponseHandler
                    .error(String.format("failure, robot with name [%s] already exists in the year [%d]", name, year));
        }

        Optional<Robot> robot = registration.getRobots().stream().filter((r) -> (r.getID()) == id).findFirst();
        // zmeni jmeno robota
        if (robot.isPresent()) {
            robot.get().setName(name);
            this.robotRepository.save(robot.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not found", id));
        }
    }

    /**
     * Registuje existujiciho robota do vybrane discipliny
     * 
     * @param robotID      ID robota, ktereho registrujeme
     * @param disciplineID ID discipliny, do ktere chceme robota registrovat
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/register")
    Response register(@RequestParam Long robotID, @RequestParam Long disciplineID) {
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(robotID);
        if (!robot.isPresent()) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not found", robotID));
        }

        // overi zda robot jiz neni registrovany
        if (robot.get().getDiscipline() != null) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] is already registered", robotID));
        }

        // overi zda registrace robota jiz nebyla povrzena
        if (robot.get().getConfirmed()) {
            return ResponseHandler
                    .error(String.format("failure, robot with ID [%d] has already been confirmed", robotID));
        }

        // overi zda discipliny existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(disciplineID);
        if (!discipline.isPresent()) {
            return ResponseHandler.error(String.format("failure, discipline with ID [%d] not exists", disciplineID));
        }

        // zjisti zda uzivatel vlastni tohoto robota (je v tymu, ktery ho vlastni)
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean ownership = robot.get().getTeamRegistration().getTeam().getMembers().stream()
                .anyMatch((m) -> (m.getID() == user.getID()));

        // provede zmeni (pokud uzivatel robota vlastni pak ho registruje do kategorie)
        if (ownership) {
            robot.get().setDicipline(discipline.get());
            this.robotRepository.save(robot.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, you don't own a robot with ID [%d]", robotID));
        }
    }

    /**
     * Zrusi registraci existujiciho robota
     * 
     * @param id ID robota
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/unregister")
    Response unregister(@RequestParam Long id) {
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(id);
        if (!robot.isPresent()) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not found", id));
        }

        // overi zda robot je robot rigistrovany v nejake discipline
        if (robot.get().getDiscipline() == null) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] is not registered", id));
        }

        // overi zda registrace robota jiz nebyla povrzena
        if (robot.get().getConfirmed()) {
            return ResponseHandler
                    .error(String.format("failure, robot with ID [%d] has already been confirmed", id));
        }

        // zjisti zda uzivatel vlastni tohoto robota (je v tymu, ktery ho vlastni)
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean ownership = robot.get().getTeamRegistration().getTeam().getMembers().stream()
                .anyMatch((m) -> (m.getID() == user.getID()));

        // provede zmeni (pokud uzivatel robota vlastni pak ho registruje do kategorie)
        if (ownership) {
            robot.get().setDicipline(null);
            this.robotRepository.save(robot.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, you don't own a robot with ID [%d]", id));
        }
    }

    /**
     * Povrdi nebo nepovrdi registraci robota
     * 
     * @param id        ID robota
     * @param confirmed Registrace je nebo neni povrzena
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @PutMapping("/confirmRegistration")
    Response confirmRegistration(@RequestParam Long id, @RequestParam Boolean confirmed) {
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(id);
        if (!robot.isPresent()) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not found", id));
        }

        // overi zda je robot prihlasen v nejake discipline
        if (robot.get().getDiscipline() == null) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] is not registered", id));
        }

        // provede zmeny
        robot.get().setConfirmed(confirmed);
        this.robotRepository.save(robot.get());

        return ResponseHandler.response("success");
    }

    /**
     * Lokalni metoda pro nelezeni registrace tymu pro daneho prihlaseneho
     * uzivatele, na kterou je mozne vytvaret roboty.
     * 
     * @param year Rocnik souteze
     * @return TeamRegistration
     * @throws Exception
     */
    private TeamRegistration getTeamRegistration(int year) throws Exception {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // id tymu, ve kterem se uzivatel nachazi
        long team_id = user.getTeamID();
        if (team_id == Team.NOT_IN_TEAM) {
            throw new Exception("failure, you are not a member of any team");
        }

        // najde tym v databazi
        Optional<Team> t = this.teamRepository.findById(team_id);
        if (!t.isPresent()) {
            throw new Exception("failure, team not exists");
        }

        // najde registraci tymu pro dany rocnik souteze
        List<TeamRegistration> registrations = t.get().getRegistrations();
        Optional<TeamRegistration> registration = registrations.stream().filter(r -> (r.getCompatitionYear() == year))
                .findFirst();
        if (!registration.isPresent()) {
            throw new Exception(String.format("failure, team registration not exists for year [%d]", year));
        }

        // robota je mozne registrovat jen pokud soutez jeste nezacala
        if (registration.get().getCompatition().getStarted()) {
            throw new Exception("failure, competition has already begun");
        }

        return registration.get();
    }

}
