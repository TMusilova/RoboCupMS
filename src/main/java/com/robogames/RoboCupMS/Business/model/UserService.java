package com.robogames.RoboCupMS.Business.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.robogames.RoboCupMS.Entity.Role;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.RoleRepository;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Zajistuje spravu uzivatelu v systemu
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Navrati info o prihlasenem uzivateli
     * 
     * @return Informace o uzivateli
     */
    public UserRC getInfo() {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    /**
     * Navrati vsechny uzivatele
     * 
     * @return Vsichni uzivatele v databazi
     */
    public List<UserRC> getAll() {
        return repository.findAll();
    }

    /**
     * Navrati jednoho uzivatele se specifickym id
     * 
     * @param id ID hledaneho uzivatele
     * @return Informace o uzivateli
     * @throws Exception
     */
    public UserRC getByID(Long id) throws Exception {
        Optional<UserRC> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception(String.format("failure, user with ID [%d] not found", id));
        }
    }

    /**
     * Navrati jednoho uzivatele se specifickym emailem
     * 
     * @param id ID hledaneho uzivatele
     * @return Informace o uzivateli
     * @throws Exception
     */
    public UserRC getByEmail(String email) throws Exception {
        Optional<UserRC> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception(String.format("failure, user with email adress [%s] not found", email));
        }
    }

    /**
     * Prida do databaze noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @throws Exception
     */
    public void add(UserRC newUser) throws Exception {
        List<ERole> roles = new ArrayList<ERole>();
        roles.add(ERole.COMPETITOR);
        UserRC user = new UserRC(
                newUser.getName(),
                newUser.getSurname(),
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getBirthDate(),
                roles);
        this.repository.save(user);
    }

    /**
     * Editace atributu uzivatele s konktretnim ID
     * 
     * @param newUser Nove atributy uzivatele
     * @param id      ID uzivatele jehoz atributy budou zmeneny
     * @throws Exception
     */
    public void edit(UserRC newUser, String uuid) throws Exception {
        Optional<UserRC> map = repository.findByUuid(uuid)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setEmail(newUser.getEmail());
                    user.setBirthDate(newUser.getBirthDate());
                    return repository.save(user);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Zmena uzivatelskeho hesla
     * 
     * @param oldPassword Stare heslo
     * @param newPasword  Nove heslo
     * @throws Exception
     */
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        UserRC user = (UserRC) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.passwordMatch(oldPassword)) {
            user.setPassword(newPassword);
            this.repository.save(user);
        } else {
            throw new Exception("failure, your old password is invalid");
        }
    }

    /**
     * Vygenerovat nove heslo
     * 
     * @param newPasword Nove heslo
     * @throws Exception
     */
    public void generatePassword(String newPassword, String uuid) throws Exception {
        Optional<UserRC> user = repository.findByUuid(uuid);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            this.repository.save(user.get());
        } else {
            throw new Exception(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Prida roli uzivateli
     * 
     * @param role Nova role, kterou prideli uzivateli
     * @param id   ID uzivatele jehoz atributy budou zmeneny
     * @throws Exception
     */
    public void addRole(ERole role, String uuid) throws Exception {
        // overi zda role existuje
        Optional<Role> newRole = this.roleRepository.findByName(role);
        if (!newRole.isPresent()) {
            throw new Exception(String.format("failure, role [%s] not exists", role.toString()));
        }

        // provede zmeny
        Optional<UserRC> map = repository.findByUuid(uuid)
                .map(user -> {
                    Set<Role> roles = user.getRoles();
                    if (!roles.contains(newRole.get())) {
                        roles.add(newRole.get());
                    }
                    return repository.save(user);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Prida roli uzivateli
     * 
     * @param role Nova role, kterou prideli uzivateli
     * @param id   ID uzivatele jehoz atributy budou zmeneny
     * @throws Exception
     */
    public void removeRole(ERole role, String uuid) throws Exception {
        // overi zda role existuje
        Optional<Role> newRole = this.roleRepository.findByName(role);
        if (!newRole.isPresent()) {
            throw new Exception(String.format("failure, role [%s] not exists", role.toString()));
        }

        // provede zmeny
        Optional<UserRC> map = repository.findByUuid(uuid)
                .map(user -> {
                    Set<Role> roles = user.getRoles();
                    if (roles.contains(newRole.get())) {
                        roles.remove(newRole.get());
                    }
                    return repository.save(user);
                });
        if (!map.isPresent()) {
            throw new Exception(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

    /**
     * Odebere uzivatele
     * 
     * @param id ID uzivatele, ktery ma byt odebran
     * @throws Exception
     */
    public void delete(String uuid) throws Exception {
        Optional<UserRC> user = repository.findByUuid(uuid);
        if (user.isPresent()) {
            this.repository.delete(user.get());
        } else {
            throw new Exception(String.format("failure, user with UUID [%s] not found", uuid));
        }
    }

}
