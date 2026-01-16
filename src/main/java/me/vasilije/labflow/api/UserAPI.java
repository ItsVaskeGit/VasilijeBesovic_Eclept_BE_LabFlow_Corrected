package me.vasilije.labflow.api;

import jakarta.servlet.http.HttpServletRequest;
import me.vasilije.labflow.dto.LoginDTO;
import me.vasilije.labflow.dto.RegisterDTO;
import me.vasilije.labflow.exception.UserNotFoundException;
import me.vasilije.labflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAPI {

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginDTO login) {
        return userService.login(login.getUsername(), login.getPassword());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegisterDTO register) {
        return userService.registerNewUser(register.getUsername(), register.getPassword(), register.isTechnician());
    }

    @RequestMapping(path = "/promote/{username}", method = RequestMethod.PUT)
    public ResponseEntity promote(@PathVariable String username, HttpServletRequest req) {

        var jwtToken = req.getHeader("Authorization").split(" ")[1];

        try {
            return userService.promote(username, jwtToken);
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
