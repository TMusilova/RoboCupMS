package com.robogames.RoboCupMS.API;

import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Competition;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.CompetitionRepository;

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
@RequestMapping(GlobalConfig.API_PREFIX + "/competition")
public class CompetitionControler {

    @Autowired
    private CompetitionRepository repository;

    /**
     * Navrati vsechny uskutecnene a naplanovane rocniky soutezi
     * 
     * @return List soutezi
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(repository.findAll());
    }

    /**
     * Navrati vsechny registrace tymu pro dany rocnik
     * 
     * @param year Rocnik souteze
     * @return List vsech registraci
     */
    @GetMapping("/allRegistrations")
    Response allRegistrations(@RequestParam int year) {
        Optional<Competition> c = this.repository.findByYear(year);
        if (c.isPresent()) {
            return ResponseHandler.response(c.get().getRegistrations());
        } else {
            return ResponseHandler.error(String.format("compatition [year: %d] not exists", year));
        }
    }

    /**
     * Vytvori novou soutez
     * 
     * @param compatition Nova soutez
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PostMapping("/create")
    Response create(@RequestBody Competition compatition) {
        if (this.repository.findByYear(compatition.getYear()).isPresent()) {
            return ResponseHandler.error("failure, the competition has already been created for this year");
        } else {
            Competition c = new Competition(
                    compatition.getYear(),
                    compatition.getDate(),
                    compatition.getStartTime(),
                    compatition.getEndTime());
            this.repository.save(c);
            return ResponseHandler.response("success");
        }
    }

    /**
     * Odstrani soutez z databaze a s ni i vsechny data
     * 
     * @param id ID souteze
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @DeleteMapping("/remove")
    Response remove(@RequestParam Long id) {
        Optional<Competition> c = this.repository.findById(id);
        if (c.isPresent()) {
            this.repository.delete(c.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("compatition with ID [%d] not exists", id));
        }
    }

    /**
     * Upravi parametry souteze, mozne jen pokud jeste nezacala
     * 
     * @param id          ID souteze jejiz parametry maji byt upraveny
     * @param compatition Soutez
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PutMapping("/edit")
    Response edit(@RequestParam Long id, @RequestBody Competition compatition) {
        Optional<Competition> map = repository.findById(id)
                .map(c -> {
                    c.setYear(compatition.getYear());
                    c.setDate(compatition.getDate());
                    c.setStartTime(compatition.getStartTime());
                    c.setEndTime(compatition.getEndTime());
                    c.setStarted(compatition.getStarted());
                    return repository.save(c);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, compatition with ID [%d] not exists", id));
        }
    }

}
