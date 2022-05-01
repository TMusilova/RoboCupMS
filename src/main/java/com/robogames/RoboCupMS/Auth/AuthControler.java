package com.robogames.RoboCupMS.Auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Enum.ERole;
import com.robogames.RoboCupMS.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConfig.AUTH_PREFIX)
public class AuthControler {

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
    @GetMapping("/login")
    public Response login(@RequestParam String email, @RequestParam String password) {
        Optional<UserRC> user = repository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().passwordMatch(password)) {
                String token = UUID.randomUUID().toString();
                UserRC u = user.get();
                u.setToken(token);
                repository.save(u);
                return ResponseHandler.response(token);
            }
        }
        return ResponseHandler.error("Incorrect email or password");
    }

    /**
     * Odhlasi uzivatele ze systemu (odstrani pristupovy token z databaze)
     * 
     * @param email Email uzivatele
     * @return Status
     */
    @GetMapping("/logout")
    public Response logout() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                if (user instanceof UserRC) {
                    ((UserRC) user).setToken(null);
                    repository.save(((UserRC) user));
                    return ResponseHandler.response("SUCCESS");
                }
            }
        }
        return ResponseHandler.error("FAILURE");
    }

    /**
     * Registruje noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @return Nove vytvoreni uzivatel
     */
    @PostMapping("/register")
    public Response register(@RequestBody UserRC newUser) {
        if (this.repository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseHandler.error("failure, user with this email already exists");
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
            return ResponseHandler.response("success");
        }
    }

}
