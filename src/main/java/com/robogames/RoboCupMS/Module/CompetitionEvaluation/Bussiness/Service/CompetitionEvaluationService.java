package com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Robot;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Module.CompetitionEvaluation.Bussiness.Model.RobotScore;
import com.robogames.RoboCupMS.Repository.CategoryRepository;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;
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
    private CategoryRepository categoryRepository;

    /**
     * Navrati skore a poradi vsech robotu, kteri soutezili v danem rocniku
     * 
     * @param year Rocnik souteze
     * @return Seznam vsech roboku a jejich skore v soutezi
     */
    public void getScoreOfAll(int year) throws Exception {

    }

    /**
     * Navrati skore vsech robotu urciteho tymu
     * 
     * @param year Rocnik
     * @param id   ID tymu
     * @return Navrati skore vsech reobotu v tymu
     */
    public void getScoreOfTeam(int year, long id) throws Exception {
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
            int totalScore = 0;
            
            scoreList.add(new RobotScore(r, totalScore));
        });
    }

    /**
     * Navrati skore robota
     * 
     * @param year Rocnik
     * @param id   ID robota
     * @return Navrati skore robota
     */
    public void getScoreOfRobot(int year, long id) throws Exception {

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
