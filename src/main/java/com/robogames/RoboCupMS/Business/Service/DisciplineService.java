package com.robogames.RoboCupMS.Business.Service;

import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu souteznich disciplin
 */
@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    /**
     * Navrati vsechny vytvorene discipliny
     * 
     * @return Seznam disciplin
     */
    public List<Discipline> getAll() {
        List<Discipline> all = this.disciplineRepository.findAll();
        return all;
    }

    /**
     * Navarti disciplinu s konkretim ID
     * 
     * @param id ID pozadovane discipliny
     * @return Disciplina
     */
    public Discipline get(Long id) throws Exception {
        Optional<Discipline> d = this.disciplineRepository.findById(id);
        if (d.isPresent()) {
            return d.get();
        } else {
            throw new Exception(String.format("failure, dicipline with ID [%d] not found", id));
        }
    }

    /**
     * Vytvori novou disciplinu
     * 
     * @param discipline Nova disciplina
     */
    public void create(Discipline discipline) throws Exception {
        this.disciplineRepository.save(discipline);
    }

    /**
     * Z databaze odstrani disciplinu
     * 
     * @param id ID discipliny, ktera ma byt odstraneni
     */
    public void remove(Long id) throws Exception {
        Optional<Discipline> d = this.disciplineRepository.findById(id);

        // overi zda disciplina existuje
        if (!d.isPresent()) {
            throw new Exception(String.format("failure, dicipline with ID [%d] not exists", id));
        }

        // overi zda discipliny nema jiz prirazene nejake hriste
        if (!d.get().getPlaygrounds().isEmpty()) {
            throw new Exception(String.format("failure, dicipline with ID [%d] have created a playgrounds", id));
        }

        // overi zda discipliny nema jiz registrovane nejake roboty
        if (!d.get().getRobots().isEmpty()) {
            throw new Exception(String.format("failure, dicipline with ID [%d] already have registered robots", id));
        }

        // odstrani disciplinu
        this.disciplineRepository.delete(d.get());
    }

    /**
     * Upravi disciplinu (nazev nebo popis)
     * 
     * @param id         ID discipliny jejiz data maji byt zmeneny
     * @param discipline Nove data discipliny
     */
    public void edit(Discipline discipline, Long id) throws Exception {
        Optional<Discipline> map = this.disciplineRepository.findById(id)
                .map(d -> {
                    d.setName(discipline.getName());
                    d.setDescription(discipline.getDescription());
                    return this.disciplineRepository.save(d);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, dicipline with ID [%d] not found", id));
        }
    }

}
