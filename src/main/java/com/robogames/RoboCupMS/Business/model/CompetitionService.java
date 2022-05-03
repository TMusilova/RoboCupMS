package com.robogames.RoboCupMS.Business.model;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Entity.TeamRegistration;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu soutezi
 */
@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository repository;

    /**
     * Navrati vsechny uskutecnene a naplanovane rocniky soutezi
     * 
     * @return List soutezi
     */
    public List<Competition> getAll() {
        List<Competition> all = repository.findAll();
        return all;
    }

    /**
     * Navrati vsechny registrace tymu pro dany rocnik
     * 
     * @param year Rocnik souteze
     * @return List vsech registraci
     */
    public List<TeamRegistration> allRegistrations(int year) throws Exception {
        Optional<Competition> c = this.repository.findByYear(year);
        if (c.isPresent()) {
            return c.get().getRegistrations();
        } else {
            throw new Exception(String.format("failure, compatition [year: %d] not exists", year));
        }
    }

    /**
     * Vytvori novou soutez
     * 
     * @param compatition Nova soutez
     */
    public void create(Competition compatition) throws Exception {
        if (this.repository.findByYear(compatition.getYear()).isPresent()) {
            throw new Exception("failure, the competition has already been created for this year");
        } else {
            Competition c = new Competition(
                    compatition.getYear(),
                    compatition.getDate(),
                    compatition.getStartTime(),
                    compatition.getEndTime());
            this.repository.save(c);
        }
    }

    /**
     * Odstrani soutez z databaze a s ni i vsechny data
     * 
     * @param id ID souteze
     */
    public void remove(Long id) throws Exception {
        Optional<Competition> c = this.repository.findById(id);
        if (c.isPresent()) {
            this.repository.delete(c.get());
        } else {
            throw new Exception(String.format("compatition with ID [%d] not exists", id));
        }
    }

    /**
     * Upravi parametry souteze, mozne jen pokud jeste nezacala
     * 
     * @param id          ID souteze jejiz parametry maji byt upraveny
     * @param compatition Soutez
     */
    public void edit(Long id, Competition compatition) throws Exception {
        Optional<Competition> map = repository.findById(id)
                .map(c -> {
                    c.setYear(compatition.getYear());
                    c.setDate(compatition.getDate());
                    c.setStartTime(compatition.getStartTime());
                    c.setEndTime(compatition.getEndTime());
                    c.setStarted(compatition.getStarted());
                    return repository.save(c);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, compatition with ID [%d] not exists", id));
        }
    }

}
