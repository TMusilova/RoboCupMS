package com.robogames.RoboCupMS.API;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.MatchGroup;
import com.robogames.RoboCupMS.business.model.MatchGroupService;

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
    private MatchGroupService groupService;

    /**
     * Navrati vsechny zapasove skupiny
     * 
     * @return Seznam vsech skupin
     */
    @GetMapping("/all")
    Response getAll() {
        List<MatchGroup> all = this.groupService.getAll();
        return ResponseHandler.response(all);
    }

    /**
     * Navrti vsechny zapasove skupiny pro specifikovany identifikator tvurce
     * skupiny
     * 
     * @return Seznam vsech skupin
     */
    @GetMapping("/getByCID")
    Response getByCID(@RequestParam Long creatorID) {
        List<MatchGroup> groups;
        try {
            groups = this.groupService.getByCID(creatorID);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(groups);
    }

    /**
     * Navrati skupinu s konkretnim ID
     * 
     * @param id ID skupiny
     * @return Zapasova skupina
     */
    @GetMapping("/getByID")
    Response getByID(@RequestParam Long id) {
        MatchGroup group;
        try {
            group = this.groupService.getByID(id);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
        return ResponseHandler.response(group);
    }

    /**
     * Vytvori novou zapasovou skupinu
     * 
     * @param creatorid Identifikator tvurce skupiny
     * @return Informace o stavu provedene operace
     */
    @PostMapping("/create")
    Response create(@RequestParam Long creatorID) {
        try {
            this.groupService.create(creatorID);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odstrani skupinu
     * 
     * @param id ID skupiny
     * @return Informace o stavu provedene operace
     */
    @DeleteMapping("/remove")
    Response remove(@RequestParam Long id) {
        try {
            this.groupService.remove(id);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

}
