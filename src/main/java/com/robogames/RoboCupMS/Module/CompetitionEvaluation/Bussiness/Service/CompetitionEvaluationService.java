package com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Entity.ScoreAggregation;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.RobotScore;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.TeamScore;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;
import com.robogames.RoboCupMS.Repository.RobotRepository;
import com.robogames.RoboCupMS.Repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetitionEvaluationService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private RobotRepository robotRepository;

    /**
     * Navrati skore vsech robotu, kteri soutezili v danem rocniku
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech roboku a jejich skore v soutezi
     */
    public List<RobotScore> getScoreOfAll(int year) throws Exception {
        // overi zda rocnik souteze existuje
        Optional<Competition> competition = this.competitionRepository.findByYear(year);
        if (!competition.isPresent()) {
            throw new Exception(String.format("failure, competition [%d] not exists", year));
        }

        // pro vsechny registrovane tymy vypoci celkove skore u vsech jejich potvrzenych
        // robotu
        List<RobotScore> scoreList = new LinkedList<RobotScore>();

        List<TeamRegistration> registrations = competition.get().getRegistrations();
        for (TeamRegistration reg : registrations) {
            List<Robot> robots = reg.getRobots();
            for (Robot r : robots) {
                if (!r.getConfirmed())
                    continue;

                // agregacni funkce skore
                ScoreAggregation ag = r.getDiscipline().getScoreAggregation();
                // funkci aplikuje pro vsechny zapasy, ktere robot odehral
                int totalScore = ag.getTotalScoreInitValue();
                List<RobotMatch> matches = r.getMatches();
                for (RobotMatch m : matches) {
                    totalScore = ag.proccess(totalScore, m.getScore());
                }

                // score s robotem zapise do listu
                scoreList.add(new RobotScore(r, totalScore));
            }
        }

        // serazeni
        Collections.sort(scoreList, new Comparator<RobotScore>() {
            @Override
            public int compare(final RobotScore r1, RobotScore r2) {
                return r1.getScore() - r2.getScore();
            }
        });

        return scoreList;
    }

    /**
     * Navrati skore vsech robotu urciteho tymu
     * 
     * @param year Rocnik
     * @param id   ID tymu
     * @return Navrati skore vsech reobotu v tymu
     */
    public TeamScore getScoreOfTeam(int year, long id) throws Exception {
        // overi zda tym existuje
        Optional<Team> team = this.teamRepository.findById(id);
        if (!team.isPresent()) {
            throw new Exception(String.format("failure, team with ID [%d] not exists", id));
        }

        // najde registraci pro dany rocnik souteze
        Optional<TeamRegistration> registration = team.get().getRegistrations().stream()
                .filter((r) -> (r.getCompatitionYear() == year)).findFirst();
        if (!registration.isPresent()) {
            throw new Exception(String.format("failure, registration for year [%d] not exists", year));
        }

        // pro kazdeho robota spocita skore
        List<RobotScore> scoreList = new ArrayList<RobotScore>();
        List<Robot> robots = registration.get().getRobots();

        robots.stream().forEach((r) -> {
            // prihlaska robota musi byt potvrzena
            if (r.getConfirmed()) {
                // agregacni funkce skore
                ScoreAggregation ag = r.getDiscipline().getScoreAggregation();
                // funkci aplikuje pro vsechny zapasy, ktere robot odehral
                int totalScore = ag.getTotalScoreInitValue();
                List<RobotMatch> matches = r.getMatches();
                for (RobotMatch m : matches) {
                    totalScore = ag.proccess(totalScore, m.getScore());
                }
                scoreList.add(new RobotScore(r, totalScore));
            }
        });

        return new TeamScore(team.get(), scoreList);
    }

    /**
     * Navrati skore robota
     * 
     * @param year Rocnik
     * @param id   ID robota
     * @return Navrati skore robota
     */
    public RobotScore getScoreOfRobot(int year, long id) throws Exception {
        Robot robot = this.robotRepository.getById(id);

        // overi povrzeni robota
        if (!robot.getConfirmed()) {
            throw new Exception(String.format("failure, robot with ID [%d] is not confirmed", id));
        }

        // overi rocnik souteze
        if (robot.getTeamRegistration().getCompatitionYear() != year) {
            throw new Exception(String.format("failure, this robot is not registed in year [%d]", year));
        }

        // agregacni funkce skore
        ScoreAggregation ag = robot.getDiscipline().getScoreAggregation();
        // funkci aplikuje pro vsechny zapasy, ktere robot odehral
        int totalScore = ag.getTotalScoreInitValue();
        List<RobotMatch> matches = robot.getMatches();
        for (RobotMatch m : matches) {
            totalScore = ag.proccess(totalScore, m.getScore());
        }

        return new RobotScore(robot, totalScore);
    }

    /**
     * Navrati viteze ve vsech kategoriich
     * 
     * @param year Rocnik souteze
     * @return Seznam vitezu vsech kategorii
     */
    public void getWinners(int year) throws Exception {

    }

    /**
     * Navrati data pro tisk na diplom za danou disciplinu a misto.
     * 
     * @param year  Rocnik souteze
     * @param id    ID discipliny
     * @param place Umisteni robota v discipline (1-3)
     * @return
     */
    public void getDataForPrinting(int year, long id, int place) throws Exception {

    }

}
