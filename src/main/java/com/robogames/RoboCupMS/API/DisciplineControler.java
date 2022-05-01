package com.robogames.RoboCupMS.API;

import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Discipline;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.DisciplineRepository;

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
@RequestMapping(GlobalConfig.API_PREFIX + "/discipline")
public class DisciplineControler {

    @Autowired
    private DisciplineRepository disciplineRepository;

    /**
     * Navrati vsechny vytvorene discipliny
     * 
     * @return Seznam disciplin
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.disciplineRepository.findAll());
    }

    /**
     * Navarti disciplinu s konkretim ID
     * 
     * @param id ID pozadovane discipliny
     * @return Disciplina
     */
    @GetMapping("/get")
    Response get(@RequestParam Long id) {
        Optional<Discipline> d = this.disciplineRepository.findById(id);
        if (d.isPresent()) {
            return ResponseHandler.response(d.get());
        } else {
            return ResponseHandler.error(String.format("failure, dicipline with ID [%d] not found", id));
        }
    }

    /**
     * Vytvori novou disciplinu
     * 
     * @param discipline Nova disciplina
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    Response create(@RequestBody Discipline discipline) {
        return ResponseHandler.response(this.disciplineRepository.save(discipline));
    }

    /**
     * Z databaze odstrani disciplinu
     * 
     * @param id ID discipliny, ktera ma byt odstraneni
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    Response remove(@RequestParam Long id) {
        Optional<Discipline> d = this.disciplineRepository.findById(id);
        if (d.isPresent()) {
            this.disciplineRepository.deleteById(id);
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, dicipline with ID [%d] not found", id));
        }
    }

    /**
     * Upravi disciplinu (nazev nebo popis)
     * 
     * @param id         ID discipliny jejiz data maji byt zmeneny
     * @param discipline Nove data discipliny
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/edit")
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    Response edit(@RequestBody Discipline discipline, @RequestParam Long id) {
        Optional<Discipline> map = this.disciplineRepository.findById(id)
                .map(d -> {
                    d.setName(discipline.getName());
                    d.setDescription(discipline.getDescription());
                    return this.disciplineRepository.save(d);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, dicipline with ID [%d] not found", id));
        }
    }

}
