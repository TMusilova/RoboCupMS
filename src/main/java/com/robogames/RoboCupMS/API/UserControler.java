package com.robogames.RoboCupMS.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX + "/user")
public class UserControler {

    @Autowired
    private UserRepository repository;

    /**
     * Navrati info o prihlasenem uzivateli
     * 
     * @return Informace o uzivateli
     */
    @GetMapping("/info")
    Response getInfo() {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseHandler.response(user);
    }

    /**
     * Navrati vsechny uzivatele
     * 
     * @return Vsichni uzivatele v databazi
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @GetMapping("/all")
    Response getAll() {
        return ResponseHandler.response(repository.findAll());
    }

    /**
     * Navrati jednoho uzivatele se specifickym id
     * 
     * @param id ID hledaneho uzivatele
     * @return Informace o uzivateli
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @GetMapping("/get_id")
    Response getOne(@RequestParam Long id) {
        Optional<UserRC> findById = repository.findById(id);
        if (findById.isPresent()) {
            return ResponseHandler.response(findById);
        } else {
            return ResponseHandler.error(String.format("user with ID [%d] not found", id));
        }
    }

    /**
     * Navrati jednoho uzivatele se specifickym emailem
     * 
     * @param id ID hledaneho uzivatele
     * @return Informace o uzivateli
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER, ERole.Names.ASSISTANT })
    @GetMapping("/get_email")
    Response getOne(@RequestParam String email) {
        Optional<UserRC> findByEmail = repository.findByEmail(email);
        if (findByEmail.isPresent()) {
            return ResponseHandler.response(findByEmail);
        } else {
            return ResponseHandler.error(String.format("user with email adress [%s] not found", email));
        }
    }

    /**
     * Prida do databaze noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PostMapping("/add")
    Response add(@RequestBody UserRC newUser) {
        List<ERole> roles = new ArrayList<ERole>();
        roles.add(ERole.COMPETITOR);
        UserRC u = new UserRC(
                newUser.getName(),
                newUser.getSurname(),
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getBirthDate(),
                roles);
        return ResponseHandler.response(repository.save(u));
    }

    /**
     * Editace atributu uzivatele s konktretnim ID
     * 
     * @param _newUser Nove atributy uzivatele
     * @param _id      ID uzivatele jehoz atributy budou zmeneny
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PutMapping("/edit")
    Response replace(@RequestBody UserRC newUser, @RequestParam Long id) {
        Optional<UserRC> map = repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setEmail(newUser.getEmail());
                    user.setBirthDate(newUser.getBirthDate());
                    user.setRoles(newUser.getRoles());
                    return repository.save(user);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, user with ID [%d] not found", id));
        }
    }

    /**
     * Odebere uzivatele
     * 
     * @param id ID uzivatele, ktery ma byt odebran
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @DeleteMapping("/delete")
    Response delete(@RequestParam Long id) {
        repository.deleteById(id);
        return ResponseHandler.response("success");
    }

}
