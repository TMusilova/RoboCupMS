package com.robogames.RoboCupMS.API;

import java.util.Optional;
import java.util.stream.Stream;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.Repository.MatchGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/group")
public class MatchGroupControler {

    @Autowired
    private MatchGroupRepository repository;

    /**
     * Navrati vsechny zapasove skupiny
     * 
     * @return Seznam vsech skupin
     */
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(this.repository.findAll());
    }

    /**
     * Navrti vsechny zapasove skupiny pro specifikovany identifikator tvurce
     * skupiny
     * 
     * @return Seznam vsech skupin
     */
    @GetMapping("/getByCID")
    Response getByCID(@RequestParam Long creatorID) {
        Stream<MatchGroup> list = this.repository.findAll().stream()
                .filter((r) -> (r.getCreatorIdentifier() == creatorID));
        return ResponseHandler.response(list.toArray());
    }

    /**
     * Navrati skupinu s konkretnim ID
     * 
     * @param id ID skupiny
     * @return Zapasova skupina
     */
    @GetMapping("/getByID")
    Response getByID(@RequestParam Long id) {
        Optional<MatchGroup> g = this.repository.findById(id);
        if (g.isPresent()) {
            return ResponseHandler.response(g);
        } else {
            return ResponseHandler.error(String.format("failure, group with ID [%d] not exists", id));
        }
    }

    /**
     * Vytvori novou zapasovou skupinu
     * 
     * @param creatorid Identifikator tvurce skupiny
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam Long creatorID) {
        MatchGroup g = new MatchGroup(creatorID);
        this.repository.save(g);
        return ResponseHandler.response("success");
    }

    /**
     * Odstrani skupinu
     * 
     * @param id ID skupiny
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response remove(@RequestParam Long id) {
        this.repository.deleteById(id);
        return ResponseHandler.response("success");
    }

}
