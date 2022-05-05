package com.robogames.RoboCupMS.Business.Service;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Entity.RobotMatch;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;
import com.robogames.RoboCupMS.Repository.PlaygroundRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu hrist
 */
@Service
public class PlaygroundService {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    /**
     * Navrati vsechny hriste
     * 
     * @return Seznam vsech souteznich hrist
     */
    public List<Playground> getAll() {
        List<Playground> all = this.playgroundRepository.findAll();
        return all;
    }

    /**
     * Navrati vsechny hriste pro urcitou disciplinu
     * 
     * @param id ID discipliny
     * @return Seznam vsech souteznich hrist pro urcitou disciplinu
     */
    public List<Playground> get(Long id) throws Exception {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(id);
        if (!discipline.isPresent()) {
            throw new Exception(String.format("failure, discipline with ID [%d] not exists", id));
        }

        return discipline.get().getPlaygrounds();
    }

    /**
     * Navrati vsechny zapasy odehrane na konkretnim hristi
     * 
     * @param id ID hriste
     * @return Seznam zapasu
     */
    public List<RobotMatch> getMatches(Long id) throws Exception {
        // overi zda disciplina existuje
        Optional<Playground> p = this.playgroundRepository.findById(id);
        if (p.isPresent()) {
            return p.get().getMatches();
        } else {
            throw new Exception(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

    /**
     * Vytvori nove soutezni hriste
     * 
     * @param name         Jmeno noveho hriste
     * @param number       Cislo noveho hriste
     * @param disciplineID ID discipliny, pro ktere bude nove vytvorene hriste
     *                     urcene
     */
    public void create(String name, int number, Long disciplineID) throws Exception {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(disciplineID);
        if (!discipline.isPresent()) {
            throw new Exception(String.format("failure, discipline with ID [%d] not exists", disciplineID));
        }

        // vytvori hriste
        Playground p = new Playground(name, number, discipline.get());
        this.playgroundRepository.save(p);
    }

    /**
     * Odstrani soutezni hriste
     * 
     * @param id ID hriste, ktere ma byt odstraneno
     */
    public void remove(Long id) throws Exception {
        Optional<Playground> p = this.playgroundRepository.findById(id);
        if (p.isPresent()) {
            // hriste neni mozne odstranit pokud jiz na nem byl odehran nejaky zapas
            if (p.get().getMatches().size() > 0) {
                throw new Exception("failure, playground cannot be removed because a match has been played on it");
            }

            // odstrani hriste
            this.playgroundRepository.delete(p.get());
        } else {
            throw new Exception(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

    /**
     * Upravi parametry souzezniho hriste
     * 
     * @param id           ID hriste, ktere ma byt upraveno
     * @param name         Nove jmeno hriste
     * @param number       Nove cislo hriste
     * @param disciplineID Nove ID discipliny, pro ktere bude hriste urcene
     * 
     */
    public void edit(Long id, String name, int number, Long disciplineID) throws Exception {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(disciplineID);
        if (!discipline.isPresent()) {
            throw new Exception(String.format("failure, discipline with ID [%d] not exists", disciplineID));
        }

        // provede zmeni
        Optional<Playground> map = this.playgroundRepository.findById(id)
                .map(p -> {
                    p.setName(name);
                    p.setNumber(number);
                    p.setDiscipline(discipline.get());
                    return this.playgroundRepository.save(p);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

}
