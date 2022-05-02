package com.robogames.RoboCupMS.API;

import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;
import com.robogames.RoboCupMS.Repository.PlaygroundRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/playground")
public class PlaygroundControler {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    /**
     * Navrati vsechny hriste
     * 
     * @return Seznam vsech souteznich hrist
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.playgroundRepository.findAll());
    }

    /**
     * Navrati vsechny hriste pro urcitou disciplinu
     * 
     * @return Seznam vsech souteznich hrist pro urcitou disciplinu
     */
    @GetMapping("/get")
    Response get(@RequestParam Long id) {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(id);
        if (!discipline.isPresent()) {
            return ResponseHandler
                    .error(String.format("failure, discipline with ID [%d] not exists", id));
        }

        return ResponseHandler.response(discipline.get().getPlaygrounds());
    }

    /**
     * Navrati vsechny zapasy odehrane na konkretnim hristi
     * 
     * @return Seznam zapasu
     */
    @GetMapping("/getMatches")
    Response getMatches(@RequestParam Long id) {
        // overi zda disciplina existuje
        Optional<Playground> p = this.playgroundRepository.findById(id);
        if (p.isPresent()) {
            return ResponseHandler.response(p.get().getMatches());
        } else {
            return ResponseHandler
                    .error(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

    /**
     * Vytvori nove soutezni hriste
     * 
     * @param name         Jmeno noveho hriste
     * @param number       Cislo noveho hriste
     * @param disciplineID ID discipliny, pro ktere bude nove vytvorene hriste
     *                     urcene
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PostMapping("/create")
    Response create(@RequestParam String name, @RequestParam int number, @RequestParam Long disciplineID) {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(disciplineID);
        if (!discipline.isPresent()) {
            return ResponseHandler
                    .error(String.format("failure, discipline with ID [%d] not exists", disciplineID));
        }

        // vytvori hriste
        Playground p = new Playground(name, number, discipline.get());
        this.playgroundRepository.save(p);
        return ResponseHandler.response("success");
    }

    /**
     * Odstrani soutezni hriste
     * 
     * @param id ID hriste, ktere ma byt odstraneno
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @DeleteMapping("/remove")
    Response remove(@RequestParam Long id) {
        Optional<Playground> p = this.playgroundRepository.findById(id);
        if (p.isPresent()) {
            this.playgroundRepository.delete(p.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

    /**
     * Upravi parametry souzezniho hriste
     * 
     * @param id         ID hriste, ktere ma byt upraveno
     * @param playground Nove parametry hriste
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @PutMapping("/edit")
    Response edit(@RequestParam Long id, @RequestBody Playground playground) {
        // overi zda disciplina existuje
        Optional<Discipline> discipline = this.disciplineRepository.findById(playground.getID());
        if (!discipline.isPresent()) {
            return ResponseHandler
                    .error(String.format("failure, discipline with ID [%d] not exists", playground.getID()));
        }

        // provede zmeni
        Optional<Playground> map = this.playgroundRepository.findById(id)
                .map(p -> {
                    p.setName(playground.getName());
                    p.setNumber(playground.getNumber());
                    p.setDiscipline(discipline.get());
                    return this.playgroundRepository.save(p);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

}
