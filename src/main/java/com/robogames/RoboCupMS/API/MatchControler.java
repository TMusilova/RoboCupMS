package com.robogames.RoboCupMS.API;

import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.Entity.MatchState;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Enum.EMatchState;
import com.robogames.RoboCupMS.Repository.MatchGroupRepository;
import com.robogames.RoboCupMS.Repository.RobotMatchRepository;
import com.robogames.RoboCupMS.Repository.MatchStateRepository;
import com.robogames.RoboCupMS.Repository.PlaygroundRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;

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
    private RobotMatchRepository robotMatchRepository;

    @Autowired
    private MatchStateRepository matchStateRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private MatchGroupRepository matchGroupRepository;

    /**
     * Navrati vsechny zapasy
     * 
     * @return Seznam vsech zapasu
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.robotMatchRepository.findAll());
    }

    /**
     * Navrati vsechny zapasy pro konkretni rocnik
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech zapasu
     */
    @GetMapping("/allByYear")
    Response allByYear(@RequestParam int year) {
        Stream<RobotMatch> filter = this.robotMatchRepository.findAll().stream()
                .filter((m) -> (m.getRobot().getTeamRegistration().getCompatitionYear() == year));
        return ResponseHandler.response(filter.toArray());
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
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(robotID);
        if (!robot.isPresent()) {
            return ResponseHandler.error(String.format("failure, robot with ID [%d] not exists", robotID));
        }

        // overi zda hriste existuje
        Optional<Playground> playground = this.playgroundRepository.findById(playgroundID);
        if (!playground.isPresent()) {
            return ResponseHandler.error(String.format("failure, playground with ID [%d] not exists", playgroundID));
        }

        // overi zda zapasova skupina existuje, pokud je id skupiny zaporne pak jde o
        // zapas jen jednoho robota (line follower, micromouse, ...)
        MatchGroup group = null;
        if (groupID >= 0) {
            Optional<MatchGroup> gOpt = this.matchGroupRepository.findById(groupID);
            if (!gOpt.isPresent()) {
                return ResponseHandler.error(String.format("failure, group with ID [%d] not exists", groupID));
            }
            group = gOpt.get();
        }

        // ziska stav zapasu
        MatchState state = matchStateRepository.findByName(EMatchState.WAITING).get();

        // vytvori zapas a ulozi ho do databaze
        RobotMatch m = new RobotMatch(
                robot.get(),
                group,
                playground.get(),
                state);
        this.robotMatchRepository.save(m);

        return ResponseHandler.response("success");
    }

    /**
     * Odstrani zapas
     * 
     * @param id ID zapasu
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response remove(@RequestParam long id) {
        // overi zda zapas existuje
        if (!this.robotMatchRepository.findById(id).isPresent()) {
            return ResponseHandler.error(String.format("failure, match with ID [%d] not exists", id));
        }

        this.robotMatchRepository.deleteById(id);
        return ResponseHandler.response("success");
    }

    /**
     * Odstrani vsechny zapasy, ktere nalezi do urcite zkupiny
     * 
     * @param groudID ID skupiny, jejiz zapasy maji byt odstraneni
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/removeAll")
    Response removeAll(@RequestParam long groupID) {
        // najde vsechny zapasy prislusici dane skupine
        Stream<RobotMatch> filter = this.robotMatchRepository.findAll().stream()
                .filter((m) -> (m.getGroupID() == groupID));

        // odstani vsechny nalezene zapasy
        int cnt = 0;
        for (Object m : filter.toArray()) {
            ++cnt;
            this.robotMatchRepository.delete((RobotMatch) m);
        }
        return ResponseHandler.response("success, removed [" + cnt + "]");
    }

    /**
     * Zapise vysledne skore zapasu
     * 
     * @param id    ID zapasu
     * @param score Skore zapasu
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/writeScore")
    Response writeScore(@RequestParam long id, @RequestParam int score) {
        Optional<RobotMatch> m = this.robotMatchRepository.findById(id);
        if (m.isPresent()) {
            // zapise skore zapasu
            m.get().setScore(score);
            this.robotMatchRepository.save(m.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.response(String.format("failure, match with ID [%d] not exists", id));
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
        // novy stav zapasu
        MatchState state = matchStateRepository.findByName(EMatchState.REMATCH).get();

        // provede zmeni
        Optional<RobotMatch> match = this.robotMatchRepository.findById(id);
        if (match.isPresent()) {
            // vynuluje skore a zmeni stav zapasu
            match.get().setScore(0);
            match.get().setMatchState(state);
            this.robotMatchRepository.save(match.get());

            // pokud jde o skupinovy zapas pak pozadavek uplatni i na ostatni zapasy skupiny
            match.get().getMatchGroup().getMatches().stream().forEach((m) -> {
                m.setScore(0);
                m.setMatchState(state);
                this.robotMatchRepository.save(m);
            });

            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.response(String.format("failure, match with ID [%d] not exists", id));
        }
    }

}
