package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.dto.LoginDTO;
import me.vasilije.labflow.dto.RegisterDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.UserService;
import me.vasilije.labflow.utils.TokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAPI {

    private final UserService userService;
    private final TokenUtils utils;

    public UserAPI(UserService userService, TokenUtils utils) {
        this.userService = userService;
        this.utils = utils;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginDTO login) {
        return userService.login(login);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegisterDTO register) {
        return userService.registerNewUser(register, register.isTechnician());
    }

    @RequestMapping(path = "/promote/{username}", method = RequestMethod.PUT)
    public ResponseEntity promote(@PathVariable String username, HttpServletRequest req) {

        if(!utils.requestHasToken(req)) {
            return ResponseEntity.status(401).body("You need to provide authentication credentials.");
        }

        try {
            return userService.promote(username, utils.getToken(req));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
