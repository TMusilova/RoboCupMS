package com.robogames.RoboCupMS.Business.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.Communication;
import com.robogames.RoboCupMS.Business.Enum.ECategory;
import com.robogames.RoboCupMS.Business.Enum.EMatchState;
import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.Entity.MatchState;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Repository.MatchGroupRepository;
import com.robogames.RoboCupMS.Repository.MatchStateRepository;
import com.robogames.RoboCupMS.Repository.PlaygroundRepository;
import com.robogames.RoboCupMS.Repository.RobotMatchRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu zapasu
 */
@Service
public class MatchService {

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
    public List<RobotMatch> getAll() {
        List<RobotMatch> all = this.robotMatchRepository.findAll();
        return all;
    }

    /**
     * Navrati vsechny zapasy pro konkretni rocnik
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech zapasu
     */
    public List<RobotMatch> allByYear(int year) {
        Stream<RobotMatch> filter = this.robotMatchRepository.findAll().stream()
                .filter((m) -> (m.getRobot().getTeamRegistration().getCompatitionYear() == year));
        List<RobotMatch> out = new ArrayList<RobotMatch>();
        filter.forEach((m) -> {
            out.add(m);
        });
        return out;
    }

    /**
     * Naplanuje novy zapas
     * 
     * @param robotID      ID robota, ktery bude zapasit
     * @param playgroundID ID hriste, na kterem se bude hrat
     * @param groupID      ID zapasove skupiny. Jen v pripade pokud zapasi proti
     *                     sobe vice robotu. V opacnem pripade zadat neplatnou
     *                     zapornou hodnotu.
     */
    public void create(long robotID, long playgroundID, long groupID)
            throws Exception {
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(robotID);
        if (!robot.isPresent()) {
            throw new Exception(String.format("failure, robot with ID [%d] not exists", robotID));
        }

        // overi zda hriste existuje
        Optional<Playground> playground = this.playgroundRepository.findById(playgroundID);
        if (!playground.isPresent()) {
            throw new Exception(String.format("failure, playground with ID [%d] not exists", playgroundID));
        }

        // overi zda ma robot povoleno zapasit (registrace byla uspesna => povoluje se
        // pri kontrole pred zacatkem souteze)
        if (!robot.get().getConfirmed()) {
            throw new Exception(String.format("failure, robot with ID [%d] is not confirmed", robotID));
        }

        // overi zda zapasova skupina existuje, pokud je id skupiny zaporne pak jde o
        // zapas jen jednoho robota (line follower, micromouse, ...)
        MatchGroup group = null;
        if (groupID >= 0) {
            Optional<MatchGroup> gOpt = this.matchGroupRepository.findById(groupID);
            if (!gOpt.isPresent()) {
                throw new Exception(String.format("failure, group with ID [%d] not exists", groupID));
            }
            group = gOpt.get();

            // overi zda maji vsichni roboti stejnou kategorii ve skupine
            ECategory mainCategory = robot.get().getTeamRegistration().getCategory();
            List<RobotMatch> matches = group.getMatches();
            for (RobotMatch matche : matches) {
                if (matche.getRobot().getTeamRegistration().getCategory() != mainCategory) {
                    throw new Exception(
                            String.format("failure, the robots in the group are not in the same category", groupID));
                }
            }
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
    }

    /**
     * Odstrani zapas
     * 
     * @param id ID zapasu
     */
    public void remove(long id) throws Exception {
        // overi zda zapas existuje
        if (!this.robotMatchRepository.findById(id).isPresent()) {
            throw new Exception(String.format("failure, match with ID [%d] not exists", id));
        }

        this.robotMatchRepository.deleteById(id);
    }

    /**
     * Odstrani vsechny zapasy, ktere nalezi do urcite zkupiny
     * 
     * @param groudID ID skupiny, jejiz zapasy maji byt odstraneni
     * @return Pocet odstranenych zapasu
     */
    public int removeAll(long groupID) {
        // najde vsechny zapasy prislusici dane skupine
        Stream<RobotMatch> filter = this.robotMatchRepository.findAll().stream()
                .filter((m) -> (m.getGroupID() == groupID));

        // odstani vsechny nalezene zapasy
        int cnt = 0;
        for (Object m : filter.toArray()) {
            ++cnt;
            this.robotMatchRepository.delete((RobotMatch) m);
        }
        return cnt;
    }

    /**
     * Zapise vysledne skore zapasu
     * 
     * @param id    ID zapasu
     * @param score Skore zapasu
     */
    public void writeScore(long id, float score) throws Exception {
        Optional<RobotMatch> m = this.robotMatchRepository.findById(id);
        if (m.isPresent()) {
            // zapise skore zapasu
            m.get().setScore(score);
            // nastavi stav jako odehrany
            MatchState state = matchStateRepository.findByName(EMatchState.DONE).get();
            m.get().setMatchState(state);
            this.robotMatchRepository.save(m.get());
            // odesle do komunikacniho systemu zparavu o zapisu skore
            Communication.getInstance().sendAll(this, "writeScore");
        } else {
            throw new Exception(String.format("failure, match with ID [%d] not exists", id));
        }
    }

    /**
     * Vyzada, aby zapas byl odehran znovu. Pokud jde o skupinovy zapas automaticky
     * tento pozadavek vyzada i u ostatnich zapasu.
     * 
     * @param id ID zapasu
     */
    public void rematch(long id) throws Exception {
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
        } else {
            throw new Exception(String.format("failure, match with ID [%d] not exists", id));
        }
    }

}
