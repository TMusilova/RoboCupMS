package com.robogames.RoboCupMS.Module.OrderManagement.Bussiness.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.robogames.RoboCupMS.Communication;
import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Business.Enum.ECategory;
import com.robogames.RoboCupMS.Business.Enum.EMatchState;
import com.robogames.RoboCupMS.Communication.CallBack;
import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.Entity.MatchState;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;
import com.robogames.RoboCupMS.Repository.MatchGroupRepository;
import com.robogames.RoboCupMS.Repository.MatchStateRepository;
import com.robogames.RoboCupMS.Repository.PlaygroundRepository;
import com.robogames.RoboCupMS.Repository.RobotMatchRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderManagementService {

    private static final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private RobotMatchRepository robotMatchRepository;

    @Autowired
    private MatchGroupRepository matchGroupRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private MatchStateRepository matchStateRepository;

    public OrderManagementService() {
        // bude naslouchat komunikacnimu systemu aplikace
        Communication.getInstance().getCallBacks().add(new CallBack() {
            @Override
            public void callBack(Object sender, Object data) {
                logger.info(">>>>>>>>> " + data + ", " + Communication.getInstance().getCallBacks().size());
                // pokud zachyti http request na endpoint "GlobalConfig.AUTH_PREFIX +
                // /match/writeScore" -> zapas byl
                // odehran -> dojde k obnoveni
                if (data instanceof HttpServletRequest) {
                    logger.info("=== " + ((HttpServletRequest) data).getRequestURI());
                    if (((HttpServletRequest) data).getRequestURI()
                            .equals(GlobalConfig.AUTH_PREFIX + "/match/writeScore")) {
                        // refresh systemu pro rizeni poradi
                        refreshSystem();
                    }
                }
            }
        });
    }

    /**
     * Navrati seznam vsech zapasu, ktere aktualne maji byt odehrany (vyzva k odjeti
     * zapasu)
     * 10
     * 
     * @param year     Rocnik souteze
     * @param category Soutezni kategorie
     * @return Seznam vsech robotu a hrist, na ktere se maji dostavit pro odehrani
     *         zapasu
     */
    public void currentMatches(int year, ECategory category) throws Exception {

    }

    /**
     * Vyzada zmenu zapasu, ktery ma byt aktulane odehran na nekterem z hrist dane
     * discipliny
     * 4
     * 
     * @param year     Rocnik souteze
     * @param id       ID dalsiho zapasu, ktery rozhodci chce, aby byl odehran
     *                 (pokud
     *                 bude zadana zaporna neplatna hodnota pak system vybere
     *                 nahodne ze
     *                 seznamu cekajicih zapasu)
     * @param category Soutezni kategorie
     * @return
     */
    public void requestAnotherMatch(int year, long id, ECategory category) throws Exception {

    }

    /**
     * Navrati pro robota seznam vsech zapasu, ktere cekaji na odehrani
     * 
     * @param year Rocnik souteze
     * @param id   ID robota
     * @return Seznam vsech zapasu robota, ktere prace cekaji na odehrani
     */
    public List<RobotMatch> waitingMatches(int year, long id) throws Exception {
        // overi zda robot existuje
        Optional<Robot> robot = this.robotRepository.findById(id);
        if (!robot.isPresent()) {
            throw new Exception(String.format("failure, robot with ID [%d] not exists", id));
        }

        // seznam vsech zapasu robota, ktere prace cekaji na odehrani "stav:
        // EMatchState.WAITING"
        Stream<RobotMatch> matches = robot.get().getMatches().stream()
                .filter((m) -> (m.getState().getName() == EMatchState.WAITING));

        return matches.collect(Collectors.toList());
    }

    /**
     * Vygeneruje skupinove zapasy "kazdy s kazdym" (sumo, robo strong, ...).
     * Neoveruje zda jde o disciplinu, ktera umoznuje zapaseni robot proti robotu
     * 
     * 
     * @param year         Rocnik souteze
     * @param robots       Seznam ID robotu, pro ktere se zapasy vytvori
     * @param playgroundID ID hriste kde se zapasy budou konat
     * @return Navrati identifikacni cislo tvurce zapasovych skupin (nasledne muze
     *         byt uplatneno pro odstraneni zapasu)
     */
    public long generateMatches(int year, Long[] robots, long playgroundID) throws Exception {
        // overi zda roucnik souteze existuje
        if (!this.competitionRepository.findByYear(year).isPresent()) {
            throw new Exception(String.format("failure, compatition [%d] not exists", year));
        }

        // overi zda hriste existuje
        Optional<Playground> playground = this.playgroundRepository.findById(playgroundID);
        if (!playground.isPresent()) {
            throw new Exception(String.format("failure, playground with ID [%d] not exists", playgroundID));
        }

        // ID kazdeho robota overi zda (existuje, je registrovany v danem rocniku
        // souteze a zda jsou vsichni roboti ze stejne kategorie a discipliny)
        boolean first = true;
        ECategory mainCategory = ECategory.OPEN;
        Discipline mainDiscipline = null;
        for (Long id : robots) {
            // overeni existence
            Optional<Robot> robot = this.robotRepository.findById(id);
            if (!robot.isPresent()) {
                throw new Exception(String.format("failure, robot with ID [%d] not exists", id));
            }
            // overeni potvrezeni registrace
            if (!robot.get().getConfirmed()) {
                throw new Exception(String.format("failure, registration of robot with ID [%d] is not confirmed", id));
            }
            // overeni zda je robot registrovan v danem rocniku souteze
            if (robot.get().getTeamRegistration().getCompatitionYear() != year) {
                throw new Exception(String.format("failure, registration of robot with ID [%d] is not confirmed", id));
            }
            // prvotni inicializace kategorie a discipliny vsech robotu
            if (first) {
                mainCategory = robot.get().getCategory();
                mainDiscipline = robot.get().getDiscipline();
            }
            // overeni stejne kategorie a discipliny
            if (robot.get().getCategory() != mainCategory) {
                throw new Exception(String.format("failure, robot with ID [%d] is from different category", id));
            }
            if (robot.get().getDiscipline() != mainDiscipline) {
                throw new Exception(String.format("failure, robot with ID [%d] is from different discipline", id));
            }
        }

        // vygeneruje unikatni id pro naslednou moznost odstraneni vsech techto
        // vygenerovanych zapasu
        long creatorIdentifier = MatchGroup.generateCreatorIdentifier(this.matchGroupRepository);

        // vygeneruje vsechny kombinace zapasu mezi roboty
        MatchState matchState = this.matchStateRepository.findByName(EMatchState.WAITING).get();
        for (int i = 0; i < robots.length - 1; ++i) {
            for (int j = i + 1; j < robots.length; ++j) {
                // vytvori zapasovou skupinu
                MatchGroup group = new MatchGroup(creatorIdentifier);
                this.matchGroupRepository.save(group);

                Robot r1 = this.robotRepository.getById(robots[i]);
                Robot r2 = this.robotRepository.getById(robots[j]);
                // vytvori zapas pro oba roboty
                RobotMatch m1 = new RobotMatch(r1, group, playground.get(), matchState);
                RobotMatch m2 = new RobotMatch(r2, group, playground.get(), matchState);
                this.robotMatchRepository.save(m1);
                this.robotMatchRepository.save(m2);
            }
        }

        return creatorIdentifier;
    }

    /**
     * 10
     * Refresh systemu pro rizeni poradi (vola se automaticky pri kazdem http
     * reqestu na zapis skore nejakeho zapasu)
     */
    private void refreshSystem() {

    }

}
