package com.robogames.RoboCupMS.Business.model;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Category;
import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.Team;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Enum.ECategory;
import com.robogames.RoboCupMS.Repository.CategoryRepository;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;
import com.robogames.RoboCupMS.Repository.TeamRegistrationRepository;
import com.robogames.RoboCupMS.Repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Zajistuje registraci tymu do souteze
 */
@Service
public class TeamRegistrationService {

    @Autowired
    private TeamRegistrationRepository registrationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Registruje tym do souteze (registrovat muze pouze vedouci tymu!!!!!)
     * 
     * @param year Rocni souteze, do ktere se tym chce registrovate
     * @throws Exception
     */
    public void register(int year, Boolean open) throws Exception {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // overi zda uzivatel je vedoucim nejakeho tymu
        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (!t.isPresent()) {
            throw new Exception("failure, you are not the leader of any existing team");
        }

        // overi zda rocnik souteze, do ktereho se hlasi existuje
        Optional<Competition> c = competitionRepository.findByYear(year);
        if (!c.isPresent()) {
            throw new Exception(String.format("failure, compatition [year: %d] not exists", year));
        }

        // overi zda soutez jiz nezacala (registrace je mozna jen pokud soutez jeste
        // nezacala)
        if (c.get().getStarted()) {
            throw new Exception(String.format("failure, competition has already begun", year));
        }

        // overi zda tym jiz neni prihlasen do tohoto rocniku
        List<TeamRegistration> registrations = t.get().getRegistrations();
        if (registrations.stream().anyMatch((r) -> (r.getCompatitionYear() == c.get().getYear()))) {
            throw new Exception("failure, team is already registred in this year of compatition");
        }

        // urci kategorii tymu
        ECategory cat_name;
        if (!open) {
            cat_name = t.get().determinateCategory();
        } else {
            cat_name = ECategory.OPEN;
        }
        Optional<Category> cat = this.categoryRepository.findByName(cat_name);
        if (!cat.isPresent()) {
            throw new Exception("failure, category not exists");
        }

        // registruje tym do souteze
        TeamRegistration r = new TeamRegistration(
                t.get(),
                c.get(),
                cat.get());

        this.registrationRepository.save(r);
    }

    /**
     * Zrusi registraci tymu
     * 
     * @param year Rocni souteze
     * @throws Exception
     */
    public void unregister(int year) throws Exception {
        UserRC leader = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // overi zda uzivatel je vedoucim nejakeho tymu
        Optional<Team> t = this.teamRepository.findByLeader(leader);
        if (!t.isPresent()) {
            throw new Exception("failure, you are not the leader of any existing team");
        }

        // overi zda rocnik souteze existuje
        Optional<Competition> c = competitionRepository.findByYear(year);
        if (!c.isPresent()) {
            throw new Exception(String.format("failure, compatition [year: %d] not exists", year));
        }

        // overi zda soutez jiz nezacala (registrace je mozna jen pokud soutez jeste
        // nezacala)
        if (c.get().getStarted()) {
            throw new Exception("failure, competition has already begun");
        }

        // najde registraci tymu v seznamu registraci daneho tymu
        List<TeamRegistration> registrations = t.get().getRegistrations();
        Optional<TeamRegistration> registration = registrations.stream().filter(r -> (r.getCompatitionYear() == year))
                .findFirst();
        if (!registration.isPresent()) {
            throw new Exception("failure, team registration not exists");
        }

        // odstrani registraci
        this.registrationRepository.delete(registration.get());
    }

    /**
     * Navrati vsechny registrace tymu, ve kterem se uzivatel nachazi (vsehny
     * rocniky, kterych se ucastnil)
     * 
     * @return Seznam vsech registraci
     * @throws Exception
     */
    public List<TeamRegistration> getAll() throws Exception {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // id tymu, ve kterem se uzivatel nachazi
        long team_id = user.getTeamID();
        if (team_id == Team.NOT_IN_TEAM) {
            throw new Exception("failure, you are not a member of any team");
        }

        // najde tym v datavazi
        Optional<Team> t = this.teamRepository.findById(team_id);
        if (!t.isPresent()) {
            throw new Exception("failure, team not exists");
        }

        // navrati vsechny registrace
        return t.get().getRegistrations();
    }

}
