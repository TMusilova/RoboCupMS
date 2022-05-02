package com.robogames.RoboCupMS.API;

import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Playground;
import com.robogames.RoboCupMS.Enum.ERole;
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
    private PlaygroundRepository repository;

    /**
     * Navrati vsechny hriste
     * 
     * @return Seznam vsech souteznich hrist
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(repository.findAll());
    }

    /**
     * Vytvori nove soutezni hriste
     * 
     * @param playground Soutezni hriste
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PostMapping("/create")
    Response create(@RequestBody Playground playground) {
        return ResponseHandler.response(this.repository.save(playground));
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
        Optional<Playground> p = this.repository.findById(id);
        if (p.isPresent()) {
            this.repository.delete(p.get());
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
        Optional<Playground> map = repository.findById(id)
                .map(p -> {
                    p.setName(playground.getName());
                    p.setNumber(playground.getNumber());
                    return repository.save(p);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, playground with ID [%d] not exists", id));
        }
    }

}
