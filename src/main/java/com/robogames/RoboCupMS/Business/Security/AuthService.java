package com.robogames.RoboCupMS.Business.Security;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

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
        // validace emailu
        // https://mailtrap.io/blog/java-email-validation/
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (!pattern.matcher(email).matches()) {
            throw new Exception("failure, email is invalid");
        }

        // autentizace uzivatele
        Optional<UserRC> user = repository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().passwordMatch(password)) {
                // vygenerovani unikatniho pristupoveho tokenu
                String token = "";
                boolean success = false;
                for (int i = 0; i < 1000; ++i) {
                    token = UUID.randomUUID().toString();
                    if (!repository.findByToken(token).isPresent()) {
                        success = true;
                        break;
                    }
                }

                // nepodarilo se vygenerovat pristupovy token
                if (!success) {
                    throw new Exception("failed to generate access token");
                }

                // ulozi token a cas do databaze
                user.get().setToken(token);
                user.get().setLastAccessTime(new java.util.Date(Calendar.getInstance().getTime().getTime()));
                this.repository.save(user.get());
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
        // overi zda uzivatel s timto email jiz neni registrovany
        if (this.repository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new Exception("failure, user with this email already exists");
        }

        // validace emailu
        // https://mailtrap.io/blog/java-email-validation/
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (!pattern.matcher(newUser.getEmail()).matches()) {
            throw new Exception("failure, email is invalid");
        }

        // registruje noveho uzivatele
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
