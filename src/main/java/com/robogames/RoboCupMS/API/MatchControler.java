package com.robogames.RoboCupMS.API;

import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Match;
import com.robogames.RoboCupMS.Entity.MatchState;
import com.robogames.RoboCupMS.Enum.EMatchState;
import com.robogames.RoboCupMS.Repository.MatchRepository;
import com.robogames.RoboCupMS.Repository.MatchStateRepository;

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
    private MatchRepository matchRepository;

    @Autowired
    private MatchStateRepository matchStateRepository;

    /**
     * Navrati vsechny zapasy
     * 
     * @return Seznam vsech zapasu
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.matchRepository.findAll());
    }

    /**
     * Navrati vsechny zapasy pro konkretni rocnik
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech zapasu
     */
    @GetMapping("/all")
    Response getAll(@RequestParam int year) {
        Stream<Match> filter = this.matchRepository.findAll().stream()
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
        this.matchRepository.deleteById(id);
        return ResponseHandler.response("success");
    }

    /**
     * Odstrani vsechny zapasy, ktere nalezi do urcite zkupiny
     * 
     * @param groudID ID skupiny, jejiz zapasy maji byt odstraneni
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response removeAll(@RequestParam long groudID) {
        Stream<Match> filter = this.matchRepository.findAll().stream().filter((m) -> (m.getGroupID() == groudID));
        filter.forEach((m) -> {
            this.matchRepository.delete(m);
        });
        return ResponseHandler.response("success");
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
        Optional<Match> m = this.matchRepository.findById(id);
        if (m.isPresent()) {
            // zapise skore zapasu
            m.get().setScore(score);
            this.matchRepository.save(m.get());
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
        Optional<Match> match = this.matchRepository.findById(id);
        if (match.isPresent()) {
            // vynuluje skore a zmeni stav zapasu
            match.get().setScore(0);
            match.get().setMatchState(state);
            this.matchRepository.save(match.get());

            // pokud jde o skupinovy zapas pak pozadavek uplatni i na ostatni zapasy skupiny
            match.get().getMatchGroup().getMatches().stream().forEach((m) -> {
                m.setScore(0);
                m.setMatchState(state);
                this.matchRepository.save(m);
            });

            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.response(String.format("failure, match with ID [%d] not exists", id));
        }
    }

}
