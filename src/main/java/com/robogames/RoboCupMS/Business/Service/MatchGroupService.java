package com.robogames.RoboCupMS.Business.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.Repository.MatchGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu zapasovych skupin
 */
@Service
public class MatchGroupService {

    @Autowired
    private MatchGroupRepository repository;

    /**
     * Navrati vsechny zapasove skupiny
     * 
     * @return Seznam vsech skupin
     */
    public List<MatchGroup> getAll() {
        List<MatchGroup> all = this.repository.findAll();
        return all;
    }

    /**
     * Navrti vsechny zapasove skupiny pro specifikovany identifikator tvurce
     * skupiny
     * 
     * @return Seznam vsech skupin
     */
    public List<MatchGroup> getByCID(Long creatorID) {
        Stream<MatchGroup> list = this.repository.findAll().stream()
                .filter((r) -> (r.getCreatorIdentifier() == creatorID));
        List<MatchGroup> out = new ArrayList<MatchGroup>();
        list.forEach((m) -> {
            out.add(m);
        });
        return out;
    }

    /**
     * Navrati skupinu s konkretnim ID
     * 
     * @param id ID skupiny
     * @return Zapasova skupina
     */
    public MatchGroup getByID(Long id) throws Exception {
        Optional<MatchGroup> g = this.repository.findById(id);
        if (g.isPresent()) {
            return g.get();
        } else {
            throw new Exception(String.format("failure, group with ID [%d] not exists", id));
        }
    }

    /**
     * Vytvori novou zapasovou skupinu
     * 
     * @param creatorid Identifikator tvurce skupiny
     * @return Informace o stavu provedene operace
     */
    public void create(Long creatorID) {
        MatchGroup g = new MatchGroup(creatorID);
        this.repository.save(g);
    }

    /**
     * Odstrani skupinu
     * 
     * @param id ID skupiny
     * @return Informace o stavu provedene operace
     */
    public void remove(Long id) {
        this.repository.deleteById(id);
    }

}
