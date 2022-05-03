package com.robogames.RoboCupMS.Business.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.robogames.RoboCupMS.Business.Enum.ERole;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Zajistuje autentizaci a registraci uzivatelu
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    /**
     * Prihlaseni uzivatele do systemu (pokud je email a heslo spravne tak
     * vygeneruje, navrati a zapise do databaze pristupovy token pro tohoto
     * uzivatele)
     * 
     * @param email    Email uzivatele
     * @param password Heslo uzivatele
     * @return Pristupovy token
     */
    public String login(String email, String password) throws Exception {
        Optional<UserRC> user = repository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().passwordMatch(password)) {
                String token = UUID.randomUUID().toString();
                UserRC u = user.get();
                u.setToken(token);
                repository.save(u);
                return token;
            }
        }
        throw new Exception("Incorrect email or password");
    }

    /**
     * Odhlasi uzivatele ze systemu (odstrani pristupovy token z databaze)
     * 
     * @param email Email uzivatele
     * @return Status
     */
    public void logout() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                if (user instanceof UserRC) {
                    ((UserRC) user).setToken(null);
                    repository.save(((UserRC) user));
                    return;
                }
            }
        }
        throw new Exception("failure");
    }

    /**
     * Registruje noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @return Nove vytvoreni uzivatel
     */
    public void register(UserRC newUser) throws Exception {
        if (this.repository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception("failure, user with this email already exists");
        } else {
            List<ERole> roles = new ArrayList<ERole>();
            roles.add(ERole.COMPETITOR);
            UserRC u = new UserRC(
                    newUser.getName(),
                    newUser.getSurname(),
                    newUser.getEmail(),
                    newUser.getPassword(),
                    newUser.getBirthDate(),
                    roles);
            repository.save(u);
        }
    }

}
