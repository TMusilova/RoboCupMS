package com.robogames.RoboCupMS.Module.OrderManagement.Bussiness.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.Business.Enum.ECategory;
import com.robogames.RoboCupMS.Business.Enum.EScoreAggregation;
import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Entity.ScoreAggregation;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.OrderObj;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.RobotScore;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.TeamScore;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;
import com.robogames.RoboCupMS.Repository.RobotMatchRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;
import com.robogames.RoboCupMS.Repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderManagementService {

    @Autowired
    private RobotMatchRepository robotMatchRepository;

    @Autowired
    private RobotRepository robotRepository;

    /**
     * Navrati seznam vsech zapasu, ktere aktualne maji byt odehrany (vyzva k odjeti
     * zapasu)
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
     * @param year     Rocnik souteze
     * @param id       ID robota
     * @param category Soutezni kategorie
     * @return Seznam vsech
     */
    public void waitingMatches(int year, long id, ECategory category) throws Exception {

    }

    /**
     * Vygeneruje skupinove zapasy "kazdy s kazdym" (sumo, robo strong, ...)
     * 
     * @param year   Rocnik souteze
     * @param robots Seznam ID robotu, pro ktere se zapasy vytvori
     * @return Navrati identifikacni cislo tvurce zapasovych skupin (nasledne muze
     *         byt uplatneno pro odstraneni zapasu)
     */
    public void generateMatches(int year, List<Long> robots) {

    }

}
