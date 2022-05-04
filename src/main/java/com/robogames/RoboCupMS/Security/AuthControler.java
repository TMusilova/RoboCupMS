package com.robogames.RoboCupMS.Security;

import com.robogames.RoboCupMS.GlobalConfig;
import com.robogames.RoboCupMS.Response;
import com.robogames.RoboCupMS.ResponseHandler;
import com.robogames.RoboCupMS.Entity.UserRC;
import com.robogames.RoboCupMS.Business.Security.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
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
    private AuthService authService;

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
        try {
            String token = this.authService.login(email, password);
            return ResponseHandler.response(token);
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Odhlasi uzivatele ze systemu (odstrani pristupovy token z databaze)
     * 
     * @param email Email uzivatele
     * @return Status
     */
    @GetMapping("/logout")
    public Response logout() {
        try {
            this.authService.logout();
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

    /**
     * Registruje noveho uzivatele
     * 
     * @param newUser Novy uzivatel
     * @return Nove vytvoreni uzivatel
     */
    @PostMapping("/register")
    public Response register(@RequestBody UserRC newUser) {
        try {
            this.authService.register(newUser);
            return ResponseHandler.response("success");
        } catch (Exception ex) {
            return ResponseHandler.error(ex.getMessage());
        }
    }

}
