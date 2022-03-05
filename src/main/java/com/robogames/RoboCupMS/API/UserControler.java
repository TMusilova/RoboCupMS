package com.robogames.RoboCupMS.API;

import java.util.List;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.API.Exception.UserException;
import com.robogames.RoboCupMS.Entity.User;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.API_PREFIX)
public class UserControler {

    private final UserRepository repository;

    public UserControler(UserRepository _repository) {
        this.repository = _repository;
    }

    /**
     * Navrati vsechny uzivatele
     * 
     * @return List<User>
     */
    @GetMapping("/user/all")
    List<User> getAll() {
        return repository.findAll();
    }

    /**
     * Navrati jednoho uzivatele se specifickym id
     * 
     * @param id ID hledaneho uzivatele
     * @return User
     */
    @GetMapping("/user/get_id/{id}")
    User getOne(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new UserException.NotFound(id));
    }

    /**
     * Navrati jednoho uzivatele se specifickym emailem
     * 
     * @param id ID hledaneho uzivatele
     * @return User
     */
    @GetMapping("/user/get_email/{email}")
    User getOne(@PathVariable String email) {
        return repository.findByEmail(email);
    }

    /**
     * Prida do databaze noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @return User
     */
    @PostMapping("/user/add")
    User add(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    /**
     * Nahradi atributy uzivatele s konktretnim ID
     * 
     * @param _newUser Nove atributy uzivatele
     * @param _id      ID uzivatele jehoz atributy budou zmeneny
     * @return User
     */
    @PutMapping("/user/replace/{id}")
    User replace(@RequestBody User newUser, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setEmail(newUser.getEmail());
                    user.setBirthDate(newUser.getBirthDate());
                    user.setRole(newUser.getRole());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserException.NotFound(id));
    }

    /**
     * Odebere uzivatele
     * 
     * @param id ID uzivatele, ktery ma byt odebran
     */
    @DeleteMapping("/user/delete/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
