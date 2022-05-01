package com.robogames.RoboCupMS.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.Role;
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
            return ResponseHandler.error(String.format("failure, user with ID [%d] not found", id));
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
            return ResponseHandler.error(String.format("failure, user with email adress [%s] not found", email));
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
     * @param newUser Nove atributy uzivatele
     * @param id      ID uzivatele jehoz atributy budou zmeneny
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/edit")
    Response edit(@RequestBody UserRC newUser, @RequestParam String uuid) {
        Optional<UserRC> map = repository.findByUuid(uuid)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setEmail(newUser.getEmail());
                    user.setBirthDate(newUser.getBirthDate());
                    return repository.save(user);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Zmena uzivatelskeho hesla
     * 
     * @param oldPassword Stare heslo
     * @param newPasword  Nove heslo
     * @return Informace o stavu provedene operace
     */
    @PutMapping("/changePassword")
    Response changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.passwordMatch(oldPassword)) {
            user.setPassword(newPassword);
            this.repository.save(user);
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error("failure, your old password is invalid");
        }
    }

    /**
     * Vygenerovat nove heslo
     * 
     * @param newPasword Nove heslo
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PutMapping("/generatePassword")
    Response generatePassword(@RequestParam String newPassword, @RequestParam String uuid) {
        Optional<UserRC> user = repository.findByUuid(uuid);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            this.repository.save(user.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Zmeni role uzivatele
     * 
     * @param roles Nove atributy uzivatele
     * @param id    ID uzivatele jehoz atributy budou zmeneny
     * @return Informace o stavu provedene operace
     */
    @Secured({ ERole.Names.ADMIN, ERole.Names.LEADER })
    @PutMapping("/editRole")
    Response editRole(@RequestBody Set<Role> roles, @RequestParam String uuid) {
        Optional<UserRC> map = repository.findByUuid(uuid)
                .map(user -> {
                    user.setRoles(roles);
                    return repository.save(user);
                });
        if (map.isPresent()) {
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, user with UUID [%s] not found", uuid));
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
    Response delete(@RequestParam String uuid) {
        Optional<UserRC> user = repository.findByUuid(uuid);
        if (user.isPresent()) {
            this.repository.delete(user.get());
            return ResponseHandler.response("success");
        } else {
            return ResponseHandler.error(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

}
